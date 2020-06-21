package de.platen.clapsesy.guiserver.frontend.guielement;

import java.io.IOException;

import de.platen.clapsesy.guiserver.frontend.eventhandler.EventHandlerMouse;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiElementBrowser extends GuiElement {

	private final String stringUrl;

	private enum Os {
		WIN, MAC, LIN, UNKNOWN
    }

	public GuiElementBrowser(String id, int ebene, int zeichnungsnummer, int x, int y, int width, int height,
			String stringUrl) {
		super(id, ebene, zeichnungsnummer, x, y, width, height);
		this.stringUrl = stringUrl;
	}

	public void start() {
		Os os = this.getOs();
		try {
			switch (os) {
			case WIN:
				new ProcessBuilder("cmd", "/c", "start", this.stringUrl).start();
				break;
			case MAC:
				new ProcessBuilder("open", this.stringUrl).start();
				break;
			case LIN:
				// TODO
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setImage(View view, Image image) {
	}

	@Override
	public void handleNewActualState(State state) {
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

	@Override
	public void registerEventsToSend(Event event) {
	}

	@Override
	public void deregisterEventsToSend(Event event) {
	}

	private Os getOs() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("win")) {
			return Os.WIN;
		}
		if (os.startsWith("mac")) {
			return Os.MAC;
		}
		return Os.LIN;
	}
}
