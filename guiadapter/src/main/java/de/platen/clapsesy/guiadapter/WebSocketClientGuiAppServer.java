package de.platen.clapsesy.guiadapter;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClientGuiAppServer extends WebSocketClient {

	private final ConnectionAdapter connectionAdapter;

	public WebSocketClientGuiAppServer(URI serverUri, ConnectionAdapter connectionAdapter) {
		super(serverUri);
		this.connectionAdapter = connectionAdapter;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("onOpen WebSocketClientGuiAppServer");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("Message von GuiAppServer: " + message);
		this.connectionAdapter.sendToGuiServer(message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("onClose WebSocketClientGuiAppServer");
	}

	@Override
	public void onError(Exception ex) {
		System.out.println("onError WebSocketClientGuiAppServer");
	}
}
