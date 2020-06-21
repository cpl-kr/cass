package de.platen.clapsesy.app.client.demo1;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import de.platen.clapsesy.app.Version;
import de.platen.clapsesy.app.event.client.SessionHolder;

public class WebSocketAppClient extends WebSocketClient {

	private final MessageHandlerClient messageHandler;
	private final SessionHolder sessionHolder;
	
	public WebSocketAppClient(URI serverUri, Draft draft, MessageHandlerClient messageHandler, SessionHolder sessionHolder) {
		super(serverUri, draft);
		this.messageHandler = messageHandler;
		this.sessionHolder = sessionHolder;
	}

	public WebSocketAppClient(URI serverURI, MessageHandlerClient messageHandler, SessionHolder sessionHolder) {
		super(serverURI);
		this.messageHandler = messageHandler;
		this.sessionHolder = sessionHolder;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("new connection opened");
		this.messageHandler.sendGetSession(this);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
		if (message.startsWith("app:" + Version.VERSION)) {
			this.messageHandler.handleMessage(message, this);
		}
		if (message.startsWith("session:" + Version.VERSION)) {
			String[] parts = message.split(":");
			if (parts.length == 3) {
				this.sessionHolder.setSession(parts[2]);
				this.messageHandler.sendInitialGui(this, this.sessionHolder.getSession());
			}
		}
	}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}
}
