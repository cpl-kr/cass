package de.platen.clapsesy.guiserver.eventsender;

import org.java_websocket.WebSocket;

public class EventSenderKeyboard extends EventSender {

	public EventSenderKeyboard(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	public void sendEvent(String elementId, int code, boolean isShiftDown, boolean isCtrlDown, boolean isAltDown) {
		String shift = KEY_NOT_PRESSED_SHIFT;
		String ctrl = KEY_NOT_PRESSED_CTRL;
		String alt = KEY_NOT_PRESSED_ALT;
		if (isShiftDown) {
			shift = KEY_PRESSED_SHIFT;
		}
		if (isCtrlDown) {
			ctrl = KEY_PRESSED_CTRL;
		}
		if (isAltDown) {
			alt = KEY_PRESSED_ALT;
		}
		String keys = shift + TRENNER + ctrl + TRENNER + alt;
		String message = createPrefix() + elementId + TRENNER + "KC" + TRENNER + code + TRENNER + keys;
		connection.send(message);
	}
	
	public void sendEvent(String elementId, String text) {
		String message = createPrefix() + elementId + TRENNER + "KS" + TRENNER + text;
		connection.send(message);
	}
}
