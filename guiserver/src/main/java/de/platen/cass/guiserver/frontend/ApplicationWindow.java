package de.platen.cass.guiserver.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.java_websocket.WebSocket;

import de.platen.cass.guiserver.eventsender.EventSenderKeyboard;
import de.platen.cass.guiserver.eventsender.EventSenderMouseClicked;
import de.platen.cass.guiserver.eventsender.EventSenderMouseDragged;
import de.platen.cass.guiserver.eventsender.EventSenderMouseEntered;
import de.platen.cass.guiserver.eventsender.EventSenderMouseExited;
import de.platen.cass.guiserver.eventsender.EventSenderMouseMoved;
import de.platen.cass.guiserver.eventsender.EventSenderMousePressed;
import de.platen.cass.guiserver.eventsender.EventSenderMouseReleased;
import de.platen.cass.guiserver.eventsender.EventSenderWindowClosed;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerWindowClosed;
import de.platen.cass.guiserver.frontend.guielement.ElementPosition;
import de.platen.cass.guiserver.frontend.guielement.Event;
import de.platen.cass.guiserver.frontend.guielement.GuiElement;
import de.platen.cass.guiserver.frontend.guielement.GuiElementBrowser;
import de.platen.cass.guiserver.frontend.guielement.GuiElementTextInput;
import de.platen.cass.guiserver.frontend.guielement.State;
import de.platen.cass.guiserver.frontend.guielement.View;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;

public class ApplicationWindow {

	private static final Clipboard systemClipboard = Clipboard.getSystemClipboard();

	private final String windowId;
	private final Map<Integer, Map<Integer, GuiElement>> elementeSortiert;
	private final String sessionId;
	private final WebSocket connection;
	private Stage window;
	private Group group;
	private EventSenderKeyboard eventSenderKeyboard;

	private GuiElementTextInput focusElement = null;

