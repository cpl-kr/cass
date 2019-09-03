package de.platen.cass.guiserver.eventsender;

import org.java_websocket.WebSocket;

import de.platen.cass.guiserver.exception.GuiServerException;

public abstract class EventSenderMouse extends EventSender {

	protected static String MOUSE_LEFT_PRESSED = "MLP";
	protected static String MOUSE_LEFT_RELEASED = "MLR";
	protected static String MOUSE_LEFT_CLICKED = "MLC";
	protected static String MOUSE_RIGHT_PRESSED = "MRP";
	protected static String MOUSE_RIGHT_RELEASED = "MRR";
	protected static String MOUSE_RIGHT_CLICKED = "MRC";
	protected static String MOUSE_MIDDLE_PRESSED = "MMP";
	protected static String MOUSE_MIDDLE_RELEASED = "MMR";
	protected static String MOUSE_MIDDLE_CLICKED = "MMC";
	protected static String MOUSE_IN = "MI";
	protected static String MOUSE_OUT = "MO";
	protected static String CLICK_COUNT = "C";

	public EventSenderMouse(String sessionId, String windowId, WebSocket connection) {
		super(sessionId, windowId, connection);
	}

	protected String convertMouseButtonGuiToAction(MouseButtonGui mouseButtonGui) {
		switch (mouseButtonGui) {
		case PRIMARY:
			return "L";
		case SECONDARY:
			return "R";
		case MIDDLE:
			return "M";
		}
		throw new GuiServerException("Kein entsprechender Button-Typ.");
	}

	public abstract void sendEvent(String elementId, int x, int y, MouseButtonGui mouseButton, int clickCount,
			boolean isShiftDown, boolean isCtrlDown, boolean isAltDown);
}
