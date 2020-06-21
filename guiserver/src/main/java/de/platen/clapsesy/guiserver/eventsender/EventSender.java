package de.platen.clapsesy.guiserver.eventsender;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.guiserver.Version;

public abstract class EventSender {

	protected static String TRENNER = ":";
	protected static String KEY_PRESSED_SHIFT = "KSY";
	protected static String KEY_NOT_PRESSED_SHIFT = "KSN";
	protected static String KEY_PRESSED_CTRL = "KCY";
	protected static String KEY_NOT_PRESSED_CTRL = "KCN";
	protected static String KEY_PRESSED_ALT = "KAY";
	protected static String KEY_NOT_PRESSED_ALT = "KAN";

	protected final String sessionId;
	protected final String windowId;
	protected final WebSocket connection;

	public EventSender(String sessionId, String windowId, WebSocket connection) {
		this.sessionId = sessionId;
		this.windowId = windowId;
		this.connection = connection;
	}
	
	protected String createPrefix() {
		return "app:" + Version.VERSION + ":session:" + sessionId + TRENNER + windowId + TRENNER;
	}
}
