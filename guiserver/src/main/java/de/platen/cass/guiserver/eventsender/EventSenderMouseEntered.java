package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

public class EventSenderMouseEntered extends EventSenderMouse {

	public EventSenderMouseEntered(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	@Override
	public void sendEvent(String elementId, int x, int y, MouseButtonGui mouseButton, int clickCount,
			boolean isShiftDown, boolean isCtrlDown, boolean isAltDown) {
		String action = "M" + "I";
		String message = createPrefix() + elementId + TRENNER + action + TRENNER + "X" + TRENNER
				+ x + TRENNER + "Y" + TRENNER + y;
		connection.send(message);
	}

}
