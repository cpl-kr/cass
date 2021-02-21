package de.platen.htmlrenderer;

import java.net.InetSocketAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1111;

    public static void main(final String[] args) throws ParseException, InterruptedException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        final Options options = new Options();
        options.addOption("host", true, "Host");
        options.addOption("port", true, "Port");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("host")) {
            host = cmd.getOptionValue("host");
        }
        if (cmd.hasOption("port")) {
            port = Integer.parseInt(cmd.getOptionValue("port"));
        }
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        final HtmlRenderer htmlRenderer = new HtmlRenderer();
        final Thread thread = new Thread(
                new WebSocketServerHtmlRenderer(new InetSocketAddress(host, port), htmlRenderer));
        thread.start();
        Thread.sleep(3000);
    }
}
