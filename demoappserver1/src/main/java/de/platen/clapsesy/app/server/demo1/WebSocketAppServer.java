package de.platen.cass.app.server.demo1;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketAppServer extends WebSocketServer {

	private static final String COMMAND_START = "init";
	private static final String COMMAND_STOP = "exit";
	private static final String COMMAND_APP = "app";
	private static final Map<String, UUID> SESSIONS = new HashMap<>();

	private final MessageHandlerServer messageHandler;

	public WebSocketAppServer(InetSocketAddress address, MessageHandlerServer messageHandler) {
		super(address);
		this.messageHandler = messageHandler;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("onOpen Demo1");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("onClose Demo1");
	}

	@Override
	public void onMessage(WebSocket webSocket, String command) {
		System.out.println("onMessage Demo1: " + command);
		String[] kommando = command.split(":");
		if (kommando[0].equals(COMMAND_START)) {
			if (kommando.length > 1) {
				UUID uuid = UUID.fromString(kommando[1]);
				SESSIONS.put(kommando[1], uuid);
				this.messageHandler.sendInitial(webSocket, uuid);
			}
		}
		if (kommando[0].equals(COMMAND_STOP)) {
			if (kommando.length > 1) {
				SESSIONS.remove(kommando[1]);
			}
		}
		if (kommando[0].equals(COMMAND_APP)) {
			if (kommando.length > 3) {
				UUID uuid = UUID.fromString(kommando[3]);
				this.messageHandler.handleMessage(command, webSocket, uuid);
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("onError Demo1: " + ex.getMessage());
	}

	@Override
	public void onStart() {
		System.out.println("onStart Demo1");
	}
}
