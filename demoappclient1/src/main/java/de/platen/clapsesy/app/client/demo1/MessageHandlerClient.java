package de.platen.clapsesy.app.client.demo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.platen.clapsesy.app.Version;
import de.platen.clapsesy.app.event.client.ClientGuiElement;

public class MessageHandlerClient {

    private final String path;
    private final List<ClientGuiElement> guiElements;

    public MessageHandlerClient(final String path, final List<ClientGuiElement> guiElements) {
        this.path = path;
        this.guiElements = guiElements;
    }

    public void sendGetSession(final WebSocketAppClient webSocket) {
        final String getSession = "get:" + Version.VERSION + ":session";
        sendData(getSession.getBytes(), webSocket);
    }

    public void sendInitialGui(final WebSocketAppClient webSocket, final String xmlDaten) {
        sendData(xmlDaten.getBytes(), webSocket);
    }

    public void handleMessage(final String message, final WebSocketAppClient webSocket) {
        for (final ClientGuiElement clientGuiElement : guiElements) {
            clientGuiElement.handleMessage(message);
        }
    }

    private static void sendData(final byte[] xmlDaten, final WebSocketAppClient webSocket) {
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
}
