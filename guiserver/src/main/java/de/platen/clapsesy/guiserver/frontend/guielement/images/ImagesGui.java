package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ImagesGui {

	protected final ImageView imageView;
 
	public ImagesGui(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public ImageView getImageView() {
		return this.imageView;
	}
	
	public abstract void setImage(State state);
	
	public abstract void setImage(EventHandlerMouse eventHandlerMouse, State state);
	
	public abstract void setImage(View view, Image image);
}
