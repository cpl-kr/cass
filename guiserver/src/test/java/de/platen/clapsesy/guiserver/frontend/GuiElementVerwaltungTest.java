package de.platen.clapsesy.guiserver.frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import org.junit.Test;
import org.mockito.Mockito;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.lib.zahl.GanzzahlPositiv;

public class GuiElementVerwaltungTest {

    @Test
    public void testGetAnzahlEbenenKeineEbene() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        assertEquals(0, guiElementVerwaltungZumTest.getAnzahlEbenen());
    }

    @Test
    public void testGetAnzahlEbenenEineEbene() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), new HashMap<>());
        assertEquals(1, guiElementVerwaltungZumTest.getAnzahlEbenen());
    }

    @Test
    public void testGetAnzahlZeichnungsnummernKeineEbeneInElemente() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        assertEquals(0, guiElementVerwaltungZumTest.getAnzahlZeichnungsnummern(new Ebene(new GanzzahlPositiv(1))));
    }

    @Test
    public void testGetAnzahlZeichnungsnummernEbeneNichtVorhandenEbeneInElementeVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), new HashMap<>());
        assertEquals(0, guiElementVerwaltungZumTest.getAnzahlZeichnungsnummern(new Ebene(new GanzzahlPositiv(2))));
    }

    @Test
    public void testGetAnzahlZeichnungsnummernEineEbeneInElementeVorhandenKeineZeichnungsnummerZuDieserEbene() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), new HashMap<>());
        assertEquals(0, guiElementVerwaltungZumTest.getAnzahlZeichnungsnummern(new Ebene(new GanzzahlPositiv(1))));
    }

    @Test
    public void testGetAnzahlZeichnungsnummernEineEbeneInElementeVorhandenEineZeichnungsnummerZuDieserEbene() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        final Map<Zeichnungsnummer, GuiElement> map = new HashMap<>();
        map.put(new Zeichnungsnummer(new GanzzahlPositiv(1)), Mockito.mock(GuiElement.class));
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), map);
        assertEquals(1, guiElementVerwaltungZumTest.getAnzahlZeichnungsnummern(new Ebene(new GanzzahlPositiv(1))));
    }

    @Test
    public void testAddGuiElementGuiElementNull() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        try {
            guiElementVerwaltungZumTest.addGuiElement(null);
            fail();
        } catch (final GuiServerException e) {
            assertEquals("GuiElement ist null.", e.getMessage());
        }
    }

    @Test
    public void testAddGuiElementKeineEbeneInElementeVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        final GuiElement guiElement = mockGuiElement(1, 1);
        guiElementVerwaltungZumTest.addGuiElement(guiElement);
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().size());
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1))).size());
        assertEquals(guiElement, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1)))
                .get(new Zeichnungsnummer(new GanzzahlPositiv(1))));
    }

    @Test
    public void testAddGuiElementAndereEbeneInElementeVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), new HashMap<>());
        final GuiElement guiElement = mockGuiElement(2, 1);
        guiElementVerwaltungZumTest.addGuiElement(guiElement);
        assertEquals(2, guiElementVerwaltungZumTest.getElemente().size());
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(2))).size());
        assertEquals(guiElement, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(2)))
                .get(new Zeichnungsnummer(new GanzzahlPositiv(1))));
    }

    @Test
    public void testAddGuiElementKeineZeichnungsnummerZurEbeneVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), null);
        final GuiElement guiElement = mockGuiElement(1, 1);
        guiElementVerwaltungZumTest.addGuiElement(guiElement);
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().size());
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1))).size());
        assertEquals(guiElement, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1)))
                .get(new Zeichnungsnummer(new GanzzahlPositiv(1))));
    }

    @Test
    public void testAddGuiElementLeereZeichnungsnummerMapZurEbeneVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), new HashMap<>());
        final GuiElement guiElement = mockGuiElement(1, 1);
        guiElementVerwaltungZumTest.addGuiElement(guiElement);
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().size());
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1))).size());
        assertEquals(guiElement, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1)))
                .get(new Zeichnungsnummer(new GanzzahlPositiv(1))));
    }

    @Test
    public void testAddGuiElementEineZeichnungsnummerInMapZurEbeneVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        final Map<Zeichnungsnummer, GuiElement> map = new HashMap<>();
        map.put(new Zeichnungsnummer(new GanzzahlPositiv(1)), mockGuiElement(1, 1));
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), map);
        final GuiElement guiElement = mockGuiElement(1, 2);
        guiElementVerwaltungZumTest.addGuiElement(guiElement);
        assertEquals(1, guiElementVerwaltungZumTest.getElemente().size());
        assertEquals(2, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1))).size());
        assertEquals(guiElement, guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1)))
                .get(new Zeichnungsnummer(new GanzzahlPositiv(2))));
    }

    // TODO Test getGuiElement anhand elementId: ElementId null
    // TODO Test getGuiElement anhand elementId: Keine Ebene
    // TODO Test getGuiElement anhand elementId: Ebene zum Element, keine Zeichnungsnummer in Map
    // TODO Test getGuiElement anhand elementId: Ebene zum Element, andere Zeichnungsnummer zum Element
    // TODO Test getGuiElement anhand elementId: Ebene zum Element, Zeichnungsnummer zum Element

    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Ebene null
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Zeichnungsnummer null
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Keine Ebene
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Andere Ebene
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Ebene zum Element, keine Zeichnungsnummer in Map
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Ebene zum Element, andere Zeichnungsnummer zum Element
    // TODO Test getGuiElement anhand Ebene und Zeichnungsnummer: Ebene zum Element, Zeichnungsnummer zum Element

    @Test
    public void testRemoveGuiElementElementIdNull() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        try {
            guiElementVerwaltungZumTest.removeGuiElement(null);
            fail();
        } catch (final GuiServerException e) {
            assertEquals("ElementId ist null.", e.getMessage());
        }
    }

    @Test
    public void testRemoveGuiElementElementNichtVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        try {
            guiElementVerwaltungZumTest.removeGuiElement(new ElementId("id"));
            fail();
        } catch (final GuiServerException e) {
            assertEquals("Element nicht vorhanden.", e.getMessage());
        }
    }

    @Test
    public void testRemoveGuiElementElementVorhanden() {
        final GuiElementVerwaltungZumTest guiElementVerwaltungZumTest = new GuiElementVerwaltungZumTest();
        final Map<Zeichnungsnummer, GuiElement> map = new HashMap<>();
        final GuiElement guiElement = Mockito.mock(GuiElement.class);
        Mockito.when(guiElement.getElementId()).thenReturn(new ElementId("id"));
        Mockito.when(guiElement.getEbene()).thenReturn(new Ebene(new GanzzahlPositiv(1)));
        Mockito.when(guiElement.getZeichnungsnummer()).thenReturn(new Zeichnungsnummer(new GanzzahlPositiv(1)));
        map.put(new Zeichnungsnummer(new GanzzahlPositiv(1)), guiElement);
        guiElementVerwaltungZumTest.getElemente().put(new Ebene(new GanzzahlPositiv(1)), map);
        guiElementVerwaltungZumTest.removeGuiElement(new ElementId("id"));
        assertTrue(guiElementVerwaltungZumTest.getElemente().get(new Ebene(new GanzzahlPositiv(1))).isEmpty());
    }

    private GuiElement mockGuiElement(final int ebene, final int zeichnungsnummer) {
        final GuiElementTest guiElement = Mockito.mock(GuiElementTest.class);
        Mockito.when(guiElement.getEbene()).thenReturn(new Ebene(new GanzzahlPositiv(ebene)));
        Mockito.when(guiElement.getZeichnungsnummer())
                .thenReturn(new Zeichnungsnummer(new GanzzahlPositiv(zeichnungsnummer)));
        return guiElement;
    }

    private class GuiElementVerwaltungZumTest extends GuiElementVerwaltung {

        public Map<Ebene, Map<Zeichnungsnummer, GuiElement>> getElemente() {
            return elemente;
        }
    }

    private class GuiElementTest extends GuiElement {

        public GuiElementTest(ElementId elementId, Ebene ebene, Zeichnungsnummer zeichnungsnummer, X x, Y y, Width width, Height height) {
            super(elementId, ebene, zeichnungsnummer, x, y, width, height);
        }

        @Override
        public void handleNewActualState(State state) {

        }

        @Override
        public void setImage(View view, ImageCache image) {

        }

        @Override
        public void addInitialFrontView(Group group) {

        }

        @Override
        public void setInitialEventHandlerMouse(EventHandlerMouse... eventHandlerMouses) {

        }

        @Override
        public void setImage(EventHandlerMouse eventHandlerMouse, State state) {

        }

        @Override
        public ImageView getImageVieww() {
            return null;
        }
    }
}
