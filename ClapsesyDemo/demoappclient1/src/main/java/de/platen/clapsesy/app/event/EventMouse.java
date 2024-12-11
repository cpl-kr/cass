package de.platen.clapsesy.app.event;

public class EventMouse {

	private final EventHandlerMouse clientEventHandlerMouse;
	private final EventType eventType;
	private final EventMouseButton mouseButton;
	private final int clickCount;
	private final KeyAttribute[] keyAttributes;

	public EventMouse(EventHandlerMouse clientEventHandlerMouse, EventType eventType,
			EventMouseButton mouseButton, int clickCount, KeyAttribute... keyAttributes) {
		this.clientEventHandlerMouse = clientEventHandlerMouse;
		this.eventType = eventType;
		this.mouseButton = mouseButton;
		this.clickCount = clickCount;
		this.keyAttributes = keyAttributes;
	}

	public EventHandlerMouse getClientEventHandlerMouse() {
		return clientEventHandlerMouse;
	}

	public EventType getEventType() {
		return eventType;
	}

	public EventMouseButton getMouseButton() {
		return mouseButton;
	}

	public int getClickCount() {
		return clickCount;
	}

	public KeyAttribute[] getKeyAttributes() {
		return keyAttributes;
	}
}
