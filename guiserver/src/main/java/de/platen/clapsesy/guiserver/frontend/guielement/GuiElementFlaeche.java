package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.GuiElementVerwaltungOperation;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class GuiElementFlaeche extends GuiElement {

    private final GuiElementVerwaltungOperation elementverwaltung;

    public GuiElementFlaeche(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height,
            final GuiElementVerwaltungOperation elementverwaltung) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        this.elementverwaltung = elementverwaltung;
    }

    @Override
    public void handleNewActualState(final State state) {
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
    }

    @Override
    public void addInitialFrontView(final Group group) {
    }

    @Override
    public void setInitialEventHandlerMouse(final EventHandlerMouse... eventHandlerMouses) {
    }

    @Override
    public void setImage(final EventHandlerMouse eventHandlerMouse, final State state) {
    }

    @Override
    public ImageView getImageVieww() {
        return null;
    }

    public GuiElementVerwaltungOperation getVerwaltung() {
        return elementverwaltung;
    }
}
