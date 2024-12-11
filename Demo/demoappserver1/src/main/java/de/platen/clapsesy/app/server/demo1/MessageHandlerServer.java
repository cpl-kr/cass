package de.platen.clapsesy.app.server.demo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.platen.clapsesy.app.event.server.ServerGuiElement;

public class MessageHandlerServer {

	private final String path;
	
	private final List<ServerGuiElement> guiElements;
	
	public MessageHandlerServer(String path, List<ServerGuiElement> guiElements) {
		this.path = path;
		this.guiElements = guiElements;
	}
	
	public void sendInitial(WebSocket webSocket, UUID sessionId) {
		byte[] xmlDaten = insertSession(ladeGUIAusDatei(path + "InitialGUI.xml"), sessionId);
		sendData(xmlDaten, webSocket);
	}

	public void handleMessage(String message, WebSocket webSocket, UUID sessionId) {
		for (ServerGuiElement clientGuiElement : this.guiElements) {
			clientGuiElement.handleMessage(message, webSocket, sessionId);
		}
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
