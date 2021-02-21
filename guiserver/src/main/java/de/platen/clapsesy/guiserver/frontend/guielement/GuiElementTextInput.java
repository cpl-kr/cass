package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.eventsender.EventSenderKeyboard;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesTextInput;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.start.HtmlSource;
import javafx.scene.Group;
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

    public GuiElementTextInput(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height, final ImagesTextInput imagesTextInput,
            final State startState, final HtmlRenderEngine htmlRenderEngine,
            final TextInputHtmlInput textInputHtmlInput, final int characterCount, final byte[] cursor) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        this.imagesTextInput = imagesTextInput;
        this.htmlRenderEngine = htmlRenderEngine;
        this.textInputHtmlInput = textInputHtmlInput;
        this.characterCount = characterCount;
        this.cursor = cursor;
        handleNewActualState(startState);
    }

    @Override
    public void handleNewActualState(final State state) {
        if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE) || state.equals(State.FOCUS)) {
            actualState = state;
            imagesTextInput.setImage(state);
        } else {
            throw new GuiServerException("Kein gültiger State für GuiElementTextInput.");
        }
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        imagesTextInput.setImage(view, image);

    }

    @Override
    public void addInitialFrontView(final Group group) {
        group.getChildren().add(imagesTextInput.getImageView());
    }

    @Override
    public void setInitialEventHandlerMouse(final EventHandlerMouse... eventHandlerMouses) {
        eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
        eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
        eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
        eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
        eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
        eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
        imagesTextInput.addEventFilter(eventHandlerMouseEntered, eventHandlerMouseExited, eventHandlerMouseReleased,
                eventHandlerMouseClicked, eventHandlerMouseMoved);
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
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        imagesTextInput.setImage(eventHandlerMouse, state);
    }

    @Override
    public ImageView getImageVieww() {
        return imagesTextInput.getImageView();
    }

    public synchronized void handleInput(final String character, final KeyCode keyCode, final String text,
            final EventSenderKeyboard eventSenderKeyboard) {
        if (actualState.equals(State.FOCUS)) {
            if (text.equals("")) {
                if (keyCode == KeyCode.UNDEFINED) {
                    if (character.equals("\r")) {
                        eventSenderKeyboard.sendEvent(getElementId().get(), inputText + "\r");
                    } else {
                        this.handleInput(character);
                        eventSenderKeyboard.sendEvent(getElementId().get(), character);
                    }
                } else {
                    switch (keyCode) {
                    case LEFT:
                        if (cursorPosition > 0) {
                            cursorPosition--;
                        }
                        this.handleInput("");
                        break;
                    case RIGHT:
                        if (cursorPosition < inputText.length()) {
                            cursorPosition++;
                        }
                        this.handleInput("");
                        break;
                    case BACK_SPACE:
                        handleBackspace();
                        break;
                    case DELETE:
                        handleDelete();
                        break;
                    case INSERT:
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

    private void handleInput(final String text) {
        if (text.length() > 0) {
            if (text.equals("\b")) {
                handleBackspace();
            } else {
                final int c = text.charAt(0);
                if (c != 127) {
                    if (text.matches("\\S") || text.equals(" ")) {
                        if (inputText.length() < characterCount || characterCount == 0) {
                            if (cursorPosition >= inputText.length()) {
                                inputText = inputText + text;
                            } else {
                                if (cursorPosition == 0) {
                                    inputText = text + inputText;
                                } else {
                                    inputText = inputText.substring(0, cursorPosition) + text
                                            + inputText.substring(cursorPosition);
                                }
                            }
                        }
                        cursorPosition += text.length();
                    }
                } else {
                    handleDelete();
                }
            }
        }
        createInputImageWithCursor(textInputHtmlInput.getHtmlFocus(), View.FOCUS, getWidth(), getHeight(), inputText,
                imagesTextInput, htmlRenderEngine, cursor, cursorPosition);
        createInputImage(textInputHtmlInput.getHtmlActive(), View.ACTIVE, getWidth(), getHeight(), inputText,
                imagesTextInput, htmlRenderEngine);
        createInputImage(textInputHtmlInput.getHtmlInactive(), View.INACTIVE, getWidth(), getHeight(), inputText,
                imagesTextInput, htmlRenderEngine);
        createInputImage(textInputHtmlInput.getHtmlMouseOverActive(), View.MOUSE_OVER_ACTIVE, getWidth(), getHeight(),
                inputText, imagesTextInput, htmlRenderEngine);
        createInputImageWithCursor(textInputHtmlInput.getHtmlMouseOverFocus(), View.MOUSE_OVER_FOCUS, getWidth(),
                getHeight(), inputText, imagesTextInput, htmlRenderEngine, cursor, cursorPosition);
    }

    private void handleDelete() {
        if (inputText.length() > 0) {
            if (cursorPosition < inputText.length()) {
                inputText = inputText.substring(0, cursorPosition) + inputText.substring(cursorPosition + 1);
            }
        }
    }

    private void handleBackspace() {
        if (inputText.length() == 1) {
            inputText = "";
            cursorPosition = 0;
        } else {
            if (cursorPosition > 0) {
                inputText = inputText.substring(0, cursorPosition - 1) + inputText.substring(cursorPosition);
                cursorPosition--;
            }
        }
    }

    private static void createInputImage(final HtmlInput htmlInput, final View view, final Width width,
            final Height height, final String text, final ImagesTextInput imagesTextInput,
            final HtmlRenderEngine htmlRenderEngine) {
        final String html = htmlInput.getHtmlBefore() + text + htmlInput.getHtmlAfter();
        final HtmlSource htmlSource = new HtmlSource(htmlRenderEngine, html.getBytes(), width.get(), height.get());
        final ImageCache image = new ImageCache(htmlSource);
        imagesTextInput.setImage(view, image);
    }

    private static void createInputImageWithCursor(final HtmlInput htmlInput, final View view, final Width width,
            final Height height, final String text, final ImagesTextInput imagesTextInput,
            final HtmlRenderEngine htmlRenderEngine, final byte[] cursor, final int cursorposition) {
        String content = text + new String(cursor);
        if (cursorposition == 0) {
            content = new String(cursor) + text;
        }
        if (cursorposition < text.length()) {
            content = text.substring(0, cursorposition) + new String(cursor) + text.substring(cursorposition);
        }
        final String html = htmlInput.getHtmlBefore() + content + htmlInput.getHtmlAfter();
        final HtmlSource htmlSource = new HtmlSource(htmlRenderEngine, html.getBytes(), width.get(), height.get());
        final ImageCache image = new ImageCache(htmlSource);
        imagesTextInput.setImage(view, image);
    }

    private static EventHandlerMouseClicked getEventHandlerMouseClicked(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                return (EventHandlerMouseClicked) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseEntered getEventHandlerMouseEntered(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                return (EventHandlerMouseEntered) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseExited getEventHandlerMouseExited(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                return (EventHandlerMouseExited) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseReleased getEventHandlerMouseReleased(
            final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
                return (EventHandlerMouseReleased) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseMoved getEventHandlerMouseMoved(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                return (EventHandlerMouseMoved) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }
}
