package de.platen.clapsesy.guiadapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.java_websocket.client.WebSocketClient;

public class ConnectionAdapter {

	private static final String PROTOCOL_STANDARD = "ws://";
	private static final String PROTOCOL_TLS = "wss://";
	private static final int MAX_TRIAL = 30;

	private final WebSocketClientGuiAppServer webSocketClientGuiAppServer;
	private final WebSocketClientGuiServer webSocketClientGuiServer;
	private final Map<String, String> sessionOriginalToExpanded = new HashMap<>();
	private final Map<String, String> sessionExpandedToOriginal = new HashMap<>();

	public ConnectionAdapter(String hostGuiServer, int portGuiServer, String hostGuiAppServer, int portGuiAppServer)
			throws URISyntaxException {
		String urlAppServer = PROTOCOL_STANDARD + hostGuiAppServer + ":" + String.valueOf(portGuiAppServer);
		System.out.println("URL AppServer: " + urlAppServer);
		this.webSocketClientGuiAppServer = new WebSocketClientGuiAppServer(new URI(urlAppServer), this);
		String urlGuiServer = PROTOCOL_STANDARD + hostGuiServer + ":" + String.valueOf(portGuiServer);
		System.out.println("URL GuiServer: " + urlGuiServer);
		this.webSocketClientGuiServer = new WebSocketClientGuiServer(new URI(urlGuiServer), this);
		Thread threadClientGuiServer = new Thread(new WebSocketClientThread(this.webSocketClientGuiServer, false));
		threadClientGuiServer.start();
		Thread threadClientAppGuiServer = new Thread(new WebSocketClientThread(this.webSocketClientGuiAppServer, false));
		threadClientAppGuiServer.start();
		int trial = 0;
		while (!this.webSocketClientGuiServer.isOpen()) {
			trial++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (trial > MAX_TRIAL) {
				throw new RuntimeException("Keine Verbindung zum GuiServer.");
			}
		}
		trial = 0;
		while (!this.webSocketClientGuiAppServer.isOpen()) {
			trial++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (trial > MAX_TRIAL) {
				throw new RuntimeException("Keine Verbindung zum GuiAppServer.");
			}
		}
	}

	public void sendToGuiServer(String message) {
		System.out.println("Senden zum GuiServer.");
		this.webSocketClientGuiServer.send(message);
	}

	public void sendToGuiAppServer(String message) {
		System.out.println("Senden zum GuiAppServer.");
		this.webSocketClientGuiAppServer.send(message);
	}

	private class WebSocketClientThread implements Runnable {

		private final WebSocketClient webSocketClient;
		private final boolean withTLS;

		public WebSocketClientThread(WebSocketClient webSocketClient, boolean withTLS) {
			this.webSocketClient = webSocketClient;
			this.withTLS = withTLS;
		}

		@Override
		public void run() {
			if (this.withTLS) {
				SSLContext sslContext = null;
				try {
					sslContext = SSLContext.getInstance("TLS");
					sslContext.init(null, null, null);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					e.printStackTrace();
				}
				SSLSocketFactory factory = sslContext.getSocketFactory();
				try {
					this.webSocketClient.setSocket(factory.createSocket());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.webSocketClient.connect();
		}

	}
}
