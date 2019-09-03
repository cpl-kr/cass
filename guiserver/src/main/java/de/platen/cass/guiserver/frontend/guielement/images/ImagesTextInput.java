package de.platen.cass.guiserver.frontend.guielement.images;

import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.cass.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.cass.guiserver.frontend.guielement.State;
import de.platen.cass.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesTextInput extends ImagesGui {

	private Image imageActive;
	private Image imageInactive;
	private Image imageFocus;
	private Image imageMouseOverActive;
	private Image imageMouseOverFocus;

	private enum ActualImage {
		ACTIVE, INACTIVE, FOCUS, MOUSEOVER_ACTIVE, MOUSEOVER_FOCUS
	}

    private ActualImage actualImage = null;

	public ImagesTextInput(ImageView imageView, Image imageActive, Image imageInactive, Image imageFocus,
			Image imageMouseOverActive, Image imageMouseOverFocus) {
		super(imageView);
		this.imageActive = imageActive;
		this.imageInactive = imageInactive;
		this.imageFocus = imageFocus;
		this.imageMouseOverActive = imageMouseOverActive;
		this.imageMouseOverFocus = imageMouseOverFocus;
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
		case FOCUS:
			imageView.setImage(this.imageFocus);
			this.actualImage = ActualImage.FOCUS;
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
		switch (state) {
		case ACTIVE:
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				imageView.setImage(this.imageFocus);
				this.actualImage = ActualImage.FOCUS;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				imageView.setImage(this.imageMouseOverActive);
				this.actualImage = ActualImage.MOUSEOVER_ACTIVE;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				imageView.setImage(this.imageActive);
				this.actualImage = ActualImage.ACTIVE;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
				imageView.setImage(this.imageActive);
				this.actualImage = ActualImage.ACTIVE;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				imageView.setImage(this.imageMouseOverActive);
				this.actualImage = ActualImage.MOUSEOVER_ACTIVE;
			}
			break;
		case INACTIVE:
			imageView.setImage(this.imageInactive);
			this.actualImage = ActualImage.INACTIVE;
			break;
		case FOCUS:
			if (eventHandlerMouse instanceof EventHandlerMouseClicked) {
				imageView.setImage(this.imageFocus);
				this.actualImage = ActualImage.FOCUS;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseEntered) {
				imageView.setImage(this.imageMouseOverFocus);
				this.actualImage = ActualImage.MOUSEOVER_FOCUS;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseExited) {
				imageView.setImage(this.imageFocus);
				this.actualImage = ActualImage.FOCUS;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseReleased) {
				imageView.setImage(this.imageFocus);
				this.actualImage = ActualImage.FOCUS;
			}
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				imageView.setImage(this.imageMouseOverFocus);
				this.actualImage = ActualImage.MOUSEOVER_FOCUS;
			}
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
		switch (this.actualImage) {
		case ACTIVE:
			imageView.setImage(this.imageActive);
			break;
		case INACTIVE:
			imageView.setImage(this.imageInactive);
			break;
		case FOCUS:
			imageView.setImage(this.imageFocus);
			break;
		case MOUSEOVER_ACTIVE:
			imageView.setImage(this.imageMouseOverActive);
			break;
		case MOUSEOVER_FOCUS:
			imageView.setImage(this.imageMouseOverFocus);
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}

	public void addEventFilter(EventHandlerMouseEntered eventHandlerMouseEntered, EventHandlerMouseExited eventHandlerMouseExited,
			EventHandlerMouseReleased eventHandlerMouseReleased, EventHandlerMouseClicked eventHandlerMouseClicked,
			EventHandlerMouseMoved eventHandlerMouseMoved) {
		imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleased.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
	}
}
