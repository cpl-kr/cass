package de.platen.clapsesy.guiappserver;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketAppServer extends WebSocketServer {

	private static final String APP = "app";
	private static final String NAME = "name";
	private static final String SESSION = "session";
	private static final String INIT = "init:";
	private static final String TRENNER = ":";

	private final Map<String, AppConnection> appConnections;
	private final Map<WebSocket, Map<String, AppConnection>> clients = new HashMap<>();
	private final Map<String, WebSocket> clientsessions;

	public WebSocketAppServer(InetSocketAddress address, Map<String, AppConnection> appConnections, Map<String, WebSocket> clientsessions) {
		super(address);
		this.appConnections = appConnections;
		this.clientsessions = clientsessions;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("onOpen WebSocketAppServer");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("onClose WebSocketAppServer");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("onMessage WebSocketAppServer: " + message);
		String[] parts = message.split(":");
		if (parts.length > 6 && APP.equals(parts[0]) && Version.VERSION.equals(parts[1])) {
			if (NAME.equals(parts[2])) {
				String appName = parts[3];
				if (this.appConnections.containsKey(appName)) {
					String sessionId = parts[6];
					AppConnection connection = this.appConnections.get(appName);
					if (!this.clients.containsKey(conn)) {
						Map<String, AppConnection> sessions = new HashMap<>();
						sessions.put(sessionId, connection);
						this.clients.put(conn, sessions);
					} else {
						Map<String, AppConnection> sessions = this.clients.get(conn);
						sessions.put(sessionId, connection);
					}
					this.clientsessions.put(sessionId, conn);
					connection.sendToAppServer(INIT + sessionId + TRENNER + parts[4] + TRENNER + parts[5]);
				}
			}
			if (SESSION.equals(parts[2])) {
				String sessionId = parts[3];
				Map<String, AppConnection> sessions = this.clients.get(conn);
				if (sessions != null) {
					AppConnection connection = sessions.get(sessionId);
					if (connection != null) {
						connection.sendToAppServer(message);
					}
				}
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("onError WebSocketAppServer: " + ex.getMessage());
	}

	@Override
	public void onStart() {
		System.out.println("onStart WebSocketAppServer");
	}
}
