package de.platen.cass.app.client.demo1;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.cass.app.event.EventHandlerWindow;
import de.platen.cass.app.event.EventHandlerMouse;
import de.platen.cass.app.event.EventMouse;
import de.platen.cass.app.event.EventHandlerKey;
import de.platen.cass.app.event.EventMouseButton;
import de.platen.cass.app.event.EventType;
import de.platen.cass.app.event.KeyAttribute;
import de.platen.cass.app.event.client.ClientGuiElement;
import de.platen.cass.app.event.client.SessionHolder;
import de.platen.cass.app.event.client.WebSocketHolder;

public class Demo1 {

	private static String path;
	private static final String URI = "ws://localhost:7777";
	private static final String WINDOW = "window2";
	private static final SessionHolder sessionHolder = new SessionHolder();
	private static final WebSocketHolder webSocketHolder = new WebSocketHolder();

	private static boolean elementDeselectSelected = false;
	private static boolean elementSelectSelected = true;

	public static void main(String[] args) throws URISyntaxException {
		if (args.length < 1) {
			System.out.println("Parameter für Pfad fehlt.");
			System.exit(1);
		}
		path = args[0];
		List<ClientGuiElement> guiElements = new ArrayList<>();
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
		WebSocketAppClient client = new WebSocketAppClient(new URI(URI), new MessageHandlerClient(path, guiElements),
				sessionHolder);
		webSocketHolder.setWebSocket(client);
		client.connect();
	}

	private static ClientGuiElement createGuiElementWindow(Application application) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, null);
		clientGuiElement.registerWindowEvent(new EventHandlerWindow() {

			@Override
			public void handleWindowClosed(org.java_websocket.WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				byte[] xmlDaten = ladeGUIAusDatei(path + "Exit.xml");
				System.out.println("Exit wird gesendet.");
				sendData(insertSession(xmlDaten), webSocket);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Programm wird beendet.");
				System.exit(0);
			}
		});
		return clientGuiElement;
	}

	private static ClientGuiElement createGuiElementKey(Application application, String elementId) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, elementId);
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

	private static ClientGuiElement createGuiElementDeselect(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, elementId);
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
				sendData(insertSession(xmlDaten), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ClientGuiElement createGuiElementSelect(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, elementId);
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
				sendData(insertSession(xmlDaten), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ClientGuiElement createGuiElementMenu1(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIAddElementMenu.xml");
				System.out.println("ChangeGUIAddElementMenu wird gesendet.");
				sendData(insertSession(xmlDaten), webSocket);
			}
		}, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
		clientGuiElement.registerMouseEvent(clientEventMouse);
		return clientGuiElement;
	}

	private static ClientGuiElement createGuiElementSubMenu1(Application application, String elementId,
			EventType clientEventType, EventMouseButton clientEventMouseButton, int clickCount,
			KeyAttribute[] clientKeyAttribute) {
		ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW, elementId);
		EventMouse clientEventMouse = new EventMouse(new EventHandlerMouse() {

			@Override
			public void handleEvent(int x, int y, WebSocket webSocket, UUID sessionId) {
				application.doSomething();
				System.out.println("Maus-Event.");
				byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIRemoveElementMenu.xml");
				System.out.println("ChangeGUIRemoveElementMenu wird gesendet.");
				sendData(insertSession(xmlDaten), webSocket);
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

	private static byte[] insertSession(byte[] xmlData) {
		String data = new String(xmlData);
		String[] parts = data.split("<SessionId>");
		if (parts.length == 2) {
			String dataToSend = parts[0] + "<SessionId>" + sessionHolder.getSession() + parts[1];
			return dataToSend.getBytes();
		}
		return new byte[0];
	}

}
