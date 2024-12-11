package de.platen.clapsesy.app.client.demo2;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;

public class Demo2 {

	public static void main(String[] args) throws URISyntaxException {
		if (args.length < 1) {
			System.out.println("Parameter für Pfad fehlt.");
			System.exit(1);
		}
		String path = args[0];
		WebSocketClient client = new WebSocket(path, new URI("ws://localhost:7777"));
		client.connect();
	}
}
