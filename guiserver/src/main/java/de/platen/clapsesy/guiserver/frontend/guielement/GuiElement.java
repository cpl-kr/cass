package de.platen.clapsesy.guiserver.frontend.guielement;

import java.util.HashSet;
import java.util.Set;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public abstract class GuiElement {

    private final ElementId elementId;
    private final Ebene ebene;
    private final Zeichnungsnummer zeichnungsnummer;
    private final X x;
    private final Y y;
    private final Width width;
    private final Height height;
    protected final Set<Event> eventsToSend = new HashSet<>();
    protected State actualState;
    protected EventHandlerMouseEntered eventHandlerMouseEntered = null;
    protected EventHandlerMouseExited eventHandlerMouseExited = null;
    protected EventHandlerMouseMoved eventHandlerMouseMoved = null;

    public GuiElement(final ElementId elementId, final Ebene ebene, final Zeichnungsnummer zeichnungsnummer, final X x,
            final Y y, final Width width, final Height height) {
        this.elementId = elementId;
        this.ebene = ebene;
        this.zeichnungsnummer = zeichnungsnummer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ElementId getElementId() {
        return elementId;
    }

    public Ebene getEbene() {
        return ebene;
    }

    public Zeichnungsnummer getZeichnungsnummer() {
        return zeichnungsnummer;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Width getWidth() {
        return width;
    }

    public Height getHeight() {
        return height;
    }

    public State getActualState() {
        return actualState;
    }

    public boolean isInactive() {
        if (actualState == null) {
            return true;
        }
        if (actualState.equals(State.INACTIVE)) {
            return true;
        }
        if (actualState.equals(State.INACTIVE_DESELECTED)) {
            return true;
        }
        return actualState.equals(State.INACTIVE_SELECTED);
    }

    public void registerEventsToSend(final Event event) {
        if (areEventHandlerNull()) {
            eventsToSend.add(event);
        } else {
            switch (event) {
            case ENTERED:
                eventHandlerMouseEntered.setHasToSend(true);
                break;
            case EXITED:
                eventHandlerMouseExited.setHasToSend(true);
                break;
            case MOVED:
                eventHandlerMouseMoved.setHasToSend(true);
                break;
            default:
                throw new GuiServerException("Kein passender Event gefunden.");
            }
        }
    }

    public void deregisterEventsToSend(final Event event) {
        if (areEventHandlerNull()) {
            eventsToSend.remove(event);
        } else {
            switch (event) {
            case ENTERED:
                eventHandlerMouseEntered.setHasToSend(false);
                break;
            case EXITED:
                eventHandlerMouseExited.setHasToSend(false);
                break;
            case MOVED:
                eventHandlerMouseMoved.setHasToSend(false);
                break;
            default:
                throw new GuiServerException("Kein passender Event gefunden.");
            }
        }
    }

    public abstract void handleNewActualState(State state);

    public abstract void setImage(View view, ImageCache image);

    public abstract void addInitialFrontView(Group group);

    public abstract void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses);

    public abstract void setImage(EventHandlerMouse eventHandlerMouse, State state);

    public abstract ImageView getImageVieww();

    private boolean areEventHandlerNull() {
        return eventHandlerMouseEntered == null || //
                eventHandlerMouseExited == null || //
                eventHandlerMouseMoved == null;
    }
}
