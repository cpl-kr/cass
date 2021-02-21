package de.platen.clapsesy.app.event.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.app.ElementId;
import de.platen.clapsesy.app.client.demo1.Application;
import de.platen.clapsesy.app.event.EventHandlerKey;
import de.platen.clapsesy.app.event.EventMouse;
import de.platen.clapsesy.app.event.EventMouseButton;
import de.platen.clapsesy.app.event.EventType;
import de.platen.clapsesy.app.event.KeyAttribute;

public class ElementFactory {

    private static final String WINDOW = "window2";

    private static boolean elementDeselectSelected = false;
    private static boolean elementSelectSelected = true;

    private final List<ClientGuiElement> guiElements;
    private final String pfad;
    private final Application application;

    public ElementFactory(final List<ClientGuiElement> guiElements, final String pfad, final Application application) {
        this.guiElements = guiElements;
        this.pfad = pfad;
        this.application = application;
    }

    private void baueGuiElemente(final UUID sessionId, final WebSocket webSocket) {
        // guiElements.add(createGuiElementWindow(application));
        guiElements.add(createGuiElementKey(application, new ElementId("elementtextinput1"), sessionId, webSocket));
        guiElements.add(createGuiElementDeselect(application, new ElementId("elementdeselect1"), EventType.CLICK,
                EventMouseButton.LEFT, 1, new KeyAttribute[0], sessionId, webSocket, pfad));
        // guiElements.add(createGuiElementSelect(application, "elementselect1", EventType.CLICK, EventMouseButton.LEFT,
        // 1,
        // new KeyAttribute[0]));
        // guiElements.add(createGuiElementMenu1(application, "elementklick1", EventType.CLICK, EventMouseButton.LEFT,
        // 1,
        // new KeyAttribute[0]));
        // guiElements.add(createGuiElementSubMenu1(application, "elementklick1-1", EventType.CLICK,
        // EventMouseButton.LEFT,
        // 1, new KeyAttribute[0]));
        // guiElements.add(createGuiElementSubMenu1(application, "elementklick1-2", EventType.CLICK,
        // EventMouseButton.LEFT,
        // 1, new KeyAttribute[0]));
    }

    // private static ClientGuiElement createGuiElementWindow(final Application application, final UUID sessionId, final
    // WebSocket webSocket) {
    // final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionId, webSocket, WINDOW, null);
    // clientGuiElement.registerWindowEvent(sessionId.toString()) -> {
    // application.doSomething();
    // final byte[] xmlDaten = ladeGUIAusDatei(pfad + "Exit.xml");
    // System.out.println("Exit wird gesendet.");
    // sendData(insertSession(xmlDaten), webSocket);
    // try {
    // Thread.sleep(3000);
    // } catch (final InterruptedException e) {
    // e.printStackTrace();
    // }
    // System.out.println("Programm wird beendet.");
    // System.exit(0);
    // });
    // return clientGuiElement;
    // }

