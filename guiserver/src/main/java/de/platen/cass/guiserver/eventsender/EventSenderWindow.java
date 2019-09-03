package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

public abstract class EventSenderWindow extends EventSender {

	public EventSenderWindow(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

}
