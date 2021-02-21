package de.platen.clapsesy.guiserver.frontend.guielement.images;

import de.platen.clapsesy.guiserver.start.HtmlSource;
import javafx.scene.image.Image;

public class ImageCache {

    private final HtmlSource htmlSource;

    private Image image;

    public ImageCache(final HtmlSource htmlSource) {
        this.htmlSource = htmlSource;
    }

    public Image getImage() {
        if (image == null) {
            image = htmlSource.renderImage();
        }
        return image;
    }
}
