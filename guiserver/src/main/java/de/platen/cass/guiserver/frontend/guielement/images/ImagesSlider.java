package de.platen.cass.guiserver.frontend.guielement.images;

import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.guielement.State;
import de.platen.cass.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesSlider extends ImagesGui {

	private Image imageActive;
	private Image imageInactive;
	private Image imageMouseOver;
	private Image imageDragged;

	private enum ActualImage {
		ACTIVE, INACTIVE, MOUSOVER, DRAGGED
	}

    private ActualImage actualImage = null;
	
	public ImagesSlider(ImageView imageView, Image imageActive, Image imageInactive, Image imageMouseOver,
			Image imageDragged) {
		super(imageView);
		this.imageActive = imageActive;
		this.imageInactive = imageInactive;
		this.imageMouseOver = imageMouseOver;
		this.imageDragged = imageDragged;
	}

	@Override
	public void setImage(State state) {
		switch (state) {
		case ACTIVE:
			imageView.setImage(this.imageActive);
			this.actualImage = ActualImage.ACTIVE;
			break;
		case INACTIVE:
			imageView.setImage(this.imageInactive);
			this.actualImage = ActualImage.INACTIVE;
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
		switch (state) {
		case ACTIVE:
			if (eventHandlerMouse instanceof EventHandlerMouseDragged) {
				imageView.setImage(this.imageDragged);
				this.actualImage = ActualImage.ACTIVE;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				imageView.setImage(this.imageMouseOver);
				this.actualImage = ActualImage.MOUSOVER;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				imageView.setImage(this.imageActive);
				this.actualImage = ActualImage.ACTIVE;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
				imageView.setImage(this.imageActive);
				this.actualImage = ActualImage.ACTIVE;
			}
			break;
		case INACTIVE:
			imageView.setImage(this.imageInactive);
			this.actualImage = ActualImage.INACTIVE;
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	@Override
	public void setImage(View view, Image image) {
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
		switch (this.actualImage) {
		case ACTIVE:
			imageView.setImage(this.imageActive);
			break;
		case INACTIVE:
			imageView.setImage(this.imageInactive);
			break;
		case MOUSOVER:
			imageView.setImage(this.imageMouseOver);
			break;
		case DRAGGED:
			imageView.setImage(this.imageDragged);
			break;
		default:
			break;
		}
	}
	
	public void addEventFilter(EventHandlerMouseDragged eventHandlerMouseDragged,
			EventHandlerMouseEntered eventHandlerMouseEntered, EventHandlerMouseExited eventHandlerMouseExited,
			EventHandlerMouseReleased eventHandlerMouseReleased, EventHandlerMouseMoved eventHandlerMouseMoved) {
		imageView.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandlerMouseDragged.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleased.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
	}
}
