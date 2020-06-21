package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesContent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiElementContent extends GuiElement {

	private final ImagesContent imagesContent;

	public GuiElementContent(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			ImagesContent imageContent) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.imagesContent = imageContent;
		this.imagesContent.setImage();
	}

	public ImagesContent getImageContent() {
		return imagesContent;
	}

	@Override
	public void handleNewActualState(State state) {
		this.imagesContent.setImage(state);
	}

	@Override
	public void setImage(View view, Image image) {
		this.imagesContent.setImage(view, image);
	}

	@Override
	public void addInitialFrontView(Group group) {
		group.getChildren().add(this.imagesContent.getImageView());
	}

	@Override
	public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
	}

	@Override
	public ImageView getImageVieww() {
		return this.imagesContent.getImageView();
	}
	
	@Override
	public void registerEventsToSend(Event event) {
	}
	
	@Override
	public void deregisterEventsToSend(Event event) {
	}
}
