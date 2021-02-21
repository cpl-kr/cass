package de.platen.clapsesy.guiserver.frontend;

import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.guiserver.eventsender.EventSenderKeyboard;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseClicked;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseDragged;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseEntered;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseExited;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseMoved;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMousePressed;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseReleased;
import de.platen.clapsesy.guiserver.eventsender.EventSenderWindowClosed;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerWindowClosed;
import de.platen.clapsesy.guiserver.frontend.guielement.ElementPosition;
import de.platen.clapsesy.guiserver.frontend.guielement.Event;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementTextInput;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationWindow {

    private static final Clipboard systemClipboard = Clipboard.getSystemClipboard();

    private final String windowId;
    private final GuiElementVerwaltungOperation guiElementVerwaltungOperation;
    private final UUID sessionId;
    private final WebSocket connection;
    private final Stage window;
    private final Group group;
    private final EventSenderKeyboard eventSenderKeyboard;

    private GuiElementTextInput focusElement = null;

    public ApplicationWindow(final String windowId, final int x, final int y, final int width, final int height,
            final String titel, final GuiElementVerwaltungOperation guiElementVerwaltungOperation, final UUID sessionId,
            final WebSocket connection) {
        this.windowId = windowId;
        this.guiElementVerwaltungOperation = guiElementVerwaltungOperation;
        this.sessionId = sessionId;
        this.connection = connection;
        eventSenderKeyboard = new EventSenderKeyboard(sessionId.toString(), windowId, connection);
        Scene scene = null;
        window = new Stage();
        group = new Group();
        guiElementVerwaltungOperation.addInitialFrontView(new Verbindung(group));
        guiElementVerwaltungOperation.addInitialEventHandler(this.sessionId, this.windowId, this.connection);
        scene = new Scene(group, width, height);
        scene.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (focusElement != null) {
                try {
                    final String clipboardText = getClipboardText(event);
                    if (clipboardText == null) {
                        // System.out.println("Texteingabe Key Typed");
                        focusElement.handleInput(event.getCharacter(), event.getCode(), event.getText(),
                                eventSenderKeyboard);
                    } else {
                        // System.out.println("Text aus Zwischenablage bei Key Typed: " + clipboardText);
                        focusElement.handleInput(clipboardText, event.getCode(), clipboardText, eventSenderKeyboard);
                    }
                } catch (final GuiServerException e1) {
                    e1.printStackTrace();
                } catch (final RuntimeException e2) {
                    e2.printStackTrace();
                } catch (final Throwable e3) {
                    e3.printStackTrace();
                }
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (focusElement != null) {
                try {
                    final String clipboardText = getClipboardText(event);
                    if (clipboardText == null) {
                        // System.out.println("Texteingabe Key Pressed");
                        focusElement.handleInput(event.getCharacter(), event.getCode(), event.getText(),
                                eventSenderKeyboard);
                    } else {
                        // System.out.println("Text aus Zwischenablage bei Key Pressed: " + clipboardText);
                        focusElement.handleInput(clipboardText, event.getCode(), clipboardText, eventSenderKeyboard);
                    }
                } catch (final GuiServerException e1) {
                    e1.printStackTrace();
                } catch (final RuntimeException e2) {
                    e2.printStackTrace();
                } catch (final Throwable e3) {
                    e3.printStackTrace();
                }
            }
        });
        window.setScene(scene);
        window.sizeToScene();
        window.setX(x);
        window.setY(y);
        window.setTitle(titel);
        window.setResizable(false);
        window.initStyle(StageStyle.UTILITY);
        final EventSenderWindowClosed eventSenderWindowClosed = new EventSenderWindowClosed(this.sessionId.toString(),
                windowId, connection);
        final EventHandlerWindowClosed eventHandlerWindowClosed = new EventHandlerWindowClosed(eventSenderWindowClosed);
        window.setOnCloseRequest(eventHandlerWindowClosed.getEventHandler());
    }

    public void show() {
        if (window != null) {
            window.show();
        }
    }

    public void close() {
        if (window != null) {
            window.close();
        }
    }

    public void changeElementState(final ElementId elementId, final State state) {
        if (elementId != null && state != null) {
            final GuiElement guiElement = guiElementVerwaltungOperation.getGuiElement(elementId);
            if (guiElement != null) {
                try {
                    guiElement.handleNewActualState(state);
                    if (guiElement instanceof GuiElementTextInput) {
                        if (state.equals(State.FOCUS)) {
                            focusElement = (GuiElementTextInput) guiElement;
                        } else {
                            if (focusElement.equals(guiElement)) {
                                focusElement = null;
                            }
                        }
                    }
                } catch (final GuiServerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setElementImage(final ElementId elementId, final View view, final ImageCache image) {
        if (elementId != null && view != null && image != null) {
            final GuiElement guiElement = guiElementVerwaltungOperation.getGuiElement(elementId);
            if (guiElement != null) {
                try {
                    guiElement.setImage(view, image);
                } catch (final GuiServerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addElement(final GuiElement guiElement) {
        if (guiElement != null) {
            guiElement.addInitialFrontView(group);
            addInitialEventHandler(guiElement, sessionId, windowId, connection);
            if (guiElement instanceof GuiElementTextInput) {
                if (guiElement.getActualState() == State.FOCUS) {
                    focusElement = (GuiElementTextInput) guiElement;
                }
            }
            guiElementVerwaltungOperation.addGuiElement(guiElement);
        }
    }

    public void removeElement(final ElementId elementId) {
        if (elementId != null) {
            final GuiElement guiElement = guiElementVerwaltungOperation.getGuiElement(elementId);
            if (guiElement != null) {
                if (focusElement != null) {
                    if (guiElement.getElementId() == focusElement.getElementId()) {
                        focusElement = null;
                    }
                }
                if (group != null) {
                    try {
                        group.getChildren().remove(guiElement.getImageVieww());
                        guiElementVerwaltungOperation.removeGuiElement(elementId);
                        window.hide();
                        window.show();
                    } catch (final RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setEvent(final ElementId elementId, final Event event, final boolean isActive) {
        if (elementId != null) {
            final GuiElement guiElement = guiElementVerwaltungOperation.getGuiElement(elementId);
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
        return windowId;
    }

    public ElementPosition getElementPosition(final ElementId elementId) {
        final GuiElement guiElement = guiElementVerwaltungOperation.getGuiElement(elementId);
        if (guiElement != null) {
            return new ElementPosition(guiElement.getX(), guiElement.getY(), guiElement.getWidth(),
                    guiElement.getHeight());
        }
        return null;
    }

    private static void addInitialEventHandler(final GuiElement guiElement, final UUID sessionId, final String windowId,
            final WebSocket connection) {
        final EventSenderMouseClicked eventSenderMouseClicked = new EventSenderMouseClicked(sessionId.toString(),
                windowId, connection);
        final EventSenderMousePressed eventSenderMousePressed = new EventSenderMousePressed(sessionId.toString(),
                windowId, connection);
        final EventSenderMouseReleased eventSenderMouseReleased = new EventSenderMouseReleased(sessionId.toString(),
                windowId, connection);
        final EventSenderMouseEntered eventSenderMouseEntered = new EventSenderMouseEntered(sessionId.toString(),
                windowId, connection);
        final EventSenderMouseExited eventSenderMouseExited = new EventSenderMouseExited(sessionId.toString(), windowId,
                connection);
        final EventSenderMouseMoved eventSenderMouseMoved = new EventSenderMouseMoved(sessionId.toString(), windowId,
                connection);
        final EventSenderMouseDragged eventSenderMouseDragged = new EventSenderMouseDragged(sessionId.toString(),
                windowId, connection);
        final EventHandlerMouseClicked eventHandlerMouseClicked = new EventHandlerMouseClicked(guiElement,
                eventSenderMouseClicked);
        final EventHandlerMousePressed eventHandlerMousePressed = new EventHandlerMousePressed(guiElement,
                eventSenderMousePressed);
        final EventHandlerMouseReleased eventHandlerMouseReleased = new EventHandlerMouseReleased(guiElement,
                eventSenderMouseReleased);
        final EventHandlerMouseEntered eventHandlerMouseEntered = new EventHandlerMouseEntered(guiElement,
                eventSenderMouseEntered);
        final EventHandlerMouseExited eventHandlerMouseExited = new EventHandlerMouseExited(guiElement,
                eventSenderMouseExited);
        final EventHandlerMouseMoved eventHandlerMouseMoved = new EventHandlerMouseMoved(guiElement,
                eventSenderMouseMoved);
        final EventHandlerMouseDragged eventHandlerMouseDragged = new EventHandlerMouseDragged(guiElement,
                eventSenderMouseDragged);
        guiElement.setInitialEventHandlerMouse(eventHandlerMousePressed, eventHandlerMouseEntered,
                eventHandlerMouseExited, eventHandlerMouseReleased, eventHandlerMouseClicked, eventHandlerMouseMoved,
                eventHandlerMouseDragged);
    }

    private static String getClipboardText(final KeyEvent event) {
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
