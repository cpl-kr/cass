package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

public class EventSenderMouseReleased extends EventSenderMouse {

	public EventSenderMouseReleased(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	@Override
	public void sendEvent(String elementId, int x, int y, MouseButtonGui mouseButton, int clickCount,
			boolean isShiftDown, boolean isCtrlDown, boolean isAltDown) {
		String button = convertMouseButtonGuiToAction(mouseButton);
		String action = "M" + button + "R";
		String message = createPrefix() + elementId + TRENNER + action + TRENNER + "X" + TRENNER + x + TRENNER
				+ "Y" + TRENNER + y;
		connection.send(message);
	}

}
