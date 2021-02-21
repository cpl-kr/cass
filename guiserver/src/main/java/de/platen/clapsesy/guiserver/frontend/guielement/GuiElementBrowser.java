package de.platen.clapsesy.guiserver.frontend.guielement;

import java.io.IOException;

import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class GuiElementBrowser extends GuiElement {

    private final String stringUrl;

    private enum Os {
        WIN, MAC, LIN, UNKNOWN
    }

    public GuiElementBrowser(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer,
            final X x, final Y y, final Width width, final Height height, final String stringUrl) {
        super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        this.stringUrl = stringUrl;
    }

    public void start() {
        final Os os = getOs();
        try {
            switch (os) {
            case WIN:
                new ProcessBuilder("cmd", "/c", "start", stringUrl).start();
                break;
            case MAC:
                new ProcessBuilder("open", stringUrl).start();
                break;
            case LIN:
                // TODO
                break;
            default:
                break;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setImage(final View view, final ImageCache image) {
    }

    @Override
    public void handleNewActualState(final State state) {
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

    @Override
    public void registerEventsToSend(final Event event) {
    }

    @Override
    public void deregisterEventsToSend(final Event event) {
    }

    private Os getOs() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {
            return Os.WIN;
        }
        if (os.startsWith("mac")) {
            return Os.MAC;
        }
        return Os.LIN;
    }
}
