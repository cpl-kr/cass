package de.platen.clapsesy.guiserver.frontend;

import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseClicked;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseDragged;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseEntered;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseExited;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseMoved;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMousePressed;
import de.platen.clapsesy.guiserver.eventsender.EventSenderMouseReleased;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseClicked;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseDragged;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseEntered;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseExited;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseMoved;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMousePressed;
import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouseReleased;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementFlaeche;
import de.platen.lib.zahl.GanzzahlPositiv;

public class GuiElementVerwaltungOperation extends GuiElementVerwaltung {

    public void addInitialFrontView(final Verbindung verbindung) {
        final int anzahlEbenen = getAnzahlEbenen();
        for (int nummerEbene = 1; nummerEbene <= anzahlEbenen; nummerEbene++) {
            final Ebene ebene = new Ebene(new GanzzahlPositiv(nummerEbene));
            final int anzahlZeichnungsnummern = getAnzahlZeichnungsnummern(ebene);
            for (int nummerZeichnungsnummer = 1; nummerZeichnungsnummer <= anzahlZeichnungsnummern; nummerZeichnungsnummer++) {
                final Zeichnungsnummer zeichnungsnummer = new Zeichnungsnummer(
                        new GanzzahlPositiv(nummerZeichnungsnummer));
                final GuiElement guiElement = elemente.get(ebene).get(zeichnungsnummer);
                if (guiElement instanceof GuiElementFlaeche) {
                    System.out.println("Initial FrontView für ein Elementfläche");
                    final GuiElementFlaeche guiElementFlaeche = (GuiElementFlaeche) guiElement;
                    guiElementFlaeche.getVerwaltung().addInitialFrontView(verbindung);
                } else {
                    System.out.println("Initial FrontView für ein Element");
                    guiElement.addInitialFrontView(verbindung.get());
                }
            }
        }
    }

    public void addInitialEventHandler(final UUID sessionId, final String windowId, final WebSocket connection) {
        final int anzahlEbenen = getAnzahlEbenen();
        for (int nummerEbene = 1; nummerEbene < anzahlEbenen; nummerEbene++) {
            final Ebene ebene = new Ebene(new GanzzahlPositiv(nummerEbene));
            final int anzahlZeichnungsnummern = getAnzahlZeichnungsnummern(ebene);
            for (int nummerZeichnungsnummer = 1; nummerZeichnungsnummer < anzahlZeichnungsnummern; nummerZeichnungsnummer++) {
                final Zeichnungsnummer zeichnungsnummer = new Zeichnungsnummer(
                        new GanzzahlPositiv(nummerZeichnungsnummer));
                final GuiElement guiElement = elemente.get(ebene).get(zeichnungsnummer);
                if (guiElement instanceof GuiElementFlaeche) {
                    final GuiElementFlaeche guiElementFlaeche = (GuiElementFlaeche) guiElement;
                    guiElementFlaeche.getVerwaltung().addInitialEventHandler(sessionId, windowId, connection);
                } else {
                    addInitialEventHandler(guiElement, windowId, windowId, connection);
                }
            }
        }
    }

    private void addInitialEventHandler(final GuiElement guiElement, final String sessionId, final String windowId,
            final WebSocket connection) {
        final EventSenderMouseClicked eventSenderMouseClicked = new EventSenderMouseClicked(sessionId, windowId,
                connection);
        final EventSenderMousePressed eventSenderMousePressed = new EventSenderMousePressed(sessionId, windowId,
                connection);
        final EventSenderMouseReleased eventSenderMouseReleased = new EventSenderMouseReleased(sessionId, windowId,
                connection);
        final EventSenderMouseEntered eventSenderMouseEntered = new EventSenderMouseEntered(sessionId, windowId,
                connection);
        final EventSenderMouseExited eventSenderMouseExited = new EventSenderMouseExited(sessionId, windowId,
                connection);
        final EventSenderMouseMoved eventSenderMouseMoved = new EventSenderMouseMoved(sessionId, windowId, connection);
        final EventSenderMouseDragged eventSenderMouseDragged = new EventSenderMouseDragged(sessionId, windowId,
                connection);
        final EventHandlerMouseClicked eventHandlerMouseClicked = new EventHandlerMouseClicked(guiElement,
                eventSenderMouseClicked);
        final EventHandlerMousePressed eventHandlerMousePressed = new EventHandlerMousePressed(guiElement,
                eventSenderMousePressed);
        final EventHandlerMouseReleased eventHandlerMouseReleased = new EventHandlerMouseReleased(guiElement,
                eventSenderMouseReleased);
        final EventHandlerMouseEntered eventHandlerMouseEntered = new EventHandlerMouseEntered(guiElement,
                eventSenderMouseEntered);
        final EventHandlerMouseExited eventHandlerMouseExited = new EventHandlerMouseExited(guiElement,
                eventSenderMouseExited);
        final EventHandlerMouseMoved eventHandlerMouseMoved = new EventHandlerMouseMoved(guiElement,
                eventSenderMouseMoved);
        final EventHandlerMouseDragged eventHandlerMouseDragged = new EventHandlerMouseDragged(guiElement,
                eventSenderMouseDragged);
        guiElement.setInitialEventHandlerMouse(eventHandlerMousePressed, eventHandlerMouseEntered,
                eventHandlerMouseExited, eventHandlerMouseReleased, eventHandlerMouseClicked, eventHandlerMouseMoved,
                eventHandlerMouseDragged);
    }
}
