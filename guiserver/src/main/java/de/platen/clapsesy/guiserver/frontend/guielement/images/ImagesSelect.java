package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesSelect extends ImagesGui {

    private ImageCache imageActiveDeselected;
    private ImageCache imageActiveSelected;
    private ImageCache imageActiveDeselectedMouseOver;
    private ImageCache imageActiveSelectedMouseOver;
    private ImageCache imageInactiveDeselected;
    private ImageCache imageInactiveSelected;

    private enum ActualImage {
        ACTIVEDESELECTED, ACTIVESELECTED, ACTIVEDESELECTEDMOUSEOVER, ACTIVESELECTEDMOUSEOVER, INACTIVEDESELECTED, INACTIVESELECTED
    }

    private ActualImage actualImage = null;

    public ImagesSelect(final ImageView imageView, final ImageCache imageActiveDeselected,
            final ImageCache imageActiveSelected, final ImageCache imageActiveDeselectedMouseOver,
            final ImageCache imageActiveSelectedMouseOver, final ImageCache imageInactiveDeselected,
            final ImageCache imageInactiveSelected) {
        super(imageView);
        this.imageActiveDeselected = imageActiveDeselected;
        this.imageActiveSelected = imageActiveSelected;
        this.imageActiveDeselectedMouseOver = imageActiveDeselectedMouseOver;
        this.imageActiveSelectedMouseOver = imageActiveSelectedMouseOver;
        this.imageInactiveDeselected = imageInactiveDeselected;
        this.imageInactiveSelected = imageInactiveSelected;
    }

    @Override
    public void setImage(final State state) {
        switch (state) {
        case ACTIVE_DESELECTED:
            imageView.setImage(imageActiveDeselected.getImage());
            actualImage = ActualImage.ACTIVEDESELECTED;
            break;
        case ACTIVE_SELECTED:
            imageView.setImage(imageActiveSelected.getImage());
            actualImage = ActualImage.ACTIVESELECTED;
            break;
        case INACTIVE_DESELECTED:
            imageView.setImage(imageInactiveDeselected.getImage());
            actualImage = ActualImage.ACTIVEDESELECTED;
            break;
        case INACTIVE_SELECTED:
            imageView.setImage(imageInactiveSelected.getImage());
            actualImage = ActualImage.ACTIVESELECTED;
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
        switch (state) {
        case ACTIVE_DESELECTED:
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                imageView.setImage(imageActiveDeselectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                imageView.setImage(imageActiveDeselectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                imageView.setImage(imageActiveDeselectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                imageView.setImage(imageActiveDeselected.getImage());
                actualImage = ActualImage.ACTIVEDESELECTED;
            }
            break;
        case ACTIVE_SELECTED:
            if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
                imageView.setImage(imageActiveSelectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
                imageView.setImage(imageActiveSelectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
                imageView.setImage(imageActiveSelectedMouseOver.getImage());
                actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
            }
            if (eventHandlerMouse instanceof EventHandlerMouseExited) {
                imageView.setImage(imageActiveSelected.getImage());
                actualImage = ActualImage.ACTIVESELECTED;
            }
            break;
        case INACTIVE_DESELECTED:
            imageView.setImage(imageInactiveDeselected.getImage());
            actualImage = ActualImage.INACTIVESELECTED;
            break;
        case INACTIVE_SELECTED:
            imageView.setImage(imageInactiveDeselected.getImage());
            actualImage = ActualImage.INACTIVEDESELECTED;
            break;
        default:
            throw new GuiServerException("Kein pasender Zustand gefunden.");
        }
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        switch (view) {
        case ACTIVE_DESELECTED:
            imageActiveDeselected = image;
            break;
        case ACTIVE_SELECTED:
            imageActiveSelected = image;
            break;
        case ACTIVE_DESELECTED_MOUSEOVER:
            imageActiveDeselectedMouseOver = image;
            break;
        case ACTIVE_SELECTED_MOUSEOVER:
            imageActiveSelectedMouseOver = image;
            break;
        case INACTIVE_DESELECTED:
            imageInactiveDeselected = image;
            break;
        case INACTIVE_SELECTED:
            imageInactiveSelected = image;
            break;
        default:
            throw new GuiServerException("Keine passende View gefunden.");
        }
        switch (actualImage) {
        case ACTIVEDESELECTED:
            imageView.setImage(imageActiveDeselected.getImage());
            break;
        case ACTIVESELECTED:
            imageView.setImage(imageActiveSelected.getImage());
            break;
        case ACTIVEDESELECTEDMOUSEOVER:
            imageView.setImage(imageActiveDeselectedMouseOver.getImage());
            break;
        case ACTIVESELECTEDMOUSEOVER:
            imageView.setImage(imageActiveSelectedMouseOver.getImage());
            break;
        case INACTIVEDESELECTED:
            imageView.setImage(imageInactiveDeselected.getImage());
            break;
        case INACTIVESELECTED:
            imageView.setImage(imageInactiveSelected.getImage());
            break;
        default:
            break;
        }
    }

    public void addEventFilter(final EventHandlerMouseClicked eventHandlerMouseClicked,
            final EventHandlerMouseEntered eventHandlerMouseEntered,
            final EventHandlerMouseExited eventHandlerMouseExited,
            final EventHandlerMouseMoved eventHandlerMouseMoved) {
        imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
        imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
    }
}
