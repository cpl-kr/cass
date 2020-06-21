package de.platen.clapsesy.guiserver.frontend.guielement;

import java.util.HashSet;
import java.util.Set;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GuiElement {

	private final String id;
	private final int ebene;
	private final int zeichnungsnummer;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	protected final Set<Event> eventsToSend = new HashSet<>();
	protected State actualState;
	protected EventHandlerMouseEntered eventHandlerMouseEntered = null;
	protected EventHandlerMouseExited eventHandlerMouseExited = null;
	protected EventHandlerMouseMoved eventHandlerMouseMoved = null;

	public GuiElement(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height) {
		this.id = id;
		this.ebene = ebene;
		this.zeichnungsnummer = zeichnungsnummer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public int getEbene() {
		return ebene;
	}

	public int getZeichnungsnummer() {
		return zeichnungsnummer;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
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
	
	public void registerEventsToSend(Event event) {
		if (this.areEventHandlerNull()) {
			eventsToSend.add(event);
		} else {
			switch (event) {
			case ENTERED:
				this.eventHandlerMouseEntered.setHasToSend(true);
				break;
			case EXITED:
				this.eventHandlerMouseExited.setHasToSend(true);
				break;
			case MOVED:
				this.eventHandlerMouseMoved.setHasToSend(true);
				break;
			default:
				throw new GuiServerException("Kein passender Event gefunden.");
			}
		}
	}

	public void deregisterEventsToSend(Event event) {
		if (this.areEventHandlerNull()) {
			eventsToSend.remove(event);
		} else {
			switch (event) {
			case ENTERED:
				this.eventHandlerMouseEntered.setHasToSend(false);
				break;
			case EXITED:
				this.eventHandlerMouseExited.setHasToSend(false);
				break;
			case MOVED:
				this.eventHandlerMouseMoved.setHasToSend(false);
				break;
			default:
				throw new GuiServerException("Kein passender Event gefunden.");
			}
		}
	}

	public abstract void handleNewActualState(State state);
	
	public abstract void setImage(View view, Image image);
	
	public abstract void addInitialFrontView(Group group);
	
	public abstract void setInitialEventHandlerMouse(EventHandlerMouse ... eventHandlerMouses);
	
	public abstract void setImage(EventHandlerMouse eventHandlerMouse, State state);
	
	public abstract ImageView getImageVieww();
	
	private boolean areEventHandlerNull() {
        return this.eventHandlerMouseEntered == null || //
                this.eventHandlerMouseExited == null || //
                this.eventHandlerMouseMoved == null;
    }
}
