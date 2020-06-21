package de.platen.clapsesy.guiadapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocket extends WebSocketClient {

	private static final int DEFAULT_SERVER_PORT = 77;
	private static final String GET_RESOLUTION = "get:" + Version.VERSION + ":resolution";
	private static final String GET_SESSION = "get:" + Version.VERSION + ":session";

	private int width;
	private int height;
	private String session;

	private final String hostGuiServer;
	private final int portGuiServer;
	private final Set<String> messages = new HashSet<>();

	public WebSocket(URI serverUri, Draft draft, String hostGuiServer, int portGuiServer) {
		super(serverUri, draft);
		this.hostGuiServer = hostGuiServer;
		this.portGuiServer = portGuiServer;
	}

	public WebSocket(URI serverURI, String host, int port) {
		super(serverURI);
		this.hostGuiServer = host;
		this.portGuiServer = port;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("new connection opened");
		this.send(GET_RESOLUTION);
		this.send(GET_SESSION);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
		if (message.endsWith("\r")) {
			this.messages.add(message);
			this.send(GET_SESSION);
		} else {
			if (message.contains(":window1:WC")) {
				byte[] xmlDaten = insertSession(ladeGUIAusDatei("Exit.xml"), this.session);
				this.sendData(xmlDaten);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				close();
				System.exit(0);
			} else {
				if (message.startsWith("info:" + Version.VERSION + ":resolution:")) {
					String[] parts = message.split(":");
					if (parts.length > 6) {
						this.width = Integer.valueOf(parts[4]);
						this.height = Integer.valueOf(parts[6]);
						System.out.println("Auflösung: Breite " + this.width + ", Höhe " + this.height);
					}
				} else {
					if (message.startsWith("session:" + Version.VERSION)) {
						String[] parts = message.split(":");
						if (parts.length > 2) {
							if (this.messages.size() == 0) {
								this.session = parts[2];
								this.sendInitial();
							} else {
								String msg = this.messages.iterator().next();
								this.messages.remove(msg);
								this.handleMessage(msg, parts[2]);
							}
						}
					}
				}
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

	private void sendInitial() {
		byte[] xmlDaten = insertSession(ladeGUIAusDatei("InitialGUI.xml"), this.session);
		this.sendData(xmlDaten);
	}

	private byte[] ladeGUIAusDatei(String xmlDateiname) {
		byte[] xmlDaten = new byte[0];
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlDateiname);
		try {
			xmlDaten = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlDaten;
	}

	private void sendData(byte[] xmlDaten) {
		String data = new String(xmlDaten);
		this.send(data);
	}

	private byte[] insertSession(byte[] xmlData, String session) {
		String data = new String(xmlData);
		String[] parts = data.split("<SessionId>");
		if (parts.length == 2) {
			String dataToSend = parts[0] + "<SessionId>" + session + parts[1];
			return dataToSend.getBytes();
		}
		return new byte[0];
	}

	private void handleMessage(String message, String session) {
		String[] messageParts = message.split(":");
		if (messageParts.length > 7) {
			if (messageParts[1].equals(Version.VERSION)) {
				String host = messageParts[8];
				int port = DEFAULT_SERVER_PORT;
				int nameIndex = 9;
				if (messageParts.length > 10) {
					port = Integer.valueOf(messageParts[9]);
					nameIndex = 10;
				}
				String appName = messageParts[nameIndex].substring(0, messageParts[nameIndex].length() - 1);
				try {
					ConnectionAdapter adapter = new ConnectionAdapter(this.hostGuiServer, this.portGuiServer, host,
							port);
					String messageToSend = "app:" + Version.VERSION + ":" + "name:" + appName + ":" + String.valueOf(width)
							+ ":" + String.valueOf(height) + ":" + session;
					adapter.sendToGuiAppServer(messageToSend);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
