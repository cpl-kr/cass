package de.platen.clapsesy.guiappserver;

import java.net.URI;
import java.util.Map;

import org.java_websocket.WebSocket;

public class AppConnection {

	private final WebSocketClientApp webSocketClientApp;

	private final Thread thread;

	public AppConnection(URI uri, Map<String, WebSocket> clientsessions) {
		this.webSocketClientApp = new WebSocketClientApp(uri, clientsessions);
		this.thread = new Thread(new ServerConnection(this.webSocketClientApp));
	}

	public void open() {
		thread.start();
	}

	public void sendToAppServer(String message) {
		this.webSocketClientApp.send(message);
	}

	public void close() {
		this.webSocketClientApp.close();
	}

	private class ServerConnection implements Runnable {

		private final WebSocketClientApp webSocketClientGuiAppServer;

		public ServerConnection(WebSocketClientApp webSocketClientGuiAppAerver) {
			this.webSocketClientGuiAppServer = webSocketClientGuiAppAerver;
		}

		@Override
		public void run() {
			this.webSocketClientGuiAppServer.connect();
		}
	}
}
