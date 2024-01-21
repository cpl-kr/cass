package de.platen.clapsesy.guiserver.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.platen.clapsesy.guiserver.frontend.GuiElementVerwaltungOperation;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import de.platen.clapsesy.guiserver.websocket.WebSocketClientRenderer;
import de.platen.clapsesy.guiserver.websocket.WebSocketGuiServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GuiServer extends Application {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 7777;
    private static final String DEFAULT_HOST_RENDERER = "localhost";
    private static final int DEFAULT_PORT_RENDERER = 1111;
    private static final String XSD_FILENAME = "xsd/GUIServer2.0.0.xsd";

    @Override
    public void start(final Stage stage) throws Exception {
        Platform.setImplicitExit(false);
        // erzeugeStartfenster(200, 100, 400, 200, "GUI-Server", "Bildschubser");
        System.out.println("GUI-Server ist gestartet.");
    }

    public static void main(final String[] args) throws ParseException, URISyntaxException {
        System.out.println("GUI-Server wird gestartet.");
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        String hostRenderer = DEFAULT_HOST_RENDERER;
        int portRenderer = DEFAULT_PORT_RENDERER;
        final Options options = new Options();
        options.addOption("host", true, "Host");
        options.addOption("port", true, "Port");
        options.addOption("hostrenderer", true, "HostRenderer");
        options.addOption("portrenderer", true, "PortRenderer");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("host")) {
            host = cmd.getOptionValue("host");
        }
        if (cmd.hasOption("port")) {
            port = Integer.parseInt(cmd.getOptionValue("port"));
        }
        if (cmd.hasOption("hostrenderer")) {
            hostRenderer = cmd.getOptionValue("hostrenderer");
        }
        if (cmd.hasOption("portrenderer")) {
            portRenderer = Integer.parseInt(cmd.getOptionValue("portrenderer"));
        }
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("HostRenderer: " + hostRenderer);
        System.out.println("PortRenderer: " + portRenderer);
        final WebSocketGuiServer websocketGuiServer = init(host, port, hostRenderer, portRenderer);
        launch(args);
        try {
            websocketGuiServer.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("GUI-Server wurde beendet.");
    }

    private static WebSocketGuiServer init(final String host, final int port, final String hostRenderer,
            final int portRenderer) throws URISyntaxException {
        final URI uri = new URI("ws://" + hostRenderer + ":" + portRenderer);
        final WebSocketClientRenderer webSocketClientRenderer = new WebSocketClientRenderer(uri);
        webSocketClientRenderer.connect();
        final HtmlRenderEngine htmlRenderEngine = new HtmlRenderEngine(webSocketClientRenderer);
        final SessionVerwaltung sessionVerwaltung = new SessionVerwaltung();
        final GuiElementFactory guiElementFactory = new GuiElementFactory();
        final GuiElementVerwaltungOperation elementVerwaltungOperation = new GuiElementVerwaltungOperation();
        final GuiAufbau guiAufbau = new GuiAufbau(guiElementFactory, elementVerwaltungOperation);
        final GuiBehandlung guiBehandlung = new GuiBehandlung(guiElementFactory, guiAufbau, htmlRenderEngine,
                sessionVerwaltung, elementVerwaltungOperation);
        final GuiBereitung guiBereitung = new GuiBereitung(sessionVerwaltung, guiBehandlung, XSD_FILENAME);
        final WebSocketGuiServer websocketGuiServer = new WebSocketGuiServer(new InetSocketAddress(host, port),
                sessionVerwaltung, guiBereitung, guiBehandlung);
        final Thread thread = new Thread(new WebSocketGuiServerThread(websocketGuiServer));
        thread.start();
        return websocketGuiServer;
    }

    // private static Stage erzeugeStartfenster(final int x, final int y, final int breiteScene, final int hoeheScene,
    // final String titel, final String text) {
    // final Text textFeld = new Text(text);
    // textFeld.setFont(Font.font(null, FontWeight.BOLD, 15));
    // textFeld.setFill(Color.CRIMSON);
    // final StackPane layout = new StackPane();
    // layout.getChildren().add(textFeld);
    // final Scene scene = new Scene(layout, breiteScene, hoeheScene);
    // final Stage window = new Stage();
    // window.setTitle(titel);
    // window.setScene(scene);
    // window.setX(x);
    // window.setY(y);
    // window.setIconified(true);
    // window.show();
    // return window;
    // }
}
