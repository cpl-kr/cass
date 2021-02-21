package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderWindowClosed;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class EventHandlerWindowClosed {

    private final EventHandler<WindowEvent> eventHandler;

    public EventHandlerWindowClosed(final EventSenderWindowClosed eventSenderWindowClosed) {
        eventHandler = event -> {
            try {
                eventSenderWindowClosed.sendEvent();
            } catch (final GuiServerException e1) {
                e1.printStackTrace();
            } catch (final RuntimeException e2) {
                e2.printStackTrace();
            } catch (final Throwable e3) {
                e3.printStackTrace();
            }
        };
    }

    public EventHandler<WindowEvent> getEventHandler() {
        return eventHandler;
    }
}
