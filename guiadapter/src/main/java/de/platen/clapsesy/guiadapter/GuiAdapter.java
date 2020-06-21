package de.platen.clapsesy.guiadapter;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.java_websocket.client.WebSocketClient;

public class GuiAdapter  {

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 7777;

	public static void main(String[] args) throws URISyntaxException, ParseException {
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
		WebSocketClient client = new WebSocket(new URI("ws://"+ host + ":" + port), host, port);
		client.connect();
	}
}