	public ApplicationWindow(String windowId, int x, int y, int width, int height, String titel,
			List<GuiElement> elemente, String sessionId, WebSocket connection) {
		this.windowId = windowId;
		this.elementeSortiert = sortiereElemente(elemente);
		this.sessionId = sessionId;
		this.connection = connection;
		this.eventSenderKeyboard = new EventSenderKeyboard(sessionId, windowId, connection);
		Scene scene = null;
		if (elemente.size() == 1 && elemente.get(0) instanceof GuiElementBrowser) {
			GuiElementBrowser guiElement = (GuiElementBrowser) elemente.get(0);
			guiElement.start();
		} else {
			this.window = new Stage();
			this.group = new Group();
			TreeSet<Integer> ebenenSortiert = new TreeSet<>();
			ebenenSortiert.addAll(this.elementeSortiert.keySet());
			for (Integer keyEbene : ebenenSortiert) {
				Map<Integer, GuiElement> guiElemente = this.elementeSortiert.get(keyEbene);
				TreeSet<Integer> zeichnungSortiert = new TreeSet<>();
				zeichnungSortiert.addAll(guiElemente.keySet());
				for (Integer keyZeichnung : zeichnungSortiert) {
					GuiElement element = guiElemente.get(keyZeichnung);
					element.addInitialFrontView(group);
					addInitialEventHandler(element, this.sessionId, this.windowId, this.connection);
					if (element instanceof GuiElementTextInput) {
						if (element.getActualState().equals(State.FOCUS)) {
							this.focusElement = (GuiElementTextInput) element;
						}
					}
				}
			}
			scene = new Scene(group, width, height);
			scene.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (ApplicationWindow.this.focusElement != null) {
						try {
							String clipboardText = getClipboardText(event);
							if (clipboardText == null) {
								// System.out.println("Texteingabe Key Typed");
								ApplicationWindow.this.focusElement.handleInput(event.getCharacter(), event.getCode(),
										event.getText(), ApplicationWindow.this.eventSenderKeyboard);
							} else {
								System.out.println("Text aus Zwischenablage bei Key Typed: " + clipboardText);
								ApplicationWindow.this.focusElement.handleInput(clipboardText, event.getCode(),
										clipboardText, ApplicationWindow.this.eventSenderKeyboard);
							}
						} catch (GuiServerException e) {
							e.printStackTrace();
						} catch (RuntimeException e) {
							e.printStackTrace();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			});
			scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (ApplicationWindow.this.focusElement != null) {
						try {
							String clipboardText = getClipboardText(event);
							if (clipboardText == null) {
								// System.out.println("Texteingabe Key Pressed");
								ApplicationWindow.this.focusElement.handleInput(event.getCharacter(), event.getCode(),
										event.getText(), ApplicationWindow.this.eventSenderKeyboard);
							} else {
								System.out.println("Text aus Zwischenablage bei Key Pressed: " + clipboardText);
								ApplicationWindow.this.focusElement.handleInput(clipboardText, event.getCode(),
										clipboardText, ApplicationWindow.this.eventSenderKeyboard);
							}
						} catch (GuiServerException e) {
							e.printStackTrace();
						} catch (RuntimeException e) {
							e.printStackTrace();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.window.setScene(scene);
			this.window.sizeToScene();
			this.window.setX(x);
			this.window.setY(y);
			this.window.setTitle(titel);
			this.window.setResizable(false);
			this.window.initStyle(StageStyle.UTILITY);
			EventSenderWindowClosed eventSenderWindowClosed = new EventSenderWindowClosed(this.sessionId, windowId,
					connection);
			EventHandlerWindowClosed eventHandlerWindowClosed = new EventHandlerWindowClosed(eventSenderWindowClosed);
			this.window.setOnCloseRequest(eventHandlerWindowClosed.getEventHandler());
		}
	}

	public void show() {
		if (this.window != null) {
			this.window.show();
		}
	}

	public void close() {
		if (this.window != null) {
			this.window.close();
		}
	}

	public void changeElementState(String elementId, State state) {
		if (elementId != null && state != null) {
			GuiElement guiElement = getGuiElement(this.elementeSortiert, elementId);
			if (guiElement != null) {
				try {
					guiElement.handleNewActualState(state);
					if (guiElement instanceof GuiElementTextInput) {
						if (state.equals(State.FOCUS)) {
							this.focusElement = (GuiElementTextInput) guiElement;
						} else {
							if (this.focusElement.equals(guiElement)) {
								this.focusElement = null;
							}
						}
					}
				} catch (GuiServerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setElementImage(String elementId, View view, Image image) {
		if (elementId != null && view != null && image != null) {
			GuiElement guiElement = getGuiElement(this.elementeSortiert, elementId);
			if (guiElement != null) {
				try {
					guiElement.setImage(view, image);
				} catch (GuiServerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addElement(GuiElement guiElement) {
		if (guiElement != null) {
			int ebene = guiElement.getEbene();
			int zeichnungsnummer = guiElement.getZeichnungsnummer();
			Map<Integer, GuiElement> elementeEbene = this.elementeSortiert.get(new Integer(ebene));
			if (elementeEbene == null) {
				TreeMap<Integer, GuiElement> elemente = new TreeMap<>();
				elemente.put(new Integer(zeichnungsnummer), guiElement);
				this.elementeSortiert.put(new Integer(ebene), elemente);
			} else {
				elementeEbene.put(new Integer(zeichnungsnummer), guiElement);
			}
			guiElement.addInitialFrontView(group);
			addInitialEventHandler(guiElement, this.sessionId, this.windowId, this.connection);
			if (guiElement instanceof GuiElementTextInput) {
				if (guiElement.getActualState() == State.FOCUS) {
					this.focusElement = (GuiElementTextInput) guiElement;
				}
			}
		}
	}

	public void removeElement(String elementId) {
		if (elementId != null) {
			GuiElement guiElement = getGuiElement(this.elementeSortiert, elementId);
			if (guiElement != null) {
				if (this.focusElement != null) {
					if (guiElement.getId() == this.focusElement.getId()) {
						this.focusElement = null;
					}
				}
				if (this.group != null) {
					try {
						this.group.getChildren().remove(guiElement.getImageVieww());
						removeGuiElement(this.elementeSortiert, elementId);
						this.window.hide();
						this.window.show();
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void setEvent(String elementId, Event event, boolean isActive) {
		if (elementId != null) {
			GuiElement guiElement = getGuiElement(this.elementeSortiert, elementId);
			if (guiElement != null) {
				if (isActive) {
					guiElement.registerEventsToSend(event);
				} else {
					guiElement.deregisterEventsToSend(event);
				}
			}
		}
	}

	public String getId() {
		return this.windowId;
	}

	public ElementPosition getElementPosition(String elementId) {
		GuiElement guiElement = getGuiElement(this.elementeSortiert, elementId);
		if (guiElement != null) {
			return new ElementPosition(guiElement.getX(), guiElement.getY(), guiElement.getWidth(),
					guiElement.getHeight());
		}
		return null;
	}

	private static Map<Integer, Map<Integer, GuiElement>> sortiereElemente(List<GuiElement> elemente) {
		Map<Integer, Map<Integer, GuiElement>> elementeSortiert = new TreeMap<>();
		Map<Integer, List<GuiElement>> elementeEbene = new HashMap<>();
		for (GuiElement element : elemente) {
			List<GuiElement> guiElemente = elementeEbene.get(element.getEbene());
			if (guiElemente == null) {
				guiElemente = new ArrayList<>();
				elementeEbene.put(element.getEbene(), guiElemente);
			}
			guiElemente.add(element);
		}
		Set<Integer> keysEbene = elementeEbene.keySet();
		Iterator<Integer> iteratorEbene = keysEbene.iterator();
		while (iteratorEbene.hasNext()) {
			Integer keyEbene = iteratorEbene.next();
			List<GuiElement> guiElemente = elementeEbene.get(keyEbene);
			Map<Integer, GuiElement> elementeZeichnung = new TreeMap<>();
			for (GuiElement element : guiElemente) {
				elementeZeichnung.put(element.getZeichnungsnummer(), element);
			}
			elementeSortiert.put(keyEbene, elementeZeichnung);
		}
		return elementeSortiert;
	}

	private static void addInitialEventHandler(GuiElement guiElement, String sessionId, String windowId,
			WebSocket connection) {
		EventSenderMouseClicked eventSenderMouseClicked = new EventSenderMouseClicked(sessionId, windowId, connection);
		EventSenderMousePressed eventSenderMousePressed = new EventSenderMousePressed(sessionId, windowId, connection);
		EventSenderMouseReleased eventSenderMouseReleased = new EventSenderMouseReleased(sessionId, windowId,
				connection);
		EventSenderMouseEntered eventSenderMouseEntered = new EventSenderMouseEntered(sessionId, windowId, connection);
		EventSenderMouseExited eventSenderMouseExited = new EventSenderMouseExited(sessionId, windowId, connection);
		EventSenderMouseMoved eventSenderMouseMoved = new EventSenderMouseMoved(sessionId, windowId, connection);
		EventSenderMouseDragged eventSenderMouseDragged = new EventSenderMouseDragged(sessionId, windowId, connection);
		EventHandlerMouseClicked eventHandlerMouseClicked = new EventHandlerMouseClicked(guiElement,
				eventSenderMouseClicked);
		EventHandlerMousePressed eventHandlerMousePressed = new EventHandlerMousePressed(guiElement,
				eventSenderMousePressed);
		EventHandlerMouseReleased eventHandlerMouseReleased = new EventHandlerMouseReleased(guiElement,
				eventSenderMouseReleased);
		EventHandlerMouseEntered eventHandlerMouseEntered = new EventHandlerMouseEntered(guiElement,
				eventSenderMouseEntered);
		EventHandlerMouseExited eventHandlerMouseExited = new EventHandlerMouseExited(guiElement,
				eventSenderMouseExited);
		EventHandlerMouseMoved eventHandlerMouseMoved = new EventHandlerMouseMoved(guiElement, eventSenderMouseMoved);
		EventHandlerMouseDragged eventHandlerMouseDragged = new EventHandlerMouseDragged(guiElement,
				eventSenderMouseDragged);
		guiElement.setInitialEventHandlerMouse(eventHandlerMousePressed, eventHandlerMouseEntered,
				eventHandlerMouseExited, eventHandlerMouseReleased, eventHandlerMouseClicked, eventHandlerMouseMoved,
				eventHandlerMouseDragged);
	}

	private static GuiElement getGuiElement(Map<Integer, Map<Integer, GuiElement>> elementeSortiert, String elementId) {
		Set<Integer> keySet = elementeSortiert.keySet();
		for (Integer number1 : keySet) {
			Map<Integer, GuiElement> map = elementeSortiert.get(number1);
			Set<Integer> keys = map.keySet();
			for (Integer number2 : keys) {
				GuiElement guiElement = map.get(number2);
				if (guiElement.getId().equals(elementId)) {
					return guiElement;
				}
			}
		}
		return null;
	}

	private static void removeGuiElement(Map<Integer, Map<Integer, GuiElement>> elementeSortiert, String elementId) {
		Set<Integer> keySet = elementeSortiert.keySet();
		for (Integer number1 : keySet) {
			Map<Integer, GuiElement> map = elementeSortiert.get(number1);
			Set<Integer> keys = map.keySet();
			for (Integer number2 : keys) {
				GuiElement guiElement = map.get(number2);
				if (guiElement.getId().equals(elementId)) {
					map.remove(number2);
					return;
				}
			}
		}
	}

	private static String getClipboardText(KeyEvent event) {
		if (event.isControlDown() && event.getCode().equals(KeyCode.V)) {
			System.out.println("Strg + v gedrückt.");
			return (String) systemClipboard.getContent(DataFormat.PLAIN_TEXT);
		}
		if (event.isControlDown() && event.getCharacter().equals("v")) {
			System.out.println("Strg + v gedrückt.");
			return (String) systemClipboard.getContent(DataFormat.PLAIN_TEXT);
		}
		return null;
	}
}
