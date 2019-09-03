package de.platen.cass.guiserver.frontend.guielement;

import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesButton;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementButton extends GuiElement {

	private final ImagesButton imagesButton;

	private EventHandlerMousePressed eventHandlerMousePressed = null;
	private EventHandlerMouseReleased eventHandlerMouseReleased = null;
	private EventHandlerMouseClicked eventHandlerMouseClicked = null;

	public GuiElementButton(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesButton imagesButton, State startState) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesButton = imagesButton;
		this.handleNewActualState(startState);
	}

	public void handleNewActualState(State state) {
		if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE)) {
			actualState = state;
			this.imagesButton.setImage(actualState);
		} else {
			throw new GuiServerException("Kein gültiger State für GuiElementButton.");
		}
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesButton.setImage(view, image);
	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesButton.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
		this.eventHandlerMousePressed = getEventHandlerMousePressed(eventHandlerMouses);
		this.eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
		eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
		eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
		this.eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
		this.eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
		eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
		this.imagesButton.addEventFilter(eventHandlerMousePressed, eventHandlerMouseEntered, eventHandlerMouseExited,
				eventHandlerMouseReleased, eventHandlerMouseClicked, eventHandlerMouseMoved);
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
		this.imagesButton.setImage(eventHandlerMouse, state);
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesButton.getImageView();
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

	private static EventHandlerMousePressed getEventHandlerMousePressed(EventHandlerMouse... eventHandlerMouses) {
		for (EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
			if (eventHandlerMouse instanceof EventHandlerMousePressed) {
				return (EventHandlerMousePressed) eventHandlerMouse;
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
}
