package de.platen.cass.guiserver.frontend.eventhandler;

import de.platen.cass.guiserver.eventsender.EventSenderMouseMoved;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseMoved extends EventHandlerMouse {

	private final EventSenderMouseMoved eventSenderMouseMoved;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSend = false;

	public EventHandlerMouseMoved(GuiElement guiElement, EventSenderMouseMoved eventSenderMouseMoved) {
		super(guiElement);
		this.eventSenderMouseMoved = eventSenderMouseMoved;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMouseMoved.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
						if (hasToSend) {
							eventSenderMouseMoved.sendEvent(guiElement.getId(), xRelative, yRelative, null, clickCount,
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
