package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesButton;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementButton extends GuiElement {

    private final ImagesButton imagesButton;

    private EventHandlerMousePressed eventHandlerMousePressed = null;
    private EventHandlerMouseReleased eventHandlerMouseReleased = null;
    private EventHandlerMouseClicked eventHandlerMouseClicked = null;

    public GuiElementButton(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height, final ImagesButton imagesButton,
            final State startState) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        this.imagesButton = imagesButton;
        handleNewActualState(startState);
    }

    @Override
    public void handleNewActualState(final State state) {
        if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE)) {
            actualState = state;
            imagesButton.setImage(actualState);
        } else {
            throw new GuiServerException("Kein gültiger State für GuiElementButton.");
        }
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        imagesButton.setImage(view, image);
    }

    @Override
    public void addInitialFrontView(final Group group) {
        group.getChildren().add(imagesButton.getImageView());
    }

    @Override
    public void setInitialEventHandlerMouse(final EventHandlerMouse... eventHandlerMouses) {
        eventHandlerMousePressed = getEventHandlerMousePressed(eventHandlerMouses);
        eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
        eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
        eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
        eventHandlerMouseClicked = getEventHandlerMouseClicked(eventHandlerMouses);
        eventHandlerMouseClicked.setHasToSend(MouseButton.PRIMARY, true);
        eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
        imagesButton.addEventFilter(eventHandlerMousePressed, eventHandlerMouseEntered, eventHandlerMouseExited,
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
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        imagesButton.setImage(eventHandlerMouse, state);
    }

    @Override
    public ImageView getImageVieww() {
        return imagesButton.getImageView();
    }

    private static EventHandlerMouseClicked getEventHandlerMouseClicked(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                return (EventHandlerMouseClicked) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseEntered getEventHandlerMouseEntered(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                return (EventHandlerMouseEntered) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseExited getEventHandlerMouseExited(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                return (EventHandlerMouseExited) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMousePressed getEventHandlerMousePressed(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMousePressed) {
                return (EventHandlerMousePressed) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseReleased getEventHandlerMouseReleased(
            final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
                return (EventHandlerMouseReleased) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }

    private static EventHandlerMouseMoved getEventHandlerMouseMoved(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                return (EventHandlerMouseMoved) eventHandlerMouse;
            }
        }
        throw new GuiServerException("Keinen passenden EventHandler gefunden.");
    }
}
