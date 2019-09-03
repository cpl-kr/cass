package de.platen.cass.guiserver.start;

import java.util.ArrayList;
import java.util.List;

import org.java_websocket.WebSocket;

import de.platen.cass.guiserver.frontend.ApplicationWindow;
import de.platen.cass.guiserver.frontend.guielement.GuiElement;
import de.platen.cass.guiserver.frontend.guielement.GuiElementFactory;
import de.platen.cass.guiserver.frontend.guielement.State;
import de.platen.cass.guiserver.frontend.guielement.View;
import de.platen.cass.guiserver.renderer.HtmlRenderEngine;
import de.platen.cass.guiserver.schema.ElementType;
import de.platen.cass.guiserver.schema.GUI;
import de.platen.cass.guiserver.schema.PositionType;
import de.platen.cass.guiserver.schema.StateType;
import de.platen.cass.guiserver.schema.ViewType;
import de.platen.cass.guiserver.schema.WindowType;

final class GuiAufbau {

	public static List<ApplicationWindow> baueFenster(GUI gui, String sessionId, WebSocket connection,
			HtmlRenderEngine htmlRenderEngine) {
		List<ApplicationWindow> appWindows = new ArrayList<>();
		List<WindowType> windows = gui.getType().getInitialGUI().getWindows().getWindow();
		for (WindowType window : windows) {
			ApplicationWindow applicationWindow = baueEinzelfenster(sessionId, connection, window, htmlRenderEngine);
			appWindows.add(applicationWindow);
		}
		return appWindows;
	}

	public static ApplicationWindow baueEinzelfenster(String sessionId, WebSocket connection, WindowType window,
			HtmlRenderEngine htmlRenderEngine) {
		String id = window.getID();
		PositionType position = window.getPosition();
		String title = window.getTitle();
		List<ElementType> elemente = window.getElemente().getElement();
		List<GuiElement> guiElemente = new ArrayList<>();
		if (elemente.size() == 1 && elemente.get(0).getElementtyp().getContent() != null) {
			guiElemente.add(GuiElementFactory.creatGuiElementBrowser(elemente.get(0)));
		} else {
			for (ElementType element : elemente) {
				guiElemente.add(GuiElementFactory.createGuiElement(element, htmlRenderEngine));
			}
		}
		ApplicationWindow applicationWindow = new ApplicationWindow(id, position.getXPosition().intValue(),
				position.getYPosition().intValue(), position.getWidth().intValue(), position.getHeight().intValue(),
				title, guiElemente, sessionId, connection);
		return applicationWindow;
	}

	public static State convertToState(StateType state) {
		switch (state) {
		case ACTIVE:
			return State.ACTIVE;
		case INACTIVE:
			return State.INACTIVE;
		case ACTIVE_DESELECTED:
			return State.ACTIVE_DESELECTED;
		case ACTIVE_SELECTED:
			return State.ACTIVE_SELECTED;
		case INACTIVE_DESELECTED:
			return State.INACTIVE_DESELECTED;
		case INACTIVE_SELECTED:
			return State.INACTIVE_SELECTED;
		case FOCUS:
			return State.FOCUS;
		default:
			return null;
		}
	}

	public static View convertToView(ViewType view) {
		switch (view) {
		case ACTIVE:
			return View.ACTIVE;
		case INACTIVE:
			return View.INACTIVE;
		case MOUSE_OVER:
			return View.MOUSEOVER;
		case PRESSED:
			return View.PRESSED;
		case DRAGGED:
			return View.DRAGGED;
		case ACTIVE_DESELECTED:
			return View.ACTIVE_DESELECTED;
		case ACTIVE_SELECTED:
			return View.ACTIVE_SELECTED;
		case ACTIVE_DESELECTED_MOUSE_OVER:
			return View.ACTIVE_DESELECTED_MOUSEOVER;
		case ACTIVE_SELECTED_MOUSE_OVER:
			return View.ACTIVE_SELECTED_MOUSEOVER;
		case INACTIVE_DESELECTED:
			return View.INACTIVE_DESELECTED;
		case INACTIVE_SELECTED:
			return View.INACTIVE_SELECTED;
		case FOCUS:
			return View.FOCUS;
		case ACTIVE_MOUSE_OVER:
			return View.MOUSE_OVER_ACTIVE;
		case FOCUS_MOUSE_OVER:
			return View.MOUSE_OVER_FOCUS;
		default:
			return null;
		}
	}
}
