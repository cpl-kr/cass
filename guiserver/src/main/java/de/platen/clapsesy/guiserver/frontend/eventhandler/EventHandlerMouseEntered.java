package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseEntered;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseEntered extends EventHandlerMouse {

	private final EventSenderMouseEntered eventSenderMouseEntered;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSend = false;

	public EventHandlerMouseEntered(GuiElement guiElement, EventSenderMouseEntered eventSenderMouseEntered) {
		super(guiElement);
		this.eventSenderMouseEntered = eventSenderMouseEntered;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMouseEntered.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
						if (hasToSend) {
							eventSenderMouseEntered.sendEvent(guiElement.getId(), xRelative, yRelative, null,
									clickCount, isShiftDown, isCtrlDown, isAltDown);
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
