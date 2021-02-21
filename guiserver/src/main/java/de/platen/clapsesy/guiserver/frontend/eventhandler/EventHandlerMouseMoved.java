package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseMoved;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseMoved extends EventHandlerMouse {

    private final EventHandler<MouseEvent> eventHandler;
    private boolean hasToSend = false;

    public EventHandlerMouseMoved(final GuiElement guiElement, final EventSenderMouseMoved eventSenderMouseMoved) {
        super(guiElement);
        eventHandler = event -> {
            try {
                guiElement.setImage(EventHandlerMouseMoved.this, guiElement.getActualState());
                if (!guiElement.isInactive()) {
                    final int xRelative = (int) event.getSceneX() - guiElement.getX().get();
                    final int yRelative = (int) event.getSceneY() - guiElement.getY().get();
                    final boolean isShiftDown = event.isShiftDown();
                    final boolean isCtrlDown = event.isControlDown();
                    final boolean isAltDown = event.isAltDown();
                    final int clickCount = event.getClickCount();
                    if (hasToSend) {
                        eventSenderMouseMoved.sendEvent(guiElement.getElementId().get(), xRelative, yRelative, null,
                                clickCount, isShiftDown, isCtrlDown, isAltDown);
                    }
                }
            } catch (final GuiServerException e1) {
                e1.printStackTrace();
            } catch (final RuntimeException e2) {
                e2.printStackTrace();
            } catch (final Throwable e3) {
                e3.printStackTrace();
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getEventHandler() {
        return eventHandler;
    }

    public void setHasToSend(final boolean hasToSend) {
        this.hasToSend = hasToSend;
    }
}
