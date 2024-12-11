package de.platen.clapsesy.app.client.demo2;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import de.platen.clapsesy.app.Version;

public class WebSocket extends WebSocketClient {

	private final String path;

	private DemoApp2 demoApp2;

	public WebSocket(String path, URI serverUri, Draft draft) {
		super(serverUri, draft);
		this.path = path;
	}

	public WebSocket(String path, URI serverURI) {
		super(serverURI);
		this.path = path;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("new connection opened");
		String getSession = "get:" + Version.VERSION + ":session";
		send(getSession);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
		if (message.startsWith("session:" + Version.VERSION)) {
			String[] parts = message.split(":");
			if (parts.length == 3) {
				this.demoApp2 = new DemoApp2(path, this, parts[2]);
				this.demoApp2.start();
			}
		}
		if (message.equals("app:1.0.0:session:session2:Zeitangabe:WC")) {
			this.demoApp2.stop();
			this.demoApp2.exit();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			close();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
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
