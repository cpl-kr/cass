package de.platen.clapsesy.app.client.demo2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Zeitangabe implements Runnable {

	private static final String DATEI_STUNDE_ZEHNERSTELLE = "StundeZehnerstelle.xml";
	private static final String DATEI_STUNDE_EINERSTELLE = "StundeEinerstelle.xml";
	private static final String DATEI_MINUTE_ZEHNERSTELLE = "MinuteZehnerstelle.xml";
	private static final String DATEI_MINUTE_EINERSTELLE = "MinuteEinerstelle.xml";
	private static final String DATEI_SEKUNDE_ZEHNERSTELLE = "SekundeZehnerstelle.xml";
	private static final String DATEI_SEKUNDE_EINERSTELLE = "SekundeEinerstelle.xml";

	private final WebSocket websocket;
	private final byte[] xmlStundeZehnerstelle;
	private final byte[] xmlStundeEinerstelle;
	private final byte[] xmlMinuteZehnerstelle;
	private final byte[] xmlMinuteEinerstelle;
	private final byte[] xmlSekundeZehnerstelle;
	private final byte[] xmlSekundeEinerstelle;
	private String zeitStundeZehnerstelle = "0";
	private String zeitStundeEinerstelle = "0";
	private String zeitMinuteZehnerstelle = "0";
	private String zeitMinuteEinerstelle = "0";
	private String zeitSekundeZehnerstelle = "0";
	private String zeitSekundeEinerstelle = "0";

	public Zeitangabe(String path, WebSocket websocketDemo, String session) {
		this.websocket = websocketDemo;
		xmlStundeZehnerstelle = insertSession(ladeGUIAusDatei(path + DATEI_STUNDE_ZEHNERSTELLE), session);
		xmlStundeEinerstelle = insertSession(ladeGUIAusDatei(path + DATEI_STUNDE_EINERSTELLE), session);
		xmlMinuteZehnerstelle = insertSession(ladeGUIAusDatei(path + DATEI_MINUTE_ZEHNERSTELLE), session);
		xmlMinuteEinerstelle = insertSession(ladeGUIAusDatei(path + DATEI_MINUTE_EINERSTELLE), session);
		xmlSekundeZehnerstelle = insertSession(ladeGUIAusDatei(path + DATEI_SEKUNDE_ZEHNERSTELLE), session);
		xmlSekundeEinerstelle = insertSession(ladeGUIAusDatei(path + DATEI_SEKUNDE_EINERSTELLE), session);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (!Thread.currentThread().isInterrupted()) {
			SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
			String dateString = sd.format(new Date());
			System.out.println("Uhrzeit: " + dateString);
			this.bearbeiteZeitangabe(dateString);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void bearbeiteZeitangabe(String dateString) {
		String aktuell = "0";
		aktuell = dateString.substring(0, 1);
		zeitStundeZehnerstelle = this.bearbeiteStelle(zeitStundeZehnerstelle, aktuell, this.xmlStundeZehnerstelle);
		aktuell = dateString.substring(1, 2);
		zeitStundeEinerstelle = this.bearbeiteStelle(zeitStundeEinerstelle, aktuell, this.xmlStundeEinerstelle);
		aktuell = dateString.substring(3, 4);
		zeitMinuteZehnerstelle = this.bearbeiteStelle(zeitMinuteZehnerstelle, aktuell, this.xmlMinuteZehnerstelle);
		aktuell = dateString.substring(4, 5);
		zeitMinuteEinerstelle = this.bearbeiteStelle(zeitMinuteEinerstelle, aktuell, this.xmlMinuteEinerstelle);
		aktuell = dateString.substring(6, 7);
		zeitSekundeZehnerstelle = this.bearbeiteStelle(zeitSekundeZehnerstelle, aktuell, this.xmlSekundeZehnerstelle);
		aktuell = dateString.substring(7, 8);
		zeitSekundeEinerstelle = this.bearbeiteStelle(zeitSekundeEinerstelle, aktuell, this.xmlSekundeEinerstelle);
	}

	private String bearbeiteStelle(String vorher, String aktuell, byte[] xml) {
		if (!vorher.equals(aktuell)) {
			String daten = new String(xml);
			String anzeige = daten.replace("ZifferX.html", "Ziffer" + aktuell + ".html");
			this.websocket.send(anzeige);
		}
		return aktuell;
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
