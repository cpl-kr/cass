package de.platen.cass.app.event;

import java.util.UUID;

import org.java_websocket.WebSocket;

public interface EventHandlerMouse {

	void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId);
}
