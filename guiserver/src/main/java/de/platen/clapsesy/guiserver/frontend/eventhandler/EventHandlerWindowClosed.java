package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderWindowClosed;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class EventHandlerWindowClosed {

	private final EventHandler<WindowEvent> eventHandler;
	private final EventSenderWindowClosed eventSenderWindowClosed;

	public EventHandlerWindowClosed(EventSenderWindowClosed eventSenderWindowClosed) {
		this.eventSenderWindowClosed = eventSenderWindowClosed;
		this.eventHandler = new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					eventSenderWindowClosed.sendEvent();
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

	public EventHandler<WindowEvent> getEventHandler() {
		return eventHandler;
	}
}
