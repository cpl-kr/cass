package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesSelect;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementSelect extends GuiElement {

	private final ImagesSelect imagesSelect;

	private EventHandlerMouseClicked eventHandlerMouseClicked = null;

	public GuiElementSelect(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesSelect imagesSelect, State startState) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesSelect = imagesSelect;
		this.handleNewActualState(startState);
	}

	@Override
	public void handleNewActualState(State state) {
		if (state.equals(State.ACTIVE_DESELECTED) || state.equals(State.ACTIVE_SELECTED)
				|| state.equals(State.INACTIVE_DESELECTED) || state.equals(State.INACTIVE_SELECTED)) {
			actualState = state;
			this.imagesSelect.setImage(actualState);
		} else {
			throw new GuiServerException("Kein gültiger State für GuiElementSelect");
		}
	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesSelect.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
		this.eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
		eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
		eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
		eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
		this.eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
		this.imagesSelect.addEventFilter(eventHandlerMouseClicked, eventHandlerMouseEntered, eventHandlerMouseExited,
				eventHandlerMouseMoved);
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
		this.imagesSelect.setImage(eventHandlerMouse, state);
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesSelect.setImage(view, image);
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesSelect.getImageView();
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

	private static EventHandlerMouseMoved getEventHandlerMouseMoved(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				return (EventHandlerMouseMoved) eventHandlerMouse;
			}
		}
		throw new GuiServerException("Keinen passenden EventHandler gefunden.");
	}
}
