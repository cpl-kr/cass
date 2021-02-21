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
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesSlider;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class GuiElementSlider extends GuiElement {

    private final ImagesSlider imagesSlider;
    private final SliderDragg sliderDraggX;
    private final SliderDragg sliderDraggY;

    private EventHandlerMouseDragged eventHandlerMouseDragged = null;
    private EventHandlerMouseReleased eventHandlerMouseReleased = null;

    public GuiElementSlider(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height, final ImagesSlider imagesSlider,
            final State startState, final SliderDragg sliderDraggX, final SliderDragg sliderDraggY) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        this.imagesSlider = imagesSlider;
        this.sliderDraggX = sliderDraggX;
        this.sliderDraggY = sliderDraggY;
        handleNewActualState(startState);
    }

    @Override
    public void handleNewActualState(final State state) {
        if (state.equals(State.ACTIVE) || state.equals(State.INACTIVE)) {
            actualState = state;
            imagesSlider.setImage(actualState);
        } else {
            throw new GuiServerException("Kein gültiger State für GuiElementButton.");
        }
    }

    @Override
    public void addInitialFrontView(final Group group) {
        group.getChildren().add(imagesSlider.getImageView());
    }

    @Override
    public void setInitialEventHandlerMouse(final EventHandlerMouse... eventHandlerMouses) {
        eventHandlerMouseDragged = getEventHandlerMouseDragged(eventHandlerMouses);
        eventHandlerMouseReleased = getEventHandlerMouseReleased(eventHandlerMouses);
        eventHandlerMouseEntered = getEventHandlerMouseEntered(eventHandlerMouses);
        eventHandlerMouseExited = getEventHandlerMouseExited(eventHandlerMouses);
        eventHandlerMouseDragged.setHasToSend(MouseButton.PRIMARY, true);
        eventHandlerMouseMoved = getEventHandlerMouseMoved(eventHandlerMouses);
        imagesSlider.addEventFilter(eventHandlerMouseDragged, eventHandlerMouseEntered, eventHandlerMouseExited,
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
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        imagesSlider.setImage(eventHandlerMouse, state);
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        imagesSlider.setImage(view, image);
    }

    public SliderDragg getSliderDraggX() {
        return sliderDraggX;
    }

    public SliderDragg getSliderDraggY() {
        return sliderDraggY;
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

    private static EventHandlerMouseDragged getEventHandlerMouseDragged(final EventHandlerMouse... eventHandlerMouses) {
        for (final EventHandlerMouse eventHandlerMouse : eventHandlerMouses) {
            if (eventHandlerMouse instanceof EventHandlerMouseDragged) {
                return (EventHandlerMouseDragged) eventHandlerMouse;
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

    @Override
    public ImageView getImageVieww() {
        return imagesSlider.getImageView();
    }
}
