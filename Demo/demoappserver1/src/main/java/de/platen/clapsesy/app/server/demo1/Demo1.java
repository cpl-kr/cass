package de.platen.clapsesy.app.server.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.app.event.EventHandlerKey;
import de.platen.clapsesy.app.event.EventHandlerMouse;
import de.platen.clapsesy.app.event.EventHandlerWindow;
import de.platen.clapsesy.app.event.EventMouse;
import de.platen.clapsesy.app.event.EventMouseButton;
import de.platen.clapsesy.app.event.EventType;
import de.platen.clapsesy.app.event.KeyAttribute;
import de.platen.clapsesy.app.event.server.ServerGuiElement;

public class Demo1 {

	private static String path;
	private static final String WINDOW = "window2";

	private static boolean elementDeselectSelected = false;
	private static boolean elementSelectSelected = true;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Parameter für Pfad fehlt.");
			System.exit(1);
		}
		path = args[0];
		List<ServerGuiElement> guiElements = new ArrayList<>();
		Application application = new Application();
		guiElements.add(createGuiElementWindow(application));
		guiElements.add(createGuiElementKey(application, "elementtextinput1"));
		guiElements.add(createGuiElementDeselect(application, "elementdeselect1", EventType.CLICK,
				EventMouseButton.LEFT, 1, new KeyAttribute[0]));
		guiElements.add(createGuiElementSelect(application, "elementselect1", EventType.CLICK, EventMouseButton.LEFT, 1,
				new KeyAttribute[0]));
		guiElements.add(createGuiElementMenu1(application, "elementklick1", EventType.CLICK, EventMouseButton.LEFT, 1,
				new KeyAttribute[0]));
		guiElements.add(createGuiElementSubMenu1(application, "elementklick1-1", EventType.CLICK, EventMouseButton.LEFT,
				1, new KeyAttribute[0]));
		guiElements.add(createGuiElementSubMenu1(application, "elementklick1-2", EventType.CLICK, EventMouseButton.LEFT,
				1, new KeyAttribute[0]));
		WebSocketAppServer webSocketAppServer = new WebSocketAppServer(new InetSocketAddress("localhost", 8888),
				new MessageHandlerServer(path, guiElements));
		webSocketAppServer.start();
	}

	private static ServerGuiElement createGuiElementWindow(Application application) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, null);
		clientGuiElement.registerWindowEvent(new EventHandlerWindow() {

			@Override
			public void handleWindowClosed(WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				byte[] xmlDaten = insertSession(ladeGUIAusDatei(path + "Exit.xml"), sessionId);
				System.out.println("Exit wird gesendet.");
				sendData(insertSession(xmlDaten, sessionId), webSocket);
			}
		});
		return clientGuiElement;
	}

	private static ServerGuiElement createGuiElementKey(Application application, String elementId) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, elementId);
		clientGuiElement.registerKeyEvent(new EventHandlerKey() {

			@Override
			public void handleLine(String line, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Zeile: " + line);
			}

			@Override
			public void handleChar(String c, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Buchstabe: " + c);
			}
		});
		return clientGuiElement;
	}

	private static ServerGuiElement createGuiElementDeselect(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				String filename = "ChangeGUIChangeElementDeselectStateSelected.xml";
				if (elementDeselectSelected) {
					elementDeselectSelected = false;
					filename = "ChangeGUIChangeElementDeselectStateDeselected.xml";
				} else {
					elementDeselectSelected = true;
				}
				byte[] xmlDaten = ladeGUIAusDatei(path + filename);
				System.out.println("ChangeElementStateDeselect wird gesendet.");
				sendData(insertSession(xmlDaten, sessionId), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ServerGuiElement createGuiElementSelect(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				String filename = "ChangeGUIChangeElementSelectStateDeselected.xml";
				if (!elementSelectSelected) {
					elementSelectSelected = true;
					filename = "ChangeGUIChangeElementSelectStateSelected.xml";
				} else {
					elementSelectSelected = false;
				}
				byte[] xmlDaten = ladeGUIAusDatei(path + filename);
				System.out.println("ChangeElementStateSelect wird gesendet.");
				sendData(insertSession(xmlDaten, sessionId), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ServerGuiElement createGuiElementMenu1(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIAddElementMenu.xml");
				System.out.println("ChangeGUIAddElementMenu wird gesendet.");
				sendData(insertSession(xmlDaten, sessionId), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ServerGuiElement createGuiElementSubMenu1(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ServerGuiElement clientGuiElement = new ServerGuiElement(WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIRemoveElementMenu.xml");
				System.out.println("ChangeGUIRemoveElementMenu wird gesendet.");
				sendData(insertSession(xmlDaten, sessionId), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static void sendData(byte[] xmlDaten, WebSocket webSocket) {
		String data = new String(xmlDaten);
		webSocket.send(data);
	}

	private static byte[] ladeGUIAusDatei(String xmlDateiname) {
		byte[] xmlDaten = new byte[0];
		Path path = Paths.get(xmlDateiname);
		try {
			xmlDaten = Files.readAllBytes(path);
		} catch (IOException e) {
			System.out.println("Fehler bei Lesen von Datei.");
			e.printStackTrace();
			return xmlDaten;
		}
		return xmlDaten;
	}

	private static byte[] insertSession(byte[] xmlData, UUID sessionId) {
		String data = new String(xmlData);
		String[] parts = data.split("<SessionId>");
		if (parts.length == 2) {
			String dataToSend = parts[0] + "<SessionId>" + sessionId.toString() + parts[1];
			return dataToSend.getBytes();
		}
		return new byte[0];
	}
}
