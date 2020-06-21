package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseExited;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseExited extends EventHandlerMouse {

	private final EventSenderMouseExited eventSenderMouseExited;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSend = false;

	public EventHandlerMouseExited(GuiElement guiElement, EventSenderMouseExited eventSenderMouseExited) {
		super(guiElement);
		this.eventSenderMouseExited = eventSenderMouseExited;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMouseExited.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
						if (hasToSend) {
							eventSenderMouseExited.sendEvent(guiElement.getId(), xRelative, yRelative, null, clickCount,
									isShiftDown, isCtrlDown, isAltDown);
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

	public void setHasToSend(boolean hasToSend) {
		this.hasToSend = hasToSend;
	}
}
