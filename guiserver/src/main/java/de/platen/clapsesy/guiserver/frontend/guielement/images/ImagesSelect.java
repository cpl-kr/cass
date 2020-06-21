package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesSelect extends ImagesGui {

	private Image imageActiveDeselected;
	private Image imageActiveSelected;
	private Image imageActiveDeselectedMouseOver;
	private Image imageActiveSelectedMouseOver;
	private Image imageInactiveDeselected;
	private Image imageInactiveSelected;

	private enum ActualImage {
		ACTIVEDESELECTED, ACTIVESELECTED, ACTIVEDESELECTEDMOUSEOVER, ACTIVESELECTEDMOUSEOVER, INACTIVEDESELECTED,
		INACTIVESELECTED
	}

    private ActualImage actualImage = null;

	public ImagesSelect(ImageView imageView, Image imageActiveDeselected, Image imageActiveSelected,
			Image imageActiveDeselectedMouseOver, Image imageActiveSelectedMouseOver, Image imageInactiveDeselected,
			Image imageInactiveSelected) {
		super(imageView);
		this.imageActiveDeselected = imageActiveDeselected;
		this.imageActiveSelected = imageActiveSelected;
		this.imageActiveDeselectedMouseOver = imageActiveDeselectedMouseOver;
		this.imageActiveSelectedMouseOver = imageActiveSelectedMouseOver;
		this.imageInactiveDeselected = imageInactiveDeselected;
		this.imageInactiveSelected = imageInactiveSelected;
	}

	@Override
	public void setImage(State state) {
		switch (state) {
		case ACTIVE_DESELECTED:
			imageView.setImage(this.imageActiveDeselected);
			this.actualImage = ActualImage.ACTIVEDESELECTED;
			break;
		case ACTIVE_SELECTED:
			imageView.setImage(this.imageActiveSelected);
			this.actualImage = ActualImage.ACTIVESELECTED;
			break;
		case INACTIVE_DESELECTED:
			imageView.setImage(this.imageInactiveDeselected);
			this.actualImage = ActualImage.ACTIVEDESELECTED;
			break;
		case INACTIVE_SELECTED:
			imageView.setImage(this.imageInactiveSelected);
			this.actualImage = ActualImage.ACTIVESELECTED;
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
		switch (state) {
		case ACTIVE_DESELECTED:
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				imageView.setImage(this.imageActiveDeselectedMouseOver);
				this.actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				imageView.setImage(this.imageActiveDeselectedMouseOver);
				this.actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				imageView.setImage(this.imageActiveDeselectedMouseOver);
				this.actualImage = ActualImage.ACTIVEDESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				imageView.setImage(this.imageActiveDeselected);
				this.actualImage = ActualImage.ACTIVEDESELECTED;
			}
			break;
		case ACTIVE_SELECTED:
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				imageView.setImage(this.imageActiveSelectedMouseOver);
				this.actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				imageView.setImage(this.imageActiveSelectedMouseOver);
				this.actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				imageView.setImage(this.imageActiveSelectedMouseOver);
				this.actualImage = ActualImage.ACTIVESELECTEDMOUSEOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				imageView.setImage(this.imageActiveSelected);
				this.actualImage = ActualImage.ACTIVESELECTED;
			}
			break;
		case INACTIVE_DESELECTED:
			imageView.setImage(this.imageInactiveDeselected);
			this.actualImage = ActualImage.INACTIVESELECTED;
			break;
		case INACTIVE_SELECTED:
			imageView.setImage(this.imageInactiveDeselected);
			this.actualImage = ActualImage.INACTIVEDESELECTED;
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	@Override
	public void setImage(View view, Image image) {
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
		switch (this.actualImage) {
		case ACTIVEDESELECTED:
			imageView.setImage(this.imageActiveDeselected);
			break;
		case ACTIVESELECTED:
			imageView.setImage(this.imageActiveSelected);
			break;
		case ACTIVEDESELECTEDMOUSEOVER:
			imageView.setImage(this.imageActiveDeselectedMouseOver);
			break;
		case ACTIVESELECTEDMOUSEOVER:
			imageView.setImage(this.imageActiveSelectedMouseOver);
			break;
		case INACTIVEDESELECTED:
			imageView.setImage(this.imageInactiveDeselected);
			break;
		case INACTIVESELECTED:
			imageView.setImage(this.imageInactiveSelected);
			break;
		default:
			break;
		}
	}

	public void addEventFilter(EventHandlerMouseClicked eventHandlerMouseClicked,
			EventHandlerMouseEntered eventHandlerMouseEntered, EventHandlerMouseExited eventHandlerMouseExited,
			EventHandlerMouseMoved eventHandlerMouseMoved) {
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
	}
}
