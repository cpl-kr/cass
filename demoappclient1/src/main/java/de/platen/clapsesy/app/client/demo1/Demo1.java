package de.platen.clapsesy.app.client.demo1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.platen.clapsesy.app.event.client.ClientGuiElement;
import de.platen.clapsesy.app.gui.GuiFactory;
import de.platen.clapsesy.app.gui.XmlGenerator;
import de.platen.clapsesy.app.gui.XmlKonverter;

public class Demo1 {

    private static String path;
    private static final String URI = "ws://localhost:7777";
    private static final String XSD_FILENAME = "xsd/GUIServer2.0.0.xsd";
    private static final String INITIAL_GUI_DATEINAME = "InitialGUI.xml";

    public static void main(final String[] args) throws URISyntaxException {
        // if (args.length < 1) {
        // System.out.println("Parameter für Pfad fehlt.");
        // System.exit(1);
        // }
        path = args[0];
        final List<ClientGuiElement> guiElements = new ArrayList<>();
        final Application application = new Application();
        final GuiFactory guiFactory = new GuiFactory(new XmlGenerator(), new XmlKonverter(), XSD_FILENAME);
        final WebSocketAppClient client = new WebSocketAppClient(new URI(URI),
                new MessageHandlerClient(path, guiElements), guiFactory, INITIAL_GUI_DATEINAME);
        client.connect();
    }
}
