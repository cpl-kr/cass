package de.platen.clapsesy.app.client.demo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.platen.clapsesy.app.Version;
import de.platen.clapsesy.app.event.client.ClientGuiElement;

public class MessageHandlerClient {

	private final  String path;
	private final List<ClientGuiElement> guiElements;
	
	public MessageHandlerClient(String path, List<ClientGuiElement> guiElements) {
		this.path = path;
		this.guiElements = guiElements;
	}
	
	public void sendGetSession(WebSocketAppClient webSocket) {
		String getSession = "get:" + Version.VERSION + ":session";
		sendData(getSession.getBytes(), webSocket);
	}
	
	public void sendInitialGui(WebSocketAppClient webSocket, String session) {
		byte[] xmlDaten = ladeGUIAusDatei(this.path + "InitialGUI.xml");
		String data = new String(xmlDaten);
		String[] parts = data.split("<SessionId>");
		if (parts.length == 2) {
			String dataToSend = parts[0] + "<SessionId>" + session + parts[1];
			sendData(dataToSend.getBytes(), webSocket);
		}
	}

	public void handleMessage(String message, WebSocketAppClient webSocket) {
		for (ClientGuiElement clientGuiElement : this.guiElements) {
			clientGuiElement.handleMessage(message);
		}
	}

	private static void sendData(byte[] xmlDaten, WebSocketAppClient webSocket) {
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

}
