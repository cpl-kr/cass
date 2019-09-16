package de.platen.cass.guiserver.frontend.guielement;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import de.platen.cass.guiserver.eventsender.EventSenderKeyboard;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesTextInput;
import de.platen.cass.guiserver.renderer.HtmlRenderEngine;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class GuiElementTextInput extends GuiElement {

	private final ImagesTextInput imagesTextInput;
	private final HtmlRenderEngine htmlRenderEngine;
	private final TextInputHtmlInput textInputHtmlInput;
	private final int characterCount;
	private final byte[] cursor;

	private String inputText = "";
	private int cursorPosition = 0;

	private EventHandlerMouseReleased eventHandlerMouseReleased = null;
	private EventHandlerMouseClicked eventHandlerMouseClicked = null;

	public GuiElementTextInput(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesTextInput imagesTextInput, State startState, HtmlRenderEngine htmlRenderEngine,
			TextInputHtmlInput textInputHtmlInput, int characterCount, byte[] cursor) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesTextInput = imagesTextInput;
		this.htmlRenderEngine = htmlRenderEngine;
		this.textInputHtmlInput = textInputHtmlInput;
		this.characterCount = characterCount;
		this.cursor = cursor;
		this.handleNewActualState(startState);
	}

	@Override
	public void handleNewActualState(State state) {
		if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE) || state.equals(State.FOCUS)) {
			actualState = state;
			this.imagesTextInput.setImage(state);
		} else {
			throw new GuiServerException("Kein gültiger State für GuiElementTextInput.");
		}
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesTextInput.setImage(view, image);

	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesTextInput.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
		this.eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
		eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
		this.eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
		eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
		eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
		this.eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
		this.imagesTextInput.addEventFilter(eventHandlerMouseEntered, eventHandlerMouseExited,
				eventHandlerMouseReleased, eventHandlerMouseClicked, eventHandlerMouseMoved);
		if (eventsToSend.contains(Event.ENTERED)) {
			eventHandlerMouseEntered.setHasToSend(true);
		}
		if (eventsToSend.contains(Event.EXITED)) {
			eventHandlerMouseExited.setHasToSend(true);
		}
		if (eventsToSend.contains(Event.MOVED)) {
			eventHandlerMouseMoved.setHasToSend(true);
		}
		eventsToSend.clear();
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
		this.imagesTextInput.setImage(eventHandlerMouse, state);
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesTextInput.getImageView();
	}

	public synchronized void handleInput(String character, KeyCode keyCode, String text,
			EventSenderKeyboard eventSenderKeyboard) {
		if (actualState.equals(State.FOCUS)) {
			if (text.equals("")) {
				if (keyCode == KeyCode.UNDEFINED) {
					if (character.equals("\r")) {
						eventSenderKeyboard.sendEvent(getId(), this.inputText + "\r");
					} else {
						this.handleInput(character);
						eventSenderKeyboard.sendEvent(getId(), character);
					}
				} else {
					switch (keyCode) {
					case LEFT:
						if (this.cursorPosition > 0) {
							this.cursorPosition--;
						}
						this.handleInput("");
						break;
					case RIGHT:
						if (this.cursorPosition < this.inputText.length()) {
							this.cursorPosition++;
						}
						this.handleInput("");
						break;
					case BACK_SPACE:
						this.handleBackspace();
						break;
					case DELETE:
						this.handleDelete();
						break;
					default:
						break;
					}
				}
			} else {
				// this.handleInput(text);
			}
		}
	}

	private void handleInput(String text) {
		if (text.length() > 0) {
			if (text.equals("\b")) {
				this.handleBackspace();
			} else {
				int c = text.charAt(0);
				if (c != 127) {
					if (text.matches("\\S") || text.equals(" ")) {
						if ((this.inputText.length() < this.characterCount) || (this.characterCount == 0)) {
							if (this.cursorPosition >= this.inputText.length()) {
								this.inputText = this.inputText + text;
							} else {
								if (this.cursorPosition == 0) {
									this.inputText = text + this.inputText;
								} else {
									this.inputText = this.inputText.substring(0, this.cursorPosition) + text
											+ this.inputText.substring(this.cursorPosition);
								}
							}
						}
						this.cursorPosition += text.length();
					}
				} else {
					handleDelete();
				}
			}
		}
		createInputImageWithCursor(this.textInputHtmlInput.getHtmlFocus(), View.FOCUS, getWidth(), getHeight(),
				this.inputText, this.imagesTextInput, this.htmlRenderEngine, this.cursor, this.cursorPosition);
		createInputImage(this.textInputHtmlInput.getHtmlActive(), View.ACTIVE, getWidth(), getHeight(), this.inputText,
				this.imagesTextInput, this.htmlRenderEngine);
		createInputImage(this.textInputHtmlInput.getHtmlInactive(), View.INACTIVE, getWidth(), getHeight(),
				this.inputText, this.imagesTextInput, this.htmlRenderEngine);
		createInputImage(this.textInputHtmlInput.getHtmlMouseOverActive(), View.MOUSE_OVER_ACTIVE, getWidth(),
				getHeight(), this.inputText, this.imagesTextInput, this.htmlRenderEngine);
		createInputImageWithCursor(this.textInputHtmlInput.getHtmlMouseOverFocus(), View.MOUSE_OVER_FOCUS, getWidth(),
				getHeight(), this.inputText, this.imagesTextInput, this.htmlRenderEngine, this.cursor,
				this.cursorPosition);
	}

	private void handleDelete() {
		if (this.inputText.length() > 0) {
			if (this.cursorPosition < this.inputText.length()) {
				this.inputText = this.inputText.substring(0, this.cursorPosition)
						+ this.inputText.substring(this.cursorPosition + 1);
			}
		}
	}

	private void handleBackspace() {
		if (this.inputText.length() == 1) {
			this.inputText = "";
			this.cursorPosition = 0;
		} else {
			if (this.cursorPosition > 0) {
				this.inputText = this.inputText.substring(0, this.cursorPosition - 1)
						+ this.inputText.substring(this.cursorPosition);
				this.cursorPosition--;
			}
		}
	}

	private static void createInputImage(HtmlInput htmlInput, View view, int width, int height, String text,
			ImagesTextInput imagesTextInput, HtmlRenderEngine htmlRenderEngine) {
		String html = htmlInput.getHtmlBefore() + text + htmlInput.getHtmlAfter();
		try {
			InputStream inputStream = htmlRenderEngine.renderPng(html.getBytes(), width, height);
			Image image = new Image(inputStream);
			imagesTextInput.setImage(view, image);
		} catch (IOException | SAXException e) {
			throw new GuiServerException("Fehler beim Rendern.");
		}
	}

	private static void createInputImageWithCursor(HtmlInput htmlInput, View view, int width, int height, String text,
			ImagesTextInput imagesTextInput, HtmlRenderEngine htmlRenderEngine, byte[] cursor, int cursorposition) {
		String content = text + new String(cursor);
		if (cursorposition == 0) {
			content = new String(cursor) + text;
		}
		if (cursorposition < text.length()) {
			content = text.substring(0, cursorposition) + new String(cursor) + text.substring(cursorposition);
		}
		String html = htmlInput.getHtmlBefore() + content + htmlInput.getHtmlAfter();
		try {
			InputStream inputStream = htmlRenderEngine.renderPng(html.getBytes(), width, height);
			Image image = new Image(inputStream);
			imagesTextInput.setImage(view, image);
		} catch (IOException | SAXException e) {
			throw new GuiServerException("Fehler beim Rendern.");
		}
	}

	private static EventHandlerMouseClicked getEventHandlerMouseClicked(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				return (EventHandlerMouseClicked) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseEntered getEventHandlerMouseEntered(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				return (EventHandlerMouseEntered) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseExited getEventHandlerMouseExited(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				return (EventHandlerMouseExited) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseReleased getEventHandlerMouseReleased(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
				return (EventHandlerMouseReleased) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseMoved getEventHandlerMouseMoved(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				return (EventHandlerMouseMoved) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}
}
