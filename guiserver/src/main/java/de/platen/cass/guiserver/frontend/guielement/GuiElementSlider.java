package de.platen.cass.guiserver.frontend.guielement;

import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesSlider;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementSlider extends GuiElement {

	private final ImagesSlider imagesSlider;
	private final SliderDragg sliderDraggX;
	private final SliderDragg sliderDraggY;

	private EventHandlerMouseDragged eventHandlerMouseDragged = null;
	private EventHandlerMouseReleased eventHandlerMouseReleased = null;

	public GuiElementSlider(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesSlider imagesSlider, State startState, SliderDragg sliderDraggX, SliderDragg sliderDraggY) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesSlider = imagesSlider;
		this.sliderDraggX = sliderDraggX;
		this.sliderDraggY = sliderDraggY;
		this.handleNewActualState(startState);
	}

	public void handleNewActualState(State state) {
		if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE)) {
			actualState = state;
			this.imagesSlider.setImage(actualState);
		} else {
			throw new GuiServerException("Kein gültiger State für GuiElementButton.");
		}
	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesSlider.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
		this.eventHandlerMouseDragged = getEventHandlerMouseDragged(eventHandlerMouses);
		this.eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
		eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
		eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
		this.eventHandlerMouseDragged.setHasToSend(MouseButton.PRIMARY, true);
		eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
		this.imagesSlider.addEventFilter(eventHandlerMouseDragged, eventHandlerMouseEntered, eventHandlerMouseExited,
				eventHandlerMouseReleased, eventHandlerMouseMoved);
		if (eventsToSend.contains(Event.ENTERED)) {
			eventHandlerMouseEntered.setHasToSend(true);
		}
		if (eventsToSend.contains(Event.EXITED)) {
			eventHandlerMouseExited.setHasToSend(true);
		}
		if (eventsToSend.contains(Event.MOVED)) {
			eventHandlerMouseMoved.setHasToSend(true);
		}
		eventsToSend.clear();
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
		this.imagesSlider.setImage(eventHandlerMouse, state);
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesSlider.setImage(view, image);
	}
	
	public SliderDragg getSliderDraggX() {
		return sliderDraggX;
	}

	public SliderDragg getSliderDraggY() {
		return sliderDraggY;
	}

	private static EventHandlerMouseClicked getEventHandlerMouseClicked(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				return (EventHandlerMouseClicked) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseEntered getEventHandlerMouseEntered(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				return (EventHandlerMouseEntered) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseExited getEventHandlerMouseExited(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				return (EventHandlerMouseExited) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseDragged getEventHandlerMouseDragged(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseDragged) {
				return (EventHandlerMouseDragged) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseReleased getEventHandlerMouseReleased(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
				return (EventHandlerMouseReleased) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	private static EventHandlerMouseMoved getEventHandlerMouseMoved(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				return (EventHandlerMouseMoved) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesSlider.getImageView();
	}
}
