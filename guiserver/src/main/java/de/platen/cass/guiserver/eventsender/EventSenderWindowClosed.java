package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

public class EventSenderWindowClosed extends EventSenderWindow {

	public EventSenderWindowClosed(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	public void sendEvent() {
		connection.send(createPrefix() + "WC");
	}
}
