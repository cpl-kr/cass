package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesKlick;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementKlick extends GuiElement {

	private final ImagesKlick imagesKlick;

	private EventHandlerMouseClicked eventHandlerMouseClicked = null;
	private EventHandlerMousePressed eventHandlerMousePressed = null;
	private EventHandlerMouseReleased eventHandlerMouseReleased = null;

	public GuiElementKlick(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesKlick imagesKlick, State startState) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesKlick = imagesKlick;
		this.handleNewActualState(startState);
	}

	@Override
	public void handleNewActualState(State state) {
		if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE)) {
			actualState = state;
			this.imagesKlick.setImage(actualState);
		} else {
			throw new GuiServerException("Kein gültiger State für GuiElementKlick");
		}
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesKlick.setImage(view, image);
	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesKlick.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
		this.eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
		eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
		eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
		this.eventHandlerMousePressed = getEventHandlerMousePressed(eventHandlerMouses);
		this.eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
		eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
		this.eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
		this.imagesKlick.addEventFilter(eventHandlerMouseClicked, eventHandlerMouseEntered, eventHandlerMouseExited,
				eventHandlerMousePressed, eventHandlerMouseReleased, eventHandlerMouseMoved);
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
		this.imagesKlick.setImage(eventHandlerMouse, state);
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesKlick.getImageView();
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
