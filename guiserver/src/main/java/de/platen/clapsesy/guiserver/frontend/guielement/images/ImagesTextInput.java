package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesTextInput extends ImagesGui {

    private ImageCache imageActive;
    private ImageCache imageInactive;
    private ImageCache imageFocus;
    private ImageCache imageMouseOverActive;
    private ImageCache imageMouseOverFocus;

    private enum ActualImage {
        ACTIVE, INACTIVE, FOCUS, MOUSEOVER_ACTIVE, MOUSEOVER_FOCUS
    }

    private ActualImage actualImage = null;

    public ImagesTextInput(final ImageView imageView, final ImageCache imageActive, final ImageCache imageInactive,
            final ImageCache imageFocus, final ImageCache imageMouseOverActive, final ImageCache imageMouseOverFocus) {
        super(imageView);
        this.imageActive = imageActive;
        this.imageInactive = imageInactive;
        this.imageFocus = imageFocus;
        this.imageMouseOverActive = imageMouseOverActive;
        this.imageMouseOverFocus = imageMouseOverFocus;
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
        case FOCUS:
            imageView.setImage(imageFocus.getImage());
            actualImage = ActualImage.FOCUS;
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        switch (state) {
        case ACTIVE:
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                imageView.setImage(imageFocus.getImage());
                actualImage = ActualImage.FOCUS;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                imageView.setImage(imageMouseOverActive.getImage());
                actualImage = ActualImage.MOUSEOVER_ACTIVE;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                imageView.setImage(imageActive.getImage());
                actualImage = ActualImage.ACTIVE;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
                imageView.setImage(imageActive.getImage());
                actualImage = ActualImage.ACTIVE;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                imageView.setImage(imageMouseOverActive.getImage());
                actualImage = ActualImage.MOUSEOVER_ACTIVE;
            }
            break;
        case INACTIVE:
            imageView.setImage(imageInactive.getImage());
            actualImage = ActualImage.INACTIVE;
            break;
        case FOCUS:
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                imageView.setImage(imageFocus.getImage());
                actualImage = ActualImage.FOCUS;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                imageView.setImage(imageMouseOverFocus.getImage());
                actualImage = ActualImage.MOUSEOVER_FOCUS;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                imageView.setImage(imageFocus.getImage());
                actualImage = ActualImage.FOCUS;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
                imageView.setImage(imageFocus.getImage());
                actualImage = ActualImage.FOCUS;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                imageView.setImage(imageMouseOverFocus.getImage());
                actualImage = ActualImage.MOUSEOVER_FOCUS;
            }
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
        case FOCUS:
            imageFocus = image;
            break;
        case MOUSE_OVER_ACTIVE:
            imageMouseOverActive = image;
            break;
        case MOUSE_OVER_FOCUS:
            imageMouseOverFocus = image;
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
        case FOCUS:
            imageView.setImage(imageFocus.getImage());
            break;
        case MOUSEOVER_ACTIVE:
            imageView.setImage(imageMouseOverActive.getImage());
            break;
        case MOUSEOVER_FOCUS:
            imageView.setImage(imageMouseOverFocus.getImage());
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    public void addEventFilter(final EventHandlerMouseEntered eventHandlerMouseEntered,
            final EventHandlerMouseExited eventHandlerMouseExited,
            final EventHandlerMouseReleased eventHandlerMouseReleased,
            final EventHandlerMouseClicked eventHandlerMouseClicked,
            final EventHandlerMouseMoved eventHandlerMouseMoved) {
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleased.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
    }
}
