package de.platen.cass.guiadapter;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketClientGuiServer extends WebSocketClient {

	private final ConnectionAdapter connectionAdapter;

	public WebSocketClientGuiServer(URI serverUri, Draft draft, ConnectionAdapter connectionAdapter) {
		super(serverUri, draft);
		this.connectionAdapter = connectionAdapter;
	}

	public WebSocketClientGuiServer(URI serverURI, ConnectionAdapter connectionAdapter) {
		super(serverURI);
		this.connectionAdapter = connectionAdapter;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("onOpen WebSocketClientGuiServer");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
	}

	@Override
	public void onMessage(String message) {
		System.out.println("Message von GuiServer: " + message);
		this.connectionAdapter.sendToGuiAppServer(message);
	}

	@Override
	public void onMessage(ByteBuffer message) {
	}

	@Override
	public void onError(Exception ex) {
	}
}
