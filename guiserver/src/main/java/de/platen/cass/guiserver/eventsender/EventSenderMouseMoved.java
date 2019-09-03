package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

public class EventSenderMouseMoved extends EventSenderMouse {

	public EventSenderMouseMoved(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	@Override
	public void sendEvent(String elementId, int x, int y, MouseButtonGui mouseButton, int clickCount,
			boolean isShiftDown, boolean isCtrlDown, boolean isAltDown) {
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
		String action = "MM" + TRENNER + keys;
		String message = createPrefix() + elementId + TRENNER + action + TRENNER + "X" + TRENNER
				+ x + TRENNER + "Y" + TRENNER + y;
		connection.send(message);
	}

}
