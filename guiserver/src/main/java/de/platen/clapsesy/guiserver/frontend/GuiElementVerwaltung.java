package de.platen.clapsesy.guiserver.frontend;

import java.util.HashMap;
import java.util.Map;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.lib.objekt.Objekt;

public class GuiElementVerwaltung {

    protected final Map<Ebene, Map<Zeichnungsnummer, GuiElement>> elemente = new HashMap<>();

    public int getAnzahlEbenen() {
        if (elemente.keySet() == null) {
            return 0;
        }
        return elemente.keySet().size();
    }

    public int getAnzahlZeichnungsnummern(final Ebene ebene) {
        Objekt.checkNull(ebene, new GuiServerException("Ebene ist null."));
        if (elemente.get(ebene) == null) {
            return 0;
        }
        return elemente.get(ebene).size();
    }

    public void addGuiElement(final GuiElement guiElement) {
        Objekt.checkNull(guiElement, new GuiServerException("GuiElement ist null."));
        final Ebene ebene = guiElement.getEbene();
        final Zeichnungsnummer zeichnungsnummer = guiElement.getZeichnungsnummer();
        Map<Zeichnungsnummer, GuiElement> map = elemente.get(ebene);
        if (map == null) {
            map = new HashMap<>();
            map.put(zeichnungsnummer, guiElement);
            elemente.put(ebene, map);
        } else {
            if (map.get(zeichnungsnummer) != null) {
                throw new GuiServerException("Element mit Ebene und Zeichnungsnummer schon vorhanden.");
            }
            map.put(zeichnungsnummer, guiElement);
        }
    }

    public GuiElement getGuiElement(final ElementId elementId) {
        Objekt.checkNull(elementId, new GuiServerException("ElementId ist null."));
        if (elemente.keySet() != null) {
            for (final Ebene ebene : elemente.keySet()) {
                if (elemente.get(ebene).keySet() != null) {
                    for (final Zeichnungsnummer zeichnungsnummer : elemente.get(ebene).keySet()) {
                        final GuiElement guiElement = elemente.get(ebene).get(zeichnungsnummer);
                        if (guiElement != null) {
                            if (guiElement.getElementId().equals(elementId)) {
                                return guiElement;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public GuiElement getGuiElement(final Ebene ebene, final Zeichnungsnummer zeichnungsnummer) {
        Objekt.checkNull(ebene, new GuiServerException("Ebene ist null."));
        Objekt.checkNull(zeichnungsnummer, new GuiServerException("Zeichnungsnummer ist null."));
        if (elemente.get(ebene) != null) {
            return elemente.get(ebene).get(zeichnungsnummer);
        }
        return null;
    }

    public void removeGuiElement(final ElementId elementId) {
        Objekt.checkNull(elementId, new GuiServerException("ElementId ist null."));
        final GuiElement guiElement = getGuiElement(elementId);
        if (guiElement != null) {
            final Ebene ebene = guiElement.getEbene();
            final Zeichnungsnummer zeichnungsnummer = guiElement.getZeichnungsnummer();
            elemente.get(ebene).remove(zeichnungsnummer);
        } else {
            throw new GuiServerException("Element nicht vorhanden.");
        }
    }
}
