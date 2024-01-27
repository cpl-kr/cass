package de.platen.clapsesy.guiappserver;

import java.net.URI;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClientApp extends WebSocketClient {

	private final Map<String, WebSocket> clientsessions;

	public WebSocketClientApp(URI serverUri, Map<String, WebSocket> clientsessions) {
		super(serverUri);
		this.clientsessions = clientsessions;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("onStart WebSocketClientApp");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("onMessage WebSocketClientApp");
		String sessionId = this.extractSession(message);
		System.out.println("SessionId: " + sessionId);
		if (!sessionId.isEmpty()) {
			WebSocket webSocket = this.clientsessions.get(sessionId);
			if (webSocket != null) {
				System.out.println("Senden zum GuiAdapter");
				webSocket.send(message);
			}
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("onClose WebSocketClientApp: " + code + ", " + reason);
	}

	@Override
	public void onError(Exception ex) {
		System.out.println("onError WebSocketClientApp: " + ex.getMessage());
	}

	private String extractSession(String message) {
		String[] parts = message.split("<SessionId>");
		if (parts.length == 2) {
			String[] subParts = parts[1].split("</SessionId>");
			if (subParts.length == 2) {
				return subParts[0];
			}
		}
		return "";
	}
}
