package de.platen.clapsesy.app.event.client;

import org.java_websocket.WebSocket;

public class WebSocketHolder {

	private WebSocket webSocket;

	public WebSocket getWebSocket() {
		return webSocket;
	}

	public void setWebSocket(WebSocket webSocket) {
		this.webSocket = webSocket;
	}
}
