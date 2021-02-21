package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import javafx.scene.image.ImageView;

public class ImagesContent extends ImagesGui {

    private final ImageCache image;

    public ImagesContent(final ImageView imageView, final ImageCache image) {
        super(imageView);
        this.image = image;
    }

    @Override
    public void setImage(final State state) {
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        imageView.setImage(image.getImage());
    }

    public void setImage() {
        imageView.setImage(image.getImage());
    }
}