    private static ClientGuiElement createGuiElementKey(final Application application, final ElementId elementId,
            final UUID sessionId, final WebSocket webSocket) {
        final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionId, webSocket, WINDOW, elementId);
        clientGuiElement.registerKeyEvent(new EventHandlerKey() {

            @Override
            public void handleLine(final String line, final WebSocket webSocket, final UUID sessionId) {
                System.out.println("Zeile: " + line);
                application.doSomething();
            }

            @Override
            public void handleChar(final String c, final WebSocket webSocket, final UUID sessionId) {
                System.out.println("Buchstabe: " + c);
                application.doSomething();
            }
        });
        return clientGuiElement;
    }

    private static ClientGuiElement createGuiElementDeselect(final Application application, final ElementId elementId,
            final EventType clientEventType, final EventMouseButton clientEventMouseButton, final int clickCount,
            final KeyAttribute[] clientKeyAttribute, final UUID sessionId, final WebSocket webSocket,
            final String pfad) {
        final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionId, webSocket, WINDOW, elementId);
        final EventMouse clientEventMouse = new EventMouse((x, y, webSocket1, sessionId1) -> {
            application.doSomething();
            System.out.println("Maus-Event.");
            String filename = "ChangeGUIChangeElementDeselectStateSelected.xml";
            if (elementDeselectSelected) {
                elementDeselectSelected = false;
                filename = "ChangeGUIChangeElementDeselectStateDeselected.xml";
            } else {
                elementDeselectSelected = true;
            }
            final byte[] xmlDaten = ladeGUIAusDatei(pfad + filename);
            System.out.println("ChangeElementStateDeselect wird gesendet.");
            sendData(insertSession(xmlDaten, sessionId1.toString()), webSocket1);
        }, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);

        clientGuiElement.registerMouseEvent(clientEventMouse);
        return clientGuiElement;
    }

    // private static ClientGuiElement createGuiElementSelect(final Application application, final String elementId,
    // final EventType clientEventType, final EventMouseButton clientEventMouseButton, final int clickCount,
    // final KeyAttribute[] clientKeyAttribute) {
    // final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW,
    // elementId);
    // final EventMouse clientEventMouse = new EventMouse((x, y, webSocket, sessionId) -> {
    // application.doSomething();
    // System.out.println("Maus-Event.");
    // String filename = "ChangeGUIChangeElementSelectStateDeselected.xml";
    // if (!elementSelectSelected) {
    // elementSelectSelected = true;
    // filename = "ChangeGUIChangeElementSelectStateSelected.xml";
    // } else {
    // elementSelectSelected = false;
    // }
    // final byte[] xmlDaten = ladeGUIAusDatei(path + filename);
    // System.out.println("ChangeElementStateSelect wird gesendet.");
    // sendData(insertSession(xmlDaten), webSocket);
    // }, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
    // clientGuiElement.registerMouseEvent(clientEventMouse);
    // return clientGuiElement;
    // }

    // private static ClientGuiElement createGuiElementMenu1(final Application application, final String elementId,
    // final EventType clientEventType, final EventMouseButton clientEventMouseButton, final int clickCount,
    // final KeyAttribute[] clientKeyAttribute) {
    // final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW,
    // elementId);
    // final EventMouse clientEventMouse = new EventMouse((x, y, webSocket, sessionId) -> {
    // application.doSomething();
    // System.out.println("Maus-Event.");
    // final byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIAddElementMenu.xml");
    // System.out.println("ChangeGUIAddElementMenu wird gesendet.");
    // sendData(insertSession(xmlDaten), webSocket);
    // }, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
    // clientGuiElement.registerMouseEvent(clientEventMouse);
    // return clientGuiElement;
    // }

    // private static ClientGuiElement createGuiElementSubMenu1(final Application application, final String elementId,
    // final EventType clientEventType, final EventMouseButton clientEventMouseButton, final int clickCount,
    // final KeyAttribute[] clientKeyAttribute) {
    // final ClientGuiElement clientGuiElement = new ClientGuiElement(sessionHolder, webSocketHolder, WINDOW,
    // elementId);
    // final EventMouse clientEventMouse = new EventMouse((x, y, webSocket, sessionId) -> {
    // application.doSomething();
    // System.out.println("Maus-Event.");
    // final byte[] xmlDaten = ladeGUIAusDatei(path + "ChangeGUIRemoveElementMenu.xml");
    // System.out.println("ChangeGUIRemoveElementMenu wird gesendet.");
    // sendData(insertSession(xmlDaten), webSocket);
    // }, clientEventType, clientEventMouseButton, clickCount, clientKeyAttribute);
    // clientGuiElement.registerMouseEvent(clientEventMouse);
    // return clientGuiElement;
    // }

    private static void sendData(final byte[] xmlDaten, final WebSocket webSocket) {
        final String data = new String(xmlDaten);
        webSocket.send(data);
    }

    private static byte[] ladeGUIAusDatei(final String xmlDateiname) {
        byte[] xmlDaten = new byte[0];
        final Path path = Paths.get(xmlDateiname);
        try {
            xmlDaten = Files.readAllBytes(path);
        } catch (final IOException e) {
            System.out.println("Fehler bei Lesen von Datei.");
            e.printStackTrace();
            return xmlDaten;
        }
        return xmlDaten;
    }

    private static byte[] insertSession(final byte[] xmlData, final String sessionId) {
        final String data = new String(xmlData);
        final String[] parts = data.split("<SessionId>");
        if (parts.length == 2) {
            final String dataToSend = parts[0] + "<SessionId>" + sessionId + parts[1];
            return dataToSend.getBytes();
        }
        return new byte[0];
    }
}
