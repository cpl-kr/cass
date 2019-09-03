package de.platen.cass.app.event.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.cass.app.event.EventMouseButton;
import de.platen.cass.app.event.EventType;
import de.platen.cass.app.event.KeyAttribute;
import de.platen.cass.app.event.Message;
import de.platen.cass.app.event.EventMouse;
import de.platen.cass.app.event.EventHandlerKey;
import de.platen.cass.app.event.EventHandlerWindow;

public class ServerGuiElement {

	private final String windowId;
	private final String elementId;

	private final List<EventMouse> mouseEvents = new ArrayList<>();

	private EventHandlerKey clientEventHandlerKey;
	private EventHandlerWindow clientEventHandlerWindow;

	public ServerGuiElement(String windowId, String elementId) {
		this.windowId = windowId;
		this.elementId = elementId;
	}

	public void registerMouseEvent(EventMouse clientEventMouse) {
		this.mouseEvents.add(clientEventMouse);
	}

	public void unregisterMouseEvent(EventMouse clientEventMouse) {
		this.mouseEvents.remove(clientEventMouse);
	}

	public void registerKeyEvent(EventHandlerKey clientEventHandlerKey) {
		this.clientEventHandlerKey = clientEventHandlerKey;
	}

	public void unregisterKeyEvent() {
		this.clientEventHandlerKey = null;
	}

	public void registerWindowEvent(EventHandlerWindow clientEventHandlerWindow) {
		this.clientEventHandlerWindow = clientEventHandlerWindow;
	}

	public void unregisterWindowEvent() {
		this.clientEventHandlerWindow = null;
	}

	public void handleMessage(String message, WebSocket webSocket, UUID sessionId) {
		Message clientMessage = new Message(message);
		if (clientMessage.getSession().equals(sessionId.toString()) && clientMessage.getWindowId().equals(this.windowId)) {
			if (this.elementId == null) {
				if (clientMessage.getElementId() == null) {
					if (clientMessage.getClientEventType() == EventType.WINDOW_CLOSED
							&& clientMessage.isWindowClosed()) {
						if (this.clientEventHandlerWindow != null) {
							this.clientEventHandlerWindow.handleWindowClosed(webSocket, sessionId);
						}
					}
				}
			} else {
				if (this.elementId.equals(clientMessage.getElementId())) {
					if (clientMessage.getClientEventType() == EventType.KEY) {
						if (this.clientEventHandlerKey != null) {
							if (clientMessage.getKey() != null) {
								this.clientEventHandlerKey.handleChar(clientMessage.getKey(), webSocket, sessionId);
							}
							if (clientMessage.getLine() != null) {
								this.clientEventHandlerKey.handleLine(
										clientMessage.getLine().substring(0, clientMessage.getLine().length() - 1), webSocket, sessionId);
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
						EventMouse clientEventMouse = this.searchClientEventMouse(
								clientMessage.getClientEventType(), clientMessage.getMouseButton(),
								clientMessage.getClickCount(), clientMessage.getKeyAttributes());
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

	private EventMouse searchClientEventMouse(EventType clientEventType,
			EventMouseButton clientEventMouseButton, int clickCount, KeyAttribute[] clientKeyAttributes) {
		for (EventMouse clientEventMouse : this.mouseEvents) {
			if ((clientEventMouse.getEventType() == clientEventType) //
					&& (clientEventMouse.getMouseButton() == clientEventMouseButton) //
					&& (clientEventMouse.getClickCount() == clickCount) //
					&& checkKeyAttributes(clientEventMouse.getKeyAttributes(), clientKeyAttributes)) {
				return clientEventMouse;
			}
		}
		return null;
	}

	private boolean checkKeyAttributes(KeyAttribute[] clientKeyAttributes1,
			KeyAttribute[] clientKeyAttributes2) {
		Set<KeyAttribute> keyAttributes1 = new HashSet<>();
		if (clientKeyAttributes1 != null) {
			keyAttributes1 = new HashSet<KeyAttribute>(Arrays.asList(clientKeyAttributes1));
		}
		Set<KeyAttribute> keyAttributes2 = new HashSet<>();
		if (clientKeyAttributes2 != null) {
			keyAttributes2 = new HashSet<KeyAttribute>(Arrays.asList(clientKeyAttributes2));
		}
		if (keyAttributes1.size() == keyAttributes2.size()) {
			for (KeyAttribute clientKeyAttribute1 : keyAttributes1) {
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
