package de.platen.cass.app.event;

import java.util.UUID;

import org.java_websocket.WebSocket;

public interface EventHandlerWindow {

	void handleWindowClosed(WebSocket webSocket, UUID sessionId);
}
