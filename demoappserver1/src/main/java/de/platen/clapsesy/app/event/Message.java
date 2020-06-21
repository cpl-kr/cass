package de.platen.cass.app.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.platen.cass.app.Version;

public class Message {

	private static final String APP = "app";
	private static final String SESSION = "session";
	private static final String WINDOW_CLOSED = "WC";
	private static final String KEY = "KS";
	private static final String RETURN = "\r";
	private static final String MOUSE = "M";
	private static final String BUTTON_LEFT = "L";
	private static final String BUTTON_RIGHT = "R";
	private static final String BUTTON_MIDDLE = "M";
	private static final String EVENT_CLICKED = "C";
	private static final String EVENT_DRAGGED = "D";
	private static final String EVENT_PRESSED = "P";
	private static final String EVENT_ENTERED = "MI";
	private static final String EVENT_EXITED = "MO";
	private static final String EVENT_MOVED = "MM";
	private static final String CLICK_COUNT = "C";
	private static final String KEY_ATTRIBUTE_KSY = "KSY";
	private static final String KEY_ATTRIBUTE_KCY = "KCY";
	private static final String KEY_ATTRIBUTE_KAY = "KAY";
	private static final String X = "X";
	private static final String Y = "Y";

	private String session;
	private String windowId;
	private String elementId;
	private EventType clientEventType;
	private KeyAttribute[] keyAttributes;
	private String key;
	private String line;
	private boolean windowClosed;
	private EventMouseButton mouseButton;
	private int clickCount;
	private int x;
	private int y;

	public Message(String message) {
		String[] parts = message.split(":");
		if (parts.length > 5) {
			if (parts[0].equals(APP) && parts[1].equals(Version.VERSION) && parts[2].equals(SESSION)) {
				this.session = parts[3];
				this.windowId = parts[4];
				if (parts[5].equals(WINDOW_CLOSED)) {
					this.windowClosed = true;
					this.clientEventType = EventType.WINDOW_CLOSED;
				} else {
					if (parts.length > 6) {
						this.elementId = parts[5];
						if (parts[6].equals(KEY)) {
							handleKey(parts);
						} else {
							handleMouse(parts);
						}
					}
					if (parts.length > 8) {
						if (parts[7].equals(CLICK_COUNT)) {
							this.clickCount = new BigInteger(parts[8]).intValue();
						}
					}
					if (parts.length > 11) {
						handleKeyAttributes(parts);
					}
					if (parts.length > 15) {
						if (parts[12].equals(X)) {
							this.x = new BigInteger(parts[13]).intValue();
						}
						if (parts[14].equals(Y)) {
							this.y = new BigInteger(parts[15]).intValue();
						}
					}
				}
			}

		}
	}

	public String getSession() {
		return session;
	}

	public String getWindowId() {
		return windowId;
	}

	public String getElementId() {
		return elementId;
	}

	public EventType getClientEventType() {
		return clientEventType;
	}

	public KeyAttribute[] getKeyAttributes() {
		return keyAttributes;
	}

	public String getKey() {
		return key;
	}

	public String getLine() {
		return line;
	}

	public boolean isWindowClosed() {
		return windowClosed;
	}

	public EventMouseButton getMouseButton() {
		return mouseButton;
	}

	public int getClickCount() {
		return clickCount;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private void handleKey(String[] parts) {
		this.clientEventType = EventType.KEY;
		if (parts.length > 7) {
			if (parts[7].endsWith(RETURN)) {
				this.line = parts[7];
			} else {
				this.key = parts[7];
			}
		}
	}

	private void handleMouse(String[] parts) {
		if (parts[6].startsWith(MOUSE)) {
			if (parts[6].length() == 3) {
				String button = parts[6].substring(1, 2);
				switch (button) {
				case BUTTON_LEFT:
					this.mouseButton = EventMouseButton.LEFT;
					break;
				case BUTTON_RIGHT:
					this.mouseButton = EventMouseButton.RIGHT;
					break;
				case BUTTON_MIDDLE:
					this.mouseButton = EventMouseButton.MIDDLE;
					break;
				default:
					break;
				}
				String event = parts[6].substring(2, 3);
				switch (event) {
				case EVENT_CLICKED:
					this.clientEventType = EventType.CLICK;
					break;
				case EVENT_DRAGGED:
					this.clientEventType = EventType.DRAGG;
					break;
				case EVENT_PRESSED:
					this.clientEventType = EventType.PRESS;
					break;
				default:
					break;
				}
			} else {
				if (parts[6].length() == 2) {
					switch (parts[6]) {
					case EVENT_ENTERED:
						this.clientEventType = EventType.IN;
						break;
					case EVENT_EXITED:
						this.clientEventType = EventType.OUT;
						break;
					case EVENT_MOVED:
						this.clientEventType = EventType.MOVE;
						break;
					default:
						break;
					}
				}
			}
		}
	}

	private void handleKeyAttributes(String[] parts) {
		List<KeyAttribute> keyAttributes = new ArrayList<>();
		if (parts[9].equals(KEY_ATTRIBUTE_KSY)) {
			keyAttributes.add(KeyAttribute.SHIFT);
		}
		if (parts[10].equals(KEY_ATTRIBUTE_KCY)) {
			keyAttributes.add(KeyAttribute.CONTROL);
		}
		if (parts[11].equals(KEY_ATTRIBUTE_KAY)) {
			keyAttributes.add(KeyAttribute.ALT);
		}
		if (keyAttributes.size() > 0) {
			this.keyAttributes = keyAttributes.toArray(new KeyAttribute[keyAttributes.size()]);
		}
	}
}
