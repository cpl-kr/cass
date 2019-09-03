package de.platen.cass.app.event;

import java.util.UUID;

import org.java_websocket.WebSocket;

public interface EventHandlerKey {

	public void handleChar(String c, WebSocket webSocket, UUID sessionId);
	
	public void handleLine(String line, WebSocket webSocket, UUID sessionId);
}
