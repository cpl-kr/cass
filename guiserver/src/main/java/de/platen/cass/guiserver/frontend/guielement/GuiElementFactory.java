package de.platen.cass.guiserver.frontend.guielement;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.xml.sax.SAXException;

import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesButton;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesContent;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesKlick;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesSelect;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesSlider;
import de.platen.cass.guiserver.frontend.guielement.images.ImagesTextInput;
import de.platen.cass.guiserver.renderer.HtmlRenderEngine;
import de.platen.cass.guiserver.schema.ElementType;
import de.platen.cass.guiserver.schema.SliderDraggType;
import de.platen.cass.guiserver.schema.SourceType;
import de.platen.cass.guiserver.schema.StateType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class GuiElementFactory {

	public static GuiElement createGuiElement(ElementType element, HtmlRenderEngine htmlRenderEngine) {
		GuiElement guiElement = null;
		try {
			ImageView imageView = createImageView(element.getPosition().getXPosition().intValue(),
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
		} catch (IOException | SAXException e) {
			throw new GuiServerException(e);
		}
		if (guiElement == null) {
			throw new GuiServerException("Es konnte kein passendes GuiElement erzeugt werden.");
		}
		if (element.getEvent() != null) {
			for (de.platen.cass.guiserver.schema.Event event : element.getEvent()) {
				Event eventToSend = convertToEvent(event.getType());
				if (event.isActive()) {
					guiElement.registerEventsToSend(eventToSend);
				} else {
					guiElement.deregisterEventsToSend(eventToSend);
				}
			}
		}
		return guiElement;
	}

	public static GuiElement creatGuiElementBrowser(ElementType element) {
		return new GuiElementBrowser(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), element.getElementtyp().getContent().getURL());
	}

	public static Event convertToEvent(de.platen.cass.guiserver.schema.EventtypType event) {
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

	private static GuiElementContent createGuiElementContent(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		Image image = createImage(element, element.getElementtyp().getContent(), htmlRenderEngine);
		ImagesContent imageContent = new ImagesContent(imageView, image);
		return new GuiElementContent(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imageContent);

	}

	private static GuiElementButton createGuiElementButton(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		State startState = convertStateType(element.getElementtyp().getButton().getStartState());
		Image imageActive = createImage(element, element.getElementtyp().getButton().getActiveImage(),
				htmlRenderEngine);
		Image imageInactive = createImage(element, element.getElementtyp().getButton().getInactiveImage(),
				htmlRenderEngine);
		Image imageMouseOver = createImage(element, element.getElementtyp().getButton().getMouseOverImage(),
				htmlRenderEngine);
		Image imagePressed = createImage(element, element.getElementtyp().getButton().getPressedImage(),
				htmlRenderEngine);
		ImagesButton imagesButton = new ImagesButton(imageView, imageActive, imageInactive, imageMouseOver,
				imagePressed);
		return new GuiElementButton(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imagesButton, startState);

	}

	private static GuiElementSelect createGuiElementSelect(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		State startState = convertStateType(element.getElementtyp().getSelect().getStartState());
		Image imageActiveDeselected = createImage(element,
				element.getElementtyp().getSelect().getActiveDeselectedImage(), htmlRenderEngine);
		Image imageActiveSelected = createImage(element, element.getElementtyp().getSelect().getActiveSelectedImage(),
				htmlRenderEngine);
		Image imageActiveDeselectedMouseOver = createImage(element,
				element.getElementtyp().getSelect().getActiveDeselectedMouseOverImage(), htmlRenderEngine);
		Image imageActiveSelectedMouseOver = createImage(element,
				element.getElementtyp().getSelect().getActiveSelectedMouseOverImage(), htmlRenderEngine);
		Image imageInactiveDeselected = createImage(element,
				element.getElementtyp().getSelect().getInactiveDeselectedImage(), htmlRenderEngine);
		Image imageInactiveSelected = createImage(element,
				element.getElementtyp().getSelect().getInactiveSelectedImage(), htmlRenderEngine);
		ImagesSelect imagesSelect = new ImagesSelect(imageView, imageActiveDeselected, imageActiveSelected,
				imageActiveDeselectedMouseOver, imageActiveSelectedMouseOver, imageInactiveDeselected,
				imageInactiveSelected);
		return new GuiElementSelect(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imagesSelect, startState);

	}

	private static GuiElementKlick createGuiElementKlick(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		State startState = convertStateType(element.getElementtyp().getKlick().getStartState());
		Image imageActive = createImage(element, element.getElementtyp().getKlick().getActiveImage(), htmlRenderEngine);
		Image imageInactive = createImage(element, element.getElementtyp().getKlick().getInactiveImage(),
				htmlRenderEngine);
		Image imageMouseOver = createImage(element, element.getElementtyp().getKlick().getMouseOverImage(),
				htmlRenderEngine);
		ImagesKlick imagesKlick = new ImagesKlick(imageView, imageActive, imageInactive, imageMouseOver);
		return new GuiElementKlick(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imagesKlick, startState);
	}

	private static GuiElementSlider createGuiElementSlider(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		State startState = convertStateType(element.getElementtyp().getSlider().getStartState());
		Image imageActive = createImage(element, element.getElementtyp().getSlider().getActiveImage(),
				htmlRenderEngine);
		Image imageInactive = createImage(element, element.getElementtyp().getSlider().getInactiveImage(),
				htmlRenderEngine);
		Image imageMouseOver = createImage(element, element.getElementtyp().getSlider().getMouseOverImage(),
				htmlRenderEngine);
		Image imageDragged = createImage(element, element.getElementtyp().getSlider().getDraggedImage(),
				htmlRenderEngine);
		ImagesSlider imagesDragged = new ImagesSlider(imageView, imageActive, imageInactive, imageMouseOver,
				imageDragged);
		SliderDragg sliderDraggX = convertToSliderDragg(element.getElementtyp().getSlider().getX());
		SliderDragg sliderDraggY = convertToSliderDragg(element.getElementtyp().getSlider().getY());
		return new GuiElementSlider(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imagesDragged, startState, sliderDraggX, sliderDraggY);
	}

	private static GuiElementTextInput createGuiElementTextInput(ElementType element, HtmlRenderEngine htmlRenderEngine,
			ImageView imageView) throws IOException, SAXException {
		State startState = convertStateType(element.getElementtyp().getTextInput().getStartState());
		Image imageActive = createImage(element, element.getElementtyp().getTextInput().getActiveImage(),
				htmlRenderEngine);
		Image imageInactive = createImage(element, element.getElementtyp().getTextInput().getInactiveImage(),
				htmlRenderEngine);
		Image imageFocus = createImage(element, element.getElementtyp().getTextInput().getFocusImage(),
				htmlRenderEngine);
		Image imageMouseOverActive = createImage(element,
				element.getElementtyp().getTextInput().getMouseOverActiveImage(), htmlRenderEngine);
		Image imageMouseOverFocus = createImage(element,
				element.getElementtyp().getTextInput().getMouseOverFocusImage(), htmlRenderEngine);
		ImagesTextInput imagesTextInput = new ImagesTextInput(imageView, imageActive, imageInactive, imageFocus,
				imageMouseOverActive, imageMouseOverFocus);
		HtmlInput active = createHtmlInput(element.getElementtyp().getTextInput().getInputActive().getBeforeBase64(),
				element.getElementtyp().getTextInput().getInputActive().getAfterBase64());
		HtmlInput inactive = createHtmlInput(
				element.getElementtyp().getTextInput().getInputInactive().getBeforeBase64(),
				element.getElementtyp().getTextInput().getInputInactive().getAfterBase64());
		HtmlInput focus = createHtmlInput(element.getElementtyp().getTextInput().getInputFocus().getBeforeBase64(),
				element.getElementtyp().getTextInput().getInputFocus().getAfterBase64());
		HtmlInput mouseOverActive = createHtmlInput(
				element.getElementtyp().getTextInput().getInputMouseOverActive().getBeforeBase64(),
				element.getElementtyp().getTextInput().getInputMouseOverActive().getAfterBase64());
		HtmlInput mouseOverFocus = createHtmlInput(
				element.getElementtyp().getTextInput().getInputMouseOverFocus().getBeforeBase64(),
				element.getElementtyp().getTextInput().getInputMouseOverFocus().getAfterBase64());
		TextInputHtmlInput textInputHtmlInput = new TextInputHtmlInput(active, inactive, focus, mouseOverActive,
				mouseOverFocus);
		byte[] cursor = element.getElementtyp().getTextInput().getCursor();
		return new GuiElementTextInput(element.getID(), element.getEbene().intValue(),
				element.getZeichnungsnummer().intValue(), element.getPosition().getXPosition().intValue(),
				element.getPosition().getYPosition().intValue(), element.getPosition().getWidth().intValue(),
				element.getPosition().getHeight().intValue(), imagesTextInput, startState, htmlRenderEngine,
				textInputHtmlInput, element.getElementtyp().getTextInput().getCharacterCount().intValue(), cursor);
	}

	private static State convertStateType(StateType state) {
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

	private static ImageView createImageView(int x, int y, int width, int height) {
		ImageView imageView = new ImageView();
		imageView.setX(x);
		imageView.setY(y);
		imageView.setPreserveRatio(true);
		return imageView;
	}

	private static Image createImage(ElementType element, SourceType sourceType, HtmlRenderEngine htmlRenderEngine)
			throws IOException, SAXException {
		if (sourceType.getURL() != null) {
			String urlString = sourceType.getURL();
			return new Image(htmlRenderEngine.renderPng(urlString, element.getPosition().getWidth().intValue(),
					element.getPosition().getHeight().intValue()));
		}
		if (sourceType.getFile() != null) {
			String file = sourceType.getFile();
			return new Image(htmlRenderEngine.renderPng(new File(file), element.getPosition().getWidth().intValue(),
					element.getPosition().getHeight().intValue()));
		}
		if (sourceType.getHtmlBase64() != null) {
			byte[] dataBytes = sourceType.getHtmlBase64();
			return new Image(htmlRenderEngine.renderPng(dataBytes, element.getPosition().getWidth().intValue(),
					element.getPosition().getHeight().intValue()));
		}
		return null;
	}

	private static HtmlInput createHtmlInput(String base64Before, String base64After) {
		return new HtmlInput(new String(Base64.getDecoder().decode(base64Before)),
				new String(Base64.getDecoder().decode(base64After)));
	}

	private static SliderDragg convertToSliderDragg(SliderDraggType sliderDraggType) {
		if (sliderDraggType == null) {
			return null;
		}
		return new SliderDragg(sliderDraggType.getRangePlus().intValue(), sliderDraggType.getRangeMinus().intValue(),
				sliderDraggType.getStepSize().intValue(), sliderDraggType.getMoveCount().intValue());
	}
}
