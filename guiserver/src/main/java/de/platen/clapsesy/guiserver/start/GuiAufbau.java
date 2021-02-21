package de.platen.clapsesy.guiserver.start;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.guiserver.frontend.ApplicationWindow;
import de.platen.clapsesy.guiserver.frontend.GuiElementVerwaltungOperation;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.schema.GUI;
import de.platen.clapsesy.guiserver.schema.PositionType;
import de.platen.clapsesy.guiserver.schema.StateType;
import de.platen.clapsesy.guiserver.schema.ViewType;
import de.platen.clapsesy.guiserver.schema.WindowType;

final class GuiAufbau {

    private final GuiElementFactory guiElementFactory;
    private final GuiElementVerwaltungOperation elementVerwaltungOperation;

    public GuiAufbau(final GuiElementFactory guiElementFactory,
            final GuiElementVerwaltungOperation elementVerwaltungOperation) {
        this.guiElementFactory = guiElementFactory;
        this.elementVerwaltungOperation = elementVerwaltungOperation;
    }

    public List<ApplicationWindow> baueFenster(final GUI gui, final UUID sessionId, final WebSocket connection,
            final HtmlRenderEngine htmlRenderEngine) {
        final List<ApplicationWindow> appWindows = new ArrayList<>();
        final List<WindowType> windows = gui.getType().getInitialGUI().getWindows().getWindow();
        for (final WindowType window : windows) {
            final ApplicationWindow applicationWindow = baueEinzelfenster(sessionId, connection, window,
                    htmlRenderEngine);
            appWindows.add(applicationWindow);
        }
        return appWindows;
    }

    public ApplicationWindow baueEinzelfenster(final UUID sessionId, final WebSocket connection,
            final WindowType window, final HtmlRenderEngine htmlRenderEngine) {
        final String id = window.getID();
        final PositionType position = window.getPosition();
        final String title = window.getTitle();
        guiElementFactory.handleStartflaeche(window.getStartflaeche(), htmlRenderEngine, elementVerwaltungOperation);
        final ApplicationWindow applicationWindow = new ApplicationWindow(id, position.getXPosition().intValue(),
                position.getYPosition().intValue(), position.getWidth().intValue(), position.getHeight().intValue(),
                title, elementVerwaltungOperation, sessionId, connection);
        return applicationWindow;
    }

    public State convertToState(final StateType state) {
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

    public View convertToView(final ViewType view) {
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
