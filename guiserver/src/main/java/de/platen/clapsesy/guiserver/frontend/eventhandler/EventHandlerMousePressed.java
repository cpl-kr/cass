package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMousePressed;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMousePressed extends EventHandlerMouse {

	private final EventSenderMousePressed eventSenderMousePressed;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSendButtonPrimary = false;
	private boolean hasToSendButtonSecoundary = false;
	private boolean hasToSendButtonMiddle = false;

	public EventHandlerMousePressed(GuiElement guiElement, EventSenderMousePressed eventSenderMousePressed) {
		super(guiElement);
		this.eventSenderMousePressed = eventSenderMousePressed;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMousePressed.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						MouseButton button = event.getButton();
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
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
							eventSenderMousePressed.sendEvent(guiElement.getId(), xRelative, yRelative,
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
