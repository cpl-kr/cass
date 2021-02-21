package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseDragged;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementSlider;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseDragged extends EventHandlerMouse {

    private final EventHandler<MouseEvent> eventHandler;
    private boolean hasToSendButtonPrimary = false;
    private boolean hasToSendButtonSecoundary = false;
    private boolean hasToSendButtonMiddle = false;
    private Integer sceneX = null;
    private Integer sceneY = null;

    public EventHandlerMouseDragged(final GuiElement guiElement,
            final EventSenderMouseDragged eventSenderMouseDragged) {
        super(guiElement);
        eventHandler = event -> {
            try {
                guiElement.setImage(EventHandlerMouseDragged.this, guiElement.getActualState());
                if (!guiElement.isInactive()) {
                    final int xRelative = (int) event.getSceneX() - guiElement.getX().get();
                    final int yRelative = (int) event.getSceneY() - guiElement.getY().get();
                    final boolean isShiftDown = event.isShiftDown();
                    final boolean isCtrlDown = event.isControlDown();
                    final boolean isAltDown = event.isAltDown();
                    final int clickCount = event.getClickCount();
                    boolean hasToSend = false;
                    final MouseButton button = event.getButton();
                    final GuiElementSlider guiElementSlider = (GuiElementSlider) guiElement;
                    if (button == MouseButton.PRIMARY) {
                        int differenceX = 0;
                        if (sceneX == null) {
                            sceneX = Integer.valueOf((int) event.getSceneX());
                        }
                        differenceX = (int) event.getSceneX() - sceneX;
                        sceneX = (int) event.getSceneX();
                        final double positionX = guiElement.getImageVieww().getX() + differenceX;
                        if (guiElementSlider.getSliderDraggX() != null) {
                            final int xLeft = guiElement.getX().get()
                                    - guiElementSlider.getSliderDraggX().getRangeMinus();
                            final int xRight = guiElement.getX().get()
                                    + guiElementSlider.getSliderDraggX().getRangePlus();
                            if ((int) positionX >= xLeft) {
                                if ((int) positionX <= xRight) {
                                    guiElement.getImageVieww().setX(positionX);
                                    if (hasToSendButtonPrimary) {
                                        hasToSend = true;
                                    }
                                }
                            }
                        }
                        int differenceY = 0;
                        if (sceneY == null) {
                            sceneY = Integer.valueOf((int) event.getSceneY());
                        }
                        differenceY = (int) event.getSceneY() - sceneY;
                        sceneY = (int) event.getSceneY();
                        final double positionY = guiElement.getImageVieww().getY() + differenceY;
                        if (guiElementSlider.getSliderDraggY() != null) {
                            final int yLeft = guiElement.getY().get()
                                    - guiElementSlider.getSliderDraggY().getRangeMinus();
                            final int yRight = guiElement.getY().get()
                                    + guiElementSlider.getSliderDraggY().getRangePlus();
                            if ((int) positionY >= yLeft) {
                                if ((int) positionY <= yRight) {
                                    guiElement.getImageVieww().setY(positionY);
                                    if (hasToSendButtonPrimary) {
                                        hasToSend = true;
                                    }
                                }
                            }
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
                        eventSenderMouseDragged.sendEvent(guiElement.getElementId().get(), xRelative, yRelative,
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
