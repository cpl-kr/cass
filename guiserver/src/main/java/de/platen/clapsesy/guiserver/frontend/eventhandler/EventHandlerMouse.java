package de.platen.clapsesy.guiserver.frontend.eventhandler;

import de.platen.clapsesy.guiserver.eventsender.MouseButtonGui;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class EventHandlerMouse {

	protected final GuiElement guiElement;
	
	protected EventHandlerMouse(GuiElement guiElement) {
		this.guiElement = guiElement;
	}
	
	protected MouseButtonGui convertToMouseButtonGui(MouseButton mouseButton) {
		switch(mouseButton) {
		case PRIMARY:
			return MouseButtonGui.PRIMARY;
		case SECONDARY:
			return MouseButtonGui.SECONDARY;
		case MIDDLE:
			return MouseButtonGui.MIDDLE;
			default:
				throw new GuiServerException("Kein passendes Aufzählungselement gefunden.");
		}
	}
	
	public abstract EventHandler<MouseEvent> getEventHandler();
}
