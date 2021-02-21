package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseReleased;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseReleased extends EventHandlerMouse {

    private final EventHandler<MouseEvent> eventHandler;
    private boolean hasToSendButtonPrimary = false;
    private boolean hasToSendButtonSecoundary = false;
    private boolean hasToSendButtonMiddle = false;

    public EventHandlerMouseReleased(final GuiElement guiElement,
            final EventSenderMouseReleased eventSenderMouseReleased) {
        super(guiElement);
        eventHandler = event -> {
            try {
                guiElement.setImage(EventHandlerMouseReleased.this, guiElement.getActualState());
                if (!guiElement.isInactive()) {
                    final int xRelative = (int) event.getSceneX() - guiElement.getX().get();
                    final int yRelative = (int) event.getSceneY() - guiElement.getY().get();
                    final boolean isShiftDown = event.isShiftDown();
                    final boolean isCtrlDown = event.isControlDown();
                    final boolean isAltDown = event.isAltDown();
                    final int clickCount = event.getClickCount();
                    final MouseButton button = event.getButton();
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
                        eventSenderMouseReleased.sendEvent(guiElement.getElementId().get(), xRelative, yRelative,
                                convertToMouseButtonGui(button), clickCount, isShiftDown, isCtrlDown, isAltDown);
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

    public void setHasToSend(final MouseButton mouseButton, final boolean hasToSend) {
        if (mouseButton == MouseButton.PRIMARY) {
            hasToSendButtonPrimary = hasToSend;
        } else if (mouseButton == MouseButton.SECONDARY) {
            hasToSendButtonSecoundary = hasToSend;
        } else if (mouseButton == MouseButton.MIDDLE) {
            hasToSendButtonMiddle = hasToSend;
        }
    }
}
