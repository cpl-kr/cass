package de.platen.clapsesy.guiserver.start;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.Ebene;
import de.platen.clapsesy.guiserver.frontend.ElementId;
import de.platen.clapsesy.guiserver.frontend.GuiElementVerwaltungOperation;
import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;
import de.platen.clapsesy.guiserver.frontend.Zeichnungsnummer;
import de.platen.clapsesy.guiserver.frontend.guielement.Event;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementBrowser;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementButton;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementContent;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementFlaeche;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementKlick;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementSelect;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementSlider;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementTextInput;
import de.platen.clapsesy.guiserver.frontend.guielement.HtmlInput;
import de.platen.clapsesy.guiserver.frontend.guielement.SliderDragg;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.TextInputHtmlInput;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImageCache;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesButton;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesContent;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesKlick;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesSelect;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesSlider;
import de.platen.clapsesy.guiserver.frontend.guielement.images.ImagesTextInput;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.schema.ElementType;
import de.platen.clapsesy.guiserver.schema.SliderDraggType;
import de.platen.clapsesy.guiserver.schema.SourceType;
import de.platen.clapsesy.guiserver.schema.StartflaecheType;
import de.platen.clapsesy.guiserver.schema.StateType;
import de.platen.lib.zahl.GanzzahlPositiv;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class GuiElementFactory {

    public void handleStartflaeche(final StartflaecheType startflaecheType, final HtmlRenderEngine htmlRenderEngine,
            final GuiElementVerwaltungOperation elementVerwaltungOperation) {
        final GuiElementVerwaltungOperation guiElementVerwaltungOperation = new GuiElementVerwaltungOperation();
        for (final ElementType element : startflaecheType.getElementflaeche().getElement()) {
            guiElementVerwaltungOperation
                    .addGuiElement(createGuiElement(element, htmlRenderEngine, elementVerwaltungOperation));
        }
        // TODO Scrollbalken
        elementVerwaltungOperation.addGuiElement(new GuiElementFlaeche(new ElementId(""),
                new Ebene(new GanzzahlPositiv(1)), new Zeichnungsnummer(new GanzzahlPositiv(1)),
                new X(startflaecheType.getPosition().getXPosition().intValue()),
                new Y(startflaecheType.getPosition().getYPosition().intValue()),
                new Width(startflaecheType.getPosition().getWidth().intValue()),
                new Height(startflaecheType.getPosition().getHeight().intValue()), guiElementVerwaltungOperation));
    }

    public GuiElement createGuiElement(final ElementType element, final HtmlRenderEngine htmlRenderEngine,
            final GuiElementVerwaltungOperation elementVerwaltungOperation) {
        GuiElement guiElement = null;
        final ImageView imageView = createImageView(element.getPosition().getXPosition().intValue(),
                element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
                element.getPosition().getHeight().intValue());
        if (element.getOpacity() != null) {
            imageView.setOpacity(element.getOpacity().doubleValue());
        }
        if (element.getElementtyp().getContent() != null) {
            guiElement = createGuiElementContent(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getButton() != null) {
            guiElement = createGuiElementButton(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getSelect() != null) {
            guiElement = createGuiElementSelect(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getKlick() != null) {
            guiElement = createGuiElementKlick(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getSlider() != null) {
            guiElement = createGuiElementSlider(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getTextInput() != null) {
            guiElement = createGuiElementTextInput(element, htmlRenderEngine, imageView);
        }
        if (element.getElementtyp().getElementflaeche() != null) {
            guiElement = createGuiElementElementflaeche(element, htmlRenderEngine, imageView,
                    new GuiElementVerwaltungOperation());
        }
        if (guiElement == null) {
            throw new GuiServerException("Es konnte kein passendes GuiElement erzeugt werden.");
        }
        if (element.getEvent() != null) {
            for (final de.platen.clapsesy.guiserver.schema.Event event : element.getEvent()) {
                final Event eventToSend = convertToEvent(event.getType());
                if (event.isActive()) {
                    guiElement.registerEventsToSend(eventToSend);
                } else {
                    guiElement.deregisterEventsToSend(eventToSend);
                }
            }
        }
        return guiElement;
    }

    public Event convertToEvent(final de.platen.clapsesy.guiserver.schema.EventtypType event) {
        switch (event) {
        case ENTERED:
            return Event.ENTERED;
        case EXITED:
            return Event.EXITED;
        case MOVED:
            return Event.MOVED;
        default:
            throw new GuiServerException("Keine Entsprechung für Event.");
        }
    }

    private GuiElement createGuiElementElementflaeche(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView,
            final GuiElementVerwaltungOperation guiElementVerwaltungOperation) {
        // TODO Scrollbalken
        for (final ElementType einzelelement : element.getElementtyp().getElementflaeche().getElement()) {
            guiElementVerwaltungOperation
                    .addGuiElement(createGuiElement(einzelelement, htmlRenderEngine, guiElementVerwaltungOperation));
        }
        return new GuiElementFlaeche(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), guiElementVerwaltungOperation);
    }

    private GuiElement creatGuiElementBrowser(final ElementType element) {
        return new GuiElementBrowser(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()),
                element.getElementtyp().getContent().getURL());
    }

    private static GuiElementContent createGuiElementContent(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final ImageCache image = createImageCache(element, element.getElementtyp().getContent(), htmlRenderEngine);
        final ImagesContent imageContent = new ImagesContent(imageView, image);
        return new GuiElementContent(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imageContent);

    }

    private static GuiElementButton createGuiElementButton(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final State startState = convertStateType(element.getElementtyp().getButton().getStartState());
        final ImageCache imageActive = createImageCache(element, element.getElementtyp().getButton().getActiveImage(),
                htmlRenderEngine);
        final ImageCache imageInactive = createImageCache(element,
                element.getElementtyp().getButton().getInactiveImage(), htmlRenderEngine);
        final ImageCache imageMouseOver = createImageCache(element,
                element.getElementtyp().getButton().getMouseOverImage(), htmlRenderEngine);
        final ImageCache imagePressed = createImageCache(element, element.getElementtyp().getButton().getPressedImage(),
                htmlRenderEngine);
        final ImagesButton imagesButton = new ImagesButton(imageView, imageActive, imageInactive, imageMouseOver,
                imagePressed);
        return new GuiElementButton(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imagesButton, startState);

    }

    private static GuiElementSelect createGuiElementSelect(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final State startState = convertStateType(element.getElementtyp().getSelect().getStartState());
        final ImageCache imageActiveDeselected = createImageCache(element,
                element.getElementtyp().getSelect().getActiveDeselectedImage(), htmlRenderEngine);
        final ImageCache imageActiveSelected = createImageCache(element,
                element.getElementtyp().getSelect().getActiveSelectedImage(), htmlRenderEngine);
        final ImageCache imageActiveDeselectedMouseOver = createImageCache(element,
                element.getElementtyp().getSelect().getActiveDeselectedMouseOverImage(), htmlRenderEngine);
        final ImageCache imageActiveSelectedMouseOver = createImageCache(element,
                element.getElementtyp().getSelect().getActiveSelectedMouseOverImage(), htmlRenderEngine);
        final ImageCache imageInactiveDeselected = createImageCache(element,
                element.getElementtyp().getSelect().getInactiveDeselectedImage(), htmlRenderEngine);
        final ImageCache imageInactiveSelected = createImageCache(element,
                element.getElementtyp().getSelect().getInactiveSelectedImage(), htmlRenderEngine);
        final ImagesSelect imagesSelect = new ImagesSelect(imageView, imageActiveDeselected, imageActiveSelected,
                imageActiveDeselectedMouseOver, imageActiveSelectedMouseOver, imageInactiveDeselected,
                imageInactiveSelected);
        return new GuiElementSelect(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imagesSelect, startState);

    }

    private static GuiElementKlick createGuiElementKlick(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final State startState = convertStateType(element.getElementtyp().getKlick().getStartState());
        final ImageCache imageActive = createImageCache(element, element.getElementtyp().getKlick().getActiveImage(),
                htmlRenderEngine);
        final ImageCache imageInactive = createImageCache(element,
                element.getElementtyp().getKlick().getInactiveImage(), htmlRenderEngine);
        final ImageCache imageMouseOver = createImageCache(element,
                element.getElementtyp().getKlick().getMouseOverImage(), htmlRenderEngine);
        final ImagesKlick imagesKlick = new ImagesKlick(imageView, imageActive, imageInactive, imageMouseOver);
        return new GuiElementKlick(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imagesKlick, startState);
    }

    private static GuiElementSlider createGuiElementSlider(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final State startState = convertStateType(element.getElementtyp().getSlider().getStartState());
        final ImageCache imageActive = createImageCache(element, element.getElementtyp().getSlider().getActiveImage(),
                htmlRenderEngine);
        final ImageCache imageInactive = createImageCache(element,
                element.getElementtyp().getSlider().getInactiveImage(), htmlRenderEngine);
        final ImageCache imageMouseOver = createImageCache(element,
                element.getElementtyp().getSlider().getMouseOverImage(), htmlRenderEngine);
        final ImageCache imageDragged = createImageCache(element, element.getElementtyp().getSlider().getDraggedImage(),
                htmlRenderEngine);
        final ImagesSlider imagesDragged = new ImagesSlider(imageView, imageActive, imageInactive, imageMouseOver,
                imageDragged);
        final SliderDragg sliderDraggX = convertToSliderDragg(element.getElementtyp().getSlider().getX());
        final SliderDragg sliderDraggY = convertToSliderDragg(element.getElementtyp().getSlider().getY());
        return new GuiElementSlider(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imagesDragged, startState, sliderDraggX,
                sliderDraggY);
    }

    private static GuiElementTextInput createGuiElementTextInput(final ElementType element,
            final HtmlRenderEngine htmlRenderEngine, final ImageView imageView) {
        final State startState = convertStateType(element.getElementtyp().getTextInput().getStartState());
        final ImageCache imageActive = createImageCache(element,
                element.getElementtyp().getTextInput().getActiveImage(), htmlRenderEngine);
        final ImageCache imageInactive = createImageCache(element,
                element.getElementtyp().getTextInput().getInactiveImage(), htmlRenderEngine);
        final ImageCache imageFocus = createImageCache(element, element.getElementtyp().getTextInput().getFocusImage(),
                htmlRenderEngine);
        final ImageCache imageMouseOverActive = createImageCache(element,
                element.getElementtyp().getTextInput().getMouseOverActiveImage(), htmlRenderEngine);
        final ImageCache imageMouseOverFocus = createImageCache(element,
                element.getElementtyp().getTextInput().getMouseOverFocusImage(), htmlRenderEngine);
        final ImagesTextInput imagesTextInput = new ImagesTextInput(imageView, imageActive, imageInactive, imageFocus,
                imageMouseOverActive, imageMouseOverFocus);
        final HtmlInput active = createHtmlInput(
                element.getElementtyp().getTextInput().getInputActive().getBeforeBase64(),
                element.getElementtyp().getTextInput().getInputActive().getAfterBase64());
        final HtmlInput inactive = createHtmlInput(
                element.getElementtyp().getTextInput().getInputInactive().getBeforeBase64(),
                element.getElementtyp().getTextInput().getInputInactive().getAfterBase64());
        final HtmlInput focus = createHtmlInput(
                element.getElementtyp().getTextInput().getInputFocus().getBeforeBase64(),
                element.getElementtyp().getTextInput().getInputFocus().getAfterBase64());
        final HtmlInput mouseOverActive = createHtmlInput(
                element.getElementtyp().getTextInput().getInputMouseOverActive().getBeforeBase64(),
                element.getElementtyp().getTextInput().getInputMouseOverActive().getAfterBase64());
        final HtmlInput mouseOverFocus = createHtmlInput(
                element.getElementtyp().getTextInput().getInputMouseOverFocus().getBeforeBase64(),
                element.getElementtyp().getTextInput().getInputMouseOverFocus().getAfterBase64());
        final TextInputHtmlInput textInputHtmlInput = new TextInputHtmlInput(active, inactive, focus, mouseOverActive,
                mouseOverFocus);
        final byte[] cursor = element.getElementtyp().getTextInput().getCursor();
        return new GuiElementTextInput(new ElementId(element.getID()),
                new Ebene(new GanzzahlPositiv(element.getEbene().intValue())),
                new Zeichnungsnummer(new GanzzahlPositiv(element.getZeichnungsnummer().intValue())),
                new X(element.getPosition().getXPosition().intValue()),
                new Y(element.getPosition().getYPosition().intValue()),
                new Width(element.getPosition().getWidth().intValue()),
                new Height(element.getPosition().getHeight().intValue()), imagesTextInput, startState, htmlRenderEngine,
                textInputHtmlInput, element.getElementtyp().getTextInput().getCharacterCount().intValue(), cursor);
    }

    private static State convertStateType(final StateType state) {
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
            throw new GuiServerException("Keine Entsprechung für StateType.");
        }
    }

    private static ImageView createImageView(final int x, final int y, final int width, final int height) {
        final ImageView imageView = new ImageView();
        imageView.setX(x);
        imageView.setY(y);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private static ImageCache createImageCache(final ElementType element, final SourceType sourceType,
            final HtmlRenderEngine htmlRenderEngine) {
        if (sourceType.getURL() != null) {
            final String urlString = sourceType.getURL();
            final HtmlSource htmlSource = new HtmlSource(htmlRenderEngine, urlString,
                    element.getPosition().getWidth().intValue(), element.getPosition().getHeight().intValue());
            return new ImageCache(htmlSource);
        }
        if (sourceType.getFile() != null) {
            final String file = sourceType.getFile();
            final HtmlSource htmlSource = new HtmlSource(htmlRenderEngine, new File(file),
                    element.getPosition().getWidth().intValue(), element.getPosition().getHeight().intValue());
            return new ImageCache(htmlSource);
        }
        if (sourceType.getHtmlBase64() != null) {
            final byte[] dataBytes = sourceType.getHtmlBase64();
            final HtmlSource htmlSource = new HtmlSource(htmlRenderEngine, dataBytes,
                    element.getPosition().getWidth().intValue(), element.getPosition().getHeight().intValue());
            return new ImageCache(htmlSource);
        }
        return null;
    }

    private static Image createImage(final ElementType element, final SourceType sourceType,
            final HtmlRenderEngine htmlRenderEngine, final String path) {
        if (sourceType.getURL() != null) {
            final String urlString = sourceType.getURL();
            return new Image(htmlRenderEngine.renderPng(urlString, element.getPosition().getWidth().intValue(),
                    element.getPosition().getHeight().intValue()));
        }
        if (sourceType.getFile() != null) {
            final String file = path + sourceType.getFile();
            try {
                return new Image(htmlRenderEngine.renderPng(new File(file), element.getPosition().getWidth().intValue(),
                        element.getPosition().getHeight().intValue()));
            } catch (final IOException e) {
                throw new GuiServerException(e);
            }
        }
        if (sourceType.getHtmlBase64() != null) {
            final byte[] dataBytes = sourceType.getHtmlBase64();
            return new Image(htmlRenderEngine.renderPng(dataBytes, element.getPosition().getWidth().intValue(),
                    element.getPosition().getHeight().intValue()));
        }
        return null;
    }

    private static HtmlInput createHtmlInput(final String base64Before, final String base64After) {
        return new HtmlInput(new String(Base64.getDecoder().decode(base64Before)),
                new String(Base64.getDecoder().decode(base64After)));
    }

    private static SliderDragg convertToSliderDragg(final SliderDraggType sliderDraggType) {
        if (sliderDraggType == null) {
            return null;
        }
        return new SliderDragg(sliderDraggType.getRangePlus().intValue(), sliderDraggType.getRangeMinus().intValue(),
                sliderDraggType.getStepSize().intValue(), sliderDraggType.getMoveCount().intValue());
    }
}
