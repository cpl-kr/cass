package de.platen.clapsesy.guiserver.start;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.frontend.ApplicationWindow;
import de.platen.clapsesy.guiserver.frontend.guielement.ElementPosition;
import de.platen.clapsesy.guiserver.frontend.guielement.Event;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElement;
import de.platen.clapsesy.guiserver.frontend.guielement.GuiElementFactory;
import de.platen.clapsesy.guiserver.frontend.guielement.State;
import de.platen.clapsesy.guiserver.frontend.guielement.View;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.websocket.WebSocketGuiServer;
import de.platen.clapsesy.guiserver.websocket.WebSocketGuiServerThread;
import de.platen.cass.guiserver.schema.ChangeElementType;
import de.platen.cass.guiserver.schema.ElementOperationType;
import de.platen.cass.guiserver.schema.ElementType;
import de.platen.cass.guiserver.schema.GUI;
import de.platen.cass.guiserver.schema.OperationType;
import de.platen.cass.guiserver.schema.SourceType;
import de.platen.cass.guiserver.schema.StateType;
import de.platen.cass.guiserver.schema.ViewType;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.java_websocket.WebSocket;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GuiServer extends Application {

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 7777;
	private static final Map<String, List<ApplicationWindow>> APPLICATIONWINDOWS = new HashMap<>();

	@Override
	public void start(Stage stage) throws Exception {
		Platform.setImplicitExit(false);
		// erzeugeStartfenster(200, 100, 400, 200, "GUI-Server", "Bildschubser");
		System.out.println("GUI-Server ist gestartet.");
	}

	public synchronized static void baueInitialGUI(GUI gui, WebSocket connection, HtmlRenderEngine htmlRenderEngine) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String sessionId = gui.getSessionId();
				if (gui.getType().getInitialGUI() != null) {
					List<ApplicationWindow> windows = GuiAufbau.baueFenster(gui, sessionId, connection,
							htmlRenderEngine);
					APPLICATIONWINDOWS.put(sessionId, windows);
					for (ApplicationWindow window : windows) {
						window.show();
					}
					System.out.println("Initial-GUI ist aufgebaut.");
				}
			}
		};
		Platform.runLater(runnable);
	}

	public synchronized static void changeGUI(GUI gui, WebSocket connection, HtmlRenderEngine htmlRenderEngine) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String sessionId = gui.getSessionId();
				if (gui.getType().getChangeGUI() != null) {
					List<OperationType> operations = gui.getType().getChangeGUI().getOperation();
					for (OperationType operation : operations) {
						if (operation.getRemoveElement() != null) {
							System.out.println("removeElement.");
							removeElement(sessionId, operation);
						}
						if (operation.getRemoveWindow() != null) {
							System.out.println("removeWindow.");
							removeWindow(sessionId, operation);
						}
						if (operation.getAddElement() != null) {
							System.out.println("addElement.");
							addElement(sessionId, operation, htmlRenderEngine);
						}
						if (operation.getAddWindow() != null) {
							System.out.println("addWindow.");
							addWindow(sessionId, operation, htmlRenderEngine);
						}
						if (operation.getChangeElement() != null) {
							System.out.println("changeElement.");
							changeElement(sessionId, operation, htmlRenderEngine);
						}
					}
				}
			}

			private void removeElement(String sessionId, OperationType operation) {
				String windowId = operation.getRemoveElement().getWindowID();
				String elementId = operation.getRemoveElement().getElementID();
				List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
				if (windows != null) {
					boolean found = false;
					for (ApplicationWindow window : windows) {
						if (window.getId().equals(windowId)) {
							found = true;
							window.removeElement(elementId);
						}
					}
					if (!found) {
						throw new GuiServerException("Element not found.");
					}
				} else {
					throw new GuiServerException("Element not found.");
				}
			}

			private void removeWindow(String sessionId, OperationType operation) {
				String windowId = operation.getRemoveWindow().getWindowID();
				List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
				if (windows != null) {
					ApplicationWindow windowToRemove = null;
					for (ApplicationWindow window : windows) {
						if (window.getId().equals(windowId)) {
							if (windowToRemove == null) {
								window.close();
								windowToRemove = window;
							}
						}
					}
					if (windowToRemove != null) {
						windows.remove(windowToRemove);
						if (windows.isEmpty()) {
							APPLICATIONWINDOWS.remove(sessionId);
						}
					} else {
						throw new GuiServerException("Window not found.");
					}
				} else {
					throw new GuiServerException("Window not found.");
				}
			}

			private void addElement(String sessionId, OperationType operation, HtmlRenderEngine htmlRenderEngine) {
				String windowId = operation.getAddElement().getWindowID();
				ElementType element = operation.getAddElement().getElement();
				List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
				if (windows != null) {
					GuiElement guiElement = GuiElementFactory.createGuiElement(element, htmlRenderEngine);
					for (ApplicationWindow window : windows) {
						if (window.getId().equals(windowId)) {
							window.addElement(guiElement);
						}
					}
				} else {
					throw new GuiServerException("Window not found.");
				}
			}

			private void addWindow(String sessionId, OperationType operation, HtmlRenderEngine htmlRenderEngine) {
				ApplicationWindow applicationWindow = GuiAufbau.baueEinzelfenster(sessionId, connection,
						operation.getAddWindow().getWindow(), htmlRenderEngine);
				List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
				if (windows != null) {
					windows.add(applicationWindow);
				} else {
					List<ApplicationWindow> windowList = new ArrayList<>();
					windowList.add(applicationWindow);
					APPLICATIONWINDOWS.put(sessionId, windowList);
				}
				applicationWindow.show();
			}

			private void changeElement(String sessionId, OperationType operation, HtmlRenderEngine htmlRenderEngine) {
				boolean found = false;
				ChangeElementType changeElementType = operation.getChangeElement();
				String windowId = changeElementType.getWindowID();
				String elementId = changeElementType.getElementID();
				ElementOperationType elementOperationType = changeElementType.getOperation();
				if (elementOperationType.getChangeState() != null) {
					StateType stateType = elementOperationType.getChangeState().getState();
					List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
					if (windows != null) {
						for (ApplicationWindow window : windows) {
							if (window.getId().equals(windowId)) {
								found = true;
								State state = GuiAufbau.convertToState(stateType);
								window.changeElementState(elementId, state);
							}
						}
					}
				}
				if (elementOperationType.getReload() != null) {
					ViewType viewType = elementOperationType.getReload().getView();
					List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
					if (windows != null) {
						for (ApplicationWindow window : windows) {
							if (window.getId().equals(windowId)) {
								found = true;
								View view = GuiAufbau.convertToView(viewType);
								ElementPosition elementPosition = window.getElementPosition(elementId);
								if (elementPosition != null) {
									try {
										Image image = renderImage(elementOperationType.getReload().getImage(),
												elementPosition.getWidth(), elementPosition.getHeight(),
												htmlRenderEngine);
										window.setElementImage(elementId, view, image);
									} catch (GuiServerException e) {
										System.out.println("Fehler bei Reload.");
									}
								}
							}
						}
					}
				}
				if (elementOperationType.getEvent() != null) {
					List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
					if (windows != null) {
						for (ApplicationWindow window : windows) {
							if (window.getId().equals(windowId)) {
								found = true;
								Event event = GuiElementFactory
										.convertToEvent(elementOperationType.getEvent().getType());
								window.setEvent(elementId, event, elementOperationType.getEvent().isActive());
							}
						}
					}
				}
				if (!found) {
					throw new GuiServerException("Element not found.");
				}
			}

			private Image renderImage(SourceType sourceType, int width, int height, HtmlRenderEngine htmlRenderEngine) {
				InputStream inputStream = null;
				try {
					if (sourceType.getURL() != null) {
						String url = sourceType.getURL();
						inputStream = htmlRenderEngine.renderPng(url, width, height);
					}
					if (sourceType.getFile() != null) {
						String file = sourceType.getFile();
						inputStream = htmlRenderEngine.renderPng(new File(file), width, height);
					}
					if (sourceType.getHtmlBase64() != null) {
						byte[] dataBytes = sourceType.getHtmlBase64();
						inputStream = htmlRenderEngine.renderPng(dataBytes, width, height);
						return new Image(inputStream);
					}
				} catch (IOException | SAXException e) {
					throw new GuiServerException(e);
				}
				if (inputStream != null) {
					return new Image(inputStream);
				}
				return null;
			}

		};
		Platform.runLater(runnable);
	}

	public synchronized static void exit(GUI gui) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String sessionId = gui.getSessionId();
				if (gui.getType().getExit() != null) {
					List<ApplicationWindow> windows = APPLICATIONWINDOWS.get(sessionId);
					for (ApplicationWindow window : windows) {
						window.close();
					}
					APPLICATIONWINDOWS.remove(sessionId);
					System.out.println("Exit.");
				}
			}
		};
		Platform.runLater(runnable);
	}

	public synchronized static void getResolution(InfoResolution info) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				double width = screenSize.getWidth();
				double height = screenSize.getHeight();
				info.setX((int) width);
				info.setY((int) height);
			}
		};
		Platform.runLater(runnable);
	}

	public static void main(String[] args) throws ParseException {
		System.out.println("GUI-Server wird gestartet.");
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		Options options = new Options();
		options.addOption("host", true, "Host");
		options.addOption("port", true, "Port");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("host")) {
			host = cmd.getOptionValue("host");
		}
		if (cmd.hasOption("port")) {
			port = Integer.parseInt(cmd.getOptionValue("port"));
		}
		System.out.println("Host: " + host);
		System.out.println("Port: " + port);
		WebSocketGuiServer websocketGuiServer = new WebSocketGuiServer(new InetSocketAddress(host, port));
		Thread thread = new Thread(new WebSocketGuiServerThread(websocketGuiServer));
		thread.start();
		launch(args);
		try {
			websocketGuiServer.stop();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("GUI-Server wurde beendet.");
	}

	private static Stage erzeugeStartfenster(int x, int y, int breiteScene, int hoeheScene, String titel, String text) {
		Text textFeld = new Text(text);
		textFeld.setFont(Font.font(null, FontWeight.BOLD, 15));
		textFeld.setFill(Color.CRIMSON);
		StackPane layout = new StackPane();
		layout.getChildren().add(textFeld);
		Scene scene = new Scene(layout, breiteScene, hoeheScene);
		Stage window = new Stage();
		window.setTitle(titel);
		window.setScene(scene);
		window.setX(x);
		window.setY(y);
		window.setIconified(true);
		window.show();
		return window;
	}
}
