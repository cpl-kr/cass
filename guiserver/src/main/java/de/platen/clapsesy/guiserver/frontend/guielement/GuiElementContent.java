package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesContent;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class GuiElementContent extends GuiElement {

    private final ImagesContent imagesContent;

    public GuiElementContent(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height, final ImagesContent imageContent) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        imagesContent = imageContent;
        imagesContent.setImage();
    }

    public ImagesContent getImageContent() {
        return imagesContent;
    }

    @Override
    public void handleNewActualState(final State state) {
        imagesContent.setImage(state);
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
        imagesContent.setImage(view, image);
    }

    @Override
    public void addInitialFrontView(final Group group) {
        group.getChildren().add(imagesContent.getImageView());
    }

    @Override
    public void setInitialEventHandlerMouse(final EventHandlerMouse... eventHandlerMouses) {
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
    }

    @Override
    public ImageView getImageVieww() {
        return imagesContent.getImageView();
    }

    @Override
    public void registerEventsToSend(final Event event) {
    }

    @Override
    public void deregisterEventsToSend(final Event event) {
    }
}
