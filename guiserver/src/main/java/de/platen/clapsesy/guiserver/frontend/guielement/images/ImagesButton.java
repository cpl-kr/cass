package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImagesButton extends ImagesGui {

	private Image imageActive;
	private Image imageInactive;
	private Image imageMouseOver;
	private Image imagePressed;

	private enum ActualImage {
		ACTIVE, INACTIVE, MOUSOVER, PRESSED
	}

    private ActualImage actualImage = null;

	public ImagesButton(ImageView imageView, Image imageActive, Image imageInactive, Image imageMouseOver,
			Image imagePressed) {
		super(imageView);
		this.imageActive = imageActive;
		this.imageInactive = imageInactive;
		this.imageMouseOver = imageMouseOver;
		this.imagePressed = imagePressed;
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
			if (eventHandlerMouse instanceof EventHandlerMousePressed) {
				imageView.setImage(this.imagePressed);
				this.actualImage = ActualImage.PRESSED;
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
			if (eventHandlerMouse instanceof EventHandlerMouseMoved) {
				imageView.setImage(this.imageMouseOver);
				this.actualImage = ActualImage.MOUSOVER;
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
		case PRESSED:
			imagePressed = image;
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
		case PRESSED:
			imageView.setImage(this.imagePressed);
			break;
		default:
			throw new GuiServerException("Kein pasender Zustand gefunden.");
		}
	}
	
	public void addEventFilter(EventHandlerMousePressed eventHandlerMousePressed,
			EventHandlerMouseEntered eventHandlerMouseEntered, EventHandlerMouseExited eventHandlerMouseExited,
			EventHandlerMouseReleased eventHandlerMouseReleased, EventHandlerMouseClicked eventHandlerMouseClicked,
			EventHandlerMouseMoved eventHandlerMouseMoved) {
		imageView.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandlerMousePressed.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_ENTERED, eventHandlerMouseEntered.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_EXITED, eventHandlerMouseExited.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleased.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClicked.getEventHandler());
		imageView.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandlerMouseMoved.getEventHandler());
	}
}
