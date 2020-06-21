package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImagesContent extends ImagesGui {

	private Image image;

	public ImagesContent(ImageView imageView, Image image) {
		super(imageView);
		this.image = image;
	}

	@Override
	public void setImage(State state) {
	}

	@Override
	public void setImage(EventHandlerMouse eventHandlerMouse, State state) {
	}

	@Override
	public void setImage(View view, Image image) {
		imageView.setImage(image);
	}
	
	public void setImage() {
		this.imageView.setImage(this.image);
	}
}
