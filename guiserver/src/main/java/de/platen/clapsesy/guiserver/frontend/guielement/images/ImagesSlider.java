package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesSlider extends ImagesGui {

    private ImageCache imageActive;
    private ImageCache imageInactive;
    private ImageCache imageMouseOver;
    private ImageCache imageDragged;

    private enum ActualImage {
        ACTIVE, INACTIVE, MOUSOVER, DRAGGED
    }

    private ActualImage actualImage = null;

    public ImagesSlider(final ImageView imageView, final ImageCache imageActive, final ImageCache imageInactive,
            final ImageCache imageMouseOver, final ImageCache imageDragged) {
        super(imageView);
        this.imageActive = imageActive;
        this.imageInactive = imageInactive;
        this.imageMouseOver = imageMouseOver;
        this.imageDragged = imageDragged;
    }

    @Override
    public void setImage(final State state) {
        switch (state) {
        case ACTIVE:
            imageView.setImage(imageActive.getImage());
            actualImage = ActualImage.ACTIVE;
            break;
        case INACTIVE:
            imageView.setImage(imageInactive.getImage());
            actualImage = ActualImage.INACTIVE;
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        switch (state) {
        case ACTIVE:
            if (eventHandlerMouse instanceof EventHandlerMouseDragged) {
                imageView.setImage(imageDragged.getImage());
                actualImage = ActualImage.ACTIVE;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                imageView.setImage(imageMouseOver.getImage());
                actualImage = ActualImage.MOUSOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                imageView.setImage(imageActive.getImage());
                actualImage = ActualImage.ACTIVE;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
                imageView.setImage(imageActive.getImage());
                actualImage = ActualImage.ACTIVE;
            }
            break;
        case INACTIVE:
            imageView.setImage(imageInactive.getImage());
            actualImage = ActualImage.INACTIVE;
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        switch (view) {
        case ACTIVE:
            imageActive = image;
            break;
        case INACTIVE:
            imageInactive = image;
            break;
        case MOUSEOVER:
            imageMouseOver = image;
            break;
        case DRAGGED:
            imageDragged = image;
            break;
        default:
            throw new GuiServerException("Keine passende View gefunden.");
        }
        switch (actualImage) {
        case ACTIVE:
            imageView.setImage(imageActive.getImage());
            break;
        case INACTIVE:
            imageView.setImage(imageInactive.getImage());
            break;
        case MOUSOVER:
            imageView.setImage(imageMouseOver.getImage());
            break;
        case DRAGGED:
            imageView.setImage(imageDragged.getImage());
            break;
        default:
            break;
        }
    }

    public void addEventFilter(final EventHandlerMouseDragged eventHandlerMouseDragged,
            final EventHandlerMouseEntered eventHandlerMouseEntered,
            final EventHandlerMouseExited eventHandlerMouseExited,
            final EventHandlerMouseReleased eventHandlerMouseReleased,
            final EventHandlerMouseMoved eventHandlerMouseMoved) {
        imageView.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandlerMouseDragged.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleased.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
    }
}
