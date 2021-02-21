package de.platen.clapsesy.app.event.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.app.ElementId;
import de.platen.clapsesy.app.event.EventHandlerKey;
import de.platen.clapsesy.app.event.EventHandlerWindow;
import de.platen.clapsesy.app.event.EventMouse;
import de.platen.clapsesy.app.event.EventMouseButton;
import de.platen.clapsesy.app.event.EventType;
import de.platen.clapsesy.app.event.KeyAttribute;
import de.platen.clapsesy.app.event.Message;

public class ClientGuiElement {

    private final UUID sessionId;
    private final WebSocket webSocket;
    private final String windowId;
    private final ElementId elementId;

    private final List<EventMouse> mouseEvents = new ArrayList<>();

    private EventHandlerKey clientEventHandlerKey;
    private EventHandlerWindow clientEventHandlerWindow;

    public ClientGuiElement(final UUID sessionId, final WebSocket webSocket, final String windowId,
            final ElementId elementId) {
        this.sessionId = sessionId;
        this.windowId = windowId;
        this.elementId = elementId;
        this.webSocket = webSocket;
    }

    public void registerMouseEvent(final EventMouse clientEventMouse) {
        mouseEvents.add(clientEventMouse);
    }

    public void unregisterMouseEvent(final EventMouse clientEventMouse) {
        mouseEvents.remove(clientEventMouse);
    }

    public void registerKeyEvent(final EventHandlerKey clientEventHandlerKey) {
        this.clientEventHandlerKey = clientEventHandlerKey;
    }

    public void unregisterKeyEvent() {
        clientEventHandlerKey = null;
    }

    public void registerWindowEvent(final EventHandlerWindow clientEventHandlerWindow) {
        this.clientEventHandlerWindow = clientEventHandlerWindow;
    }

    public void unregisterWindowEvent() {
        clientEventHandlerWindow = null;
    }

    public void handleMessage(final String message) {
        final Message clientMessage = new Message(message);
        if (clientMessage.getSession().equals(sessionId.toString()) && clientMessage.getWindowId().equals(windowId)) {
            if (elementId == null) {
                if (clientMessage.getElementId() == null) {
                    if (clientMessage.getClientEventType() == EventType.WINDOW_CLOSED
                            && clientMessage.isWindowClosed()) {
                        if (clientEventHandlerWindow != null) {
                            clientEventHandlerWindow.handleWindowClosed(webSocket, sessionId);
                        }
                    }
                }
            } else {
                if (elementId.get().equals(clientMessage.getElementId())) {
                    if (clientMessage.getClientEventType() == EventType.KEY) {
                        if (clientEventHandlerKey != null) {
                            if (clientMessage.getKey() != null) {
                                clientEventHandlerKey.handleChar(clientMessage.getKey(), webSocket, sessionId);
                            }
                            if (clientMessage.getLine() != null) {
                                clientEventHandlerKey.handleLine(
                                        clientMessage.getLine().substring(0, clientMessage.getLine().length() - 1),
                                        webSocket, sessionId);
                            }
                        }
                    }
                    switch (clientMessage.getClientEventType()) {
                    case CLICK:
                    case MOVE:
                    case IN:
                    case OUT:
                    case DRAGG:
                    case PRESS:
                        final EventMouse clientEventMouse = searchClientEventMouse(clientMessage.getClientEventType(),
                                clientMessage.getMouseButton(), clientMessage.getClickCount(),
                                clientMessage.getKeyAttributes());
                        if (clientEventMouse != null) {
                            clientEventMouse.getClientEventHandlerMouse().handleEvent(clientMessage.getX(),
                                    clientMessage.getY(), webSocket, sessionId);
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
        }
    }

    private EventMouse searchClientEventMouse(final EventType clientEventType,
            final EventMouseButton clientEventMouseButton, final int clickCount,
            final KeyAttribute[] clientKeyAttributes) {
        for (final EventMouse clientEventMouse : mouseEvents) {
            if (clientEventMouse.getEventType() == clientEventType //
                    && clientEventMouse.getMouseButton() == clientEventMouseButton //
                    && clientEventMouse.getClickCount() == clickCount //
                    && checkKeyAttributes(clientEventMouse.getKeyAttributes(), clientKeyAttributes)) {
                return clientEventMouse;
            }
        }
        return null;
    }

    private boolean checkKeyAttributes(final KeyAttribute[] clientKeyAttributes1,
            final KeyAttribute[] clientKeyAttributes2) {
        Set<KeyAttribute> keyAttributes1 = new HashSet<>();
        if (clientKeyAttributes1 != null) {
            keyAttributes1 = new HashSet<KeyAttribute>(Arrays.asList(clientKeyAttributes1));
        }
        Set<KeyAttribute> keyAttributes2 = new HashSet<>();
        if (clientKeyAttributes2 != null) {
            keyAttributes2 = new HashSet<KeyAttribute>(Arrays.asList(clientKeyAttributes2));
        }
        if (keyAttributes1.size() == keyAttributes2.size()) {
            for (final KeyAttribute clientKeyAttribute1 : keyAttributes1) {
                if (!keyAttributes2.contains(clientKeyAttribute1)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
