package de.platen.clapsesy.app.client.demo2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DemoApp2 {

	private final String path;
	private final WebSocket webSocket;
	private final String session;
	
	private Thread thread;

	public DemoApp2(String path, WebSocket webSocket, String session) {
		this.path = path;
		this.webSocket = webSocket;
		this.session = session;
	}

	public void start() {
		byte[] xmlDaten = ladeGUIAusDatei(path + "InitialGUI.xml");
		sendData(insertSession(xmlDaten, this.session));
		Zeitangabe zeitangabe = new Zeitangabe(path, webSocket, this.session);
		this.thread = new Thread(zeitangabe);
		this.thread.start();
	}
	
	public void stop() {
		this.thread.interrupt();
	}
	
	public void exit() {
		byte[] xmlDaten = ladeGUIAusDatei(path + "Exit.xml");
		sendData(insertSession(xmlDaten, this.session));
	}
	
	private void sendData(byte[] xmlDaten) {
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
	
	private static byte[] insertSession(byte[] xmlData, String session) {
		String data = new String(xmlData);
		String[] parts = data.split("<SessionId>");
		if (parts.length == 2) {
			String dataToSend = parts[0] + "<SessionId>" + session + parts[1];
			return dataToSend.getBytes(); 
		}
		return new byte[0];
	}
}
