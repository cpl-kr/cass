package de.platen.cass.guiappserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.java_websocket.WebSocket;

public class GuiAppServer {

	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 77;

	public static void main(String[] args) throws ParseException, IOException {
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		String appsFile = "";
		Options options = new Options();
		options.addOption("host", true, "Host");
		options.addOption("port", true, "Port");
		options.addOption("appsfile", true, "File with Config for Apps");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("host")) {
			host = cmd.getOptionValue("host");
			System.out.println("Host: " + host);
		}
		if (cmd.hasOption("port")) {
			port = Integer.parseInt(cmd.getOptionValue("port"));
			System.out.println("Port: " + port);
		}
		if (cmd.hasOption("appsfile")) {
			appsFile = cmd.getOptionValue("appsfile");
			System.out.println("Apps: " + appsFile);
		}
		Map<String, String> apps = new HashMap<>();
		List<String> appLines = Files.readAllLines(Paths.get(appsFile));
		for (String appLine : appLines) {
			String[] parts = appLine.split(":");
			if (parts.length > 1) {
				System.out.println("App: " + parts[0]);
				String url = appLine.substring(parts[0].length() + 1);
				System.out.println("URL: " + url);
				apps.put(parts[0], url);
			}
		}
		Map<String, AppConnection> appConnections = new HashMap<>();
		Map<String, WebSocket> clientsessions = new HashMap<>();
		for (Entry<String, String> entry : apps.entrySet()) {
			appConnections.put(entry.getKey(), new AppConnection(URI.create(entry.getValue()), clientsessions));
		}
		for (AppConnection connection : appConnections.values()) {
			connection.open();
		}
		InetSocketAddress address = new InetSocketAddress(host, port);
		WebSocketAppServer webSocketAppServer = new WebSocketAppServer(address, appConnections, clientsessions);
		webSocketAppServer.run();
	}

}
