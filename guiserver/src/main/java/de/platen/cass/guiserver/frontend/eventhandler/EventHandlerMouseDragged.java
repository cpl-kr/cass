package de.platen.cass.guiserver.frontend.eventhandler;

import de.platen.cass.guiserver.eventsender.EventSenderMouseDragged;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.guielement.GuiElement;
import de.platen.cass.guiserver.frontend.guielement.GuiElementSlider;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventHandlerMouseDragged extends EventHandlerMouse {

	private final EventSenderMouseDragged eventSenderMouseDragged;
	private final EventHandler<MouseEvent> eventHandler;
	private boolean hasToSendButtonPrimary = false;
	private boolean hasToSendButtonSecoundary = false;
	private boolean hasToSendButtonMiddle = false;
	private Integer sceneXStart = null;
	private Integer sceneYStart = null;
	private Integer sceneX = null;
	private Integer sceneY = null;

	public EventHandlerMouseDragged(GuiElement guiElement, EventSenderMouseDragged eventSenderMouseDragged) {
		super(guiElement);
		this.eventSenderMouseDragged = eventSenderMouseDragged;
		this.eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					guiElement.setImage(EventHandlerMouseDragged.this, guiElement.getActualState());
					if (!guiElement.isInactive()) {
						int xRelative = (int) event.getSceneX() - guiElement.getX();
						int yRelative = (int) event.getSceneY() - guiElement.getY();
						boolean isShiftDown = event.isShiftDown();
						boolean isCtrlDown = event.isControlDown();
						boolean isAltDown = event.isAltDown();
						int clickCount = event.getClickCount();
						boolean hasToSend = false;
						MouseButton button = event.getButton();
						GuiElementSlider guiElementSlider = (GuiElementSlider) guiElement;
						if (button == MouseButton.PRIMARY) {
							int differenceX = 0;
							if (sceneX == null) {
								sceneX = new Integer((int) (event.getSceneX()));
								sceneXStart = new Integer((int) (event.getSceneX()));
							}
							differenceX = (int) (event.getSceneX()) - sceneX;
							sceneX = (int) (event.getSceneX());
							double positionX = guiElement.getImageVieww().getX() + differenceX;
							if (guiElementSlider.getSliderDraggX() != null) {
								int xLeft = guiElement.getX() - guiElementSlider.getSliderDraggX().getRangeMinus();
								int xRight = guiElement.getX() + guiElementSlider.getSliderDraggX().getRangePlus();
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
								sceneY = new Integer((int) (event.getSceneY()));
								sceneYStart = new Integer((int) (event.getSceneY()));
							}
							differenceY = (int) (event.getSceneY()) - sceneY;
							sceneY = (int) (event.getSceneY());
							double positionY = guiElement.getImageVieww().getY() + differenceY;
							if (guiElementSlider.getSliderDraggY() != null) {
								int yLeft = guiElement.getY() - guiElementSlider.getSliderDraggY().getRangeMinus();
								int yRight = guiElement.getY() + guiElementSlider.getSliderDraggY().getRangePlus();
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
							eventSenderMouseDragged.sendEvent(guiElement.getId(), xRelative, yRelative,
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
