package de.platen.clapsesy.app.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import de.platen.clapsesy.app.ElementId;
import de.platen.clapsesy.app.Version;
import de.platen.clapsesy.app.exception.AppException;
import de.platen.clapsesy.app.schema.GUI;
import de.platen.clapsesy.app.schema.GUIType;

public class GuiFactory {

    private final XmlGenerator xmlGenerator;
    private final XmlKonverter xmlKonverter;
    private final InputStream schema;

    public GuiFactory(final XmlGenerator xmlGenerator, final XmlKonverter xmlKonverter, final String xsdFilename) {
        this.xmlGenerator = xmlGenerator;
        this.xmlKonverter = xmlKonverter;
        schema = this.getClass().getClassLoader().getResourceAsStream(xsdFilename);
    }

    public String baueInitialGui(final UUID sessionId, final String dateiname) {
        final GUI gui = xmlKonverter.konvertiereXml(leseAusDatei(dateiname), schema);
        gui.setSessionId(sessionId.toString());
        return xmlGenerator.generiereGUI(gui);
    }

    public String baueChangeGui(final UUID sessionId, final ElementId elementId) {
        final GUIType guiType = new GUIType();
        final GUI gui = new GUI();
        gui.setSessionId(sessionId.toString());
        gui.setVersion(Version.VERSION);
        gui.setType(guiType);
        return xmlGenerator.generiereGUI(gui);
    }

    public String baueExit(final UUID sessionId) {
        final GUIType guiType = new GUIType();
        guiType.setExit(new Object());
        final GUI gui = new GUI();
        gui.setSessionId(sessionId.toString());
        gui.setVersion(Version.VERSION);
        gui.setType(guiType);
        return xmlGenerator.generiereGUI(gui);
    }

    private byte[] leseAusDatei(final String dateiname) {
        final InputStream datei = this.getClass().getClassLoader().getResourceAsStream(dateiname);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            datei.transferTo(byteArrayOutputStream);
        } catch (final IOException e) {
            throw new AppException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
