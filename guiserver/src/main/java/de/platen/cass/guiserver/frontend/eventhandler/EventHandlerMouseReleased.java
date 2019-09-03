package de.platen.cass.guiserver.frontend.eventhandler;

import de.platen.cass.guiserver.eventsender.EventSenderMouseReleased;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseReleased extends EventHandlerMouse {

	private final EventSenderMouseReleased eventSenderMouseReleased;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSendButtonPrimary = false;
	private boolean hasToSendButtonSecoundary = false;
	private boolean hasToSendButtonMiddle = false;

	public EventHandlerMouseReleased(GuiElement guiElement, EventSenderMouseReleased eventSenderMouseReleased) {
		super(guiElement);
		this.eventSenderMouseReleased = eventSenderMouseReleased;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMouseReleased.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
						MouseButton button = event.getButton();
						boolean hasToSend = false;
						if (button == MouseButton.PRIMARY) {
							if (hasToSendButtonPrimary) {
								hasToSend = true;
							}
						} else if (button == MouseButton.SECONDARY) {
							if (hasToSendButtonSecoundary) {
								hasToSend = true;
							}
						} else if (button == MouseButton.MIDDLE) {
							if (hasToSendButtonMiddle) {
								hasToSend = true;
							}
						}
						if (hasToSend) {
							eventSenderMouseReleased.sendEvent(guiElement.getId(), xRelative, yRelative,
									convertToMouseButtonGui(button), clickCount, isShiftDown, isCtrlDown, isAltDown);
						}
					}
				} catch (GuiServerException e) {
					e.printStackTrace();
				} catch (RuntimeException e) {
					e.printStackTrace();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public EventHandler<MouseEvent> getEventHandler() {
		return eventHandler;
	}

	public void setHasToSend(MouseButton mouseButton, boolean hasToSend) {
		if (mouseButton == MouseButton.PRIMARY) {
            hasToSendButtonPrimary = hasToSend;
		} else if (mouseButton == MouseButton.SECONDARY) {
            hasToSendButtonSecoundary = hasToSend;
		} else if (mouseButton == MouseButton.MIDDLE) {
            hasToSendButtonMiddle = hasToSend;
		}
	}
}
