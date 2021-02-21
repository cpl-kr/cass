package de.platen.clapsesy.guiserver.start;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.java_websocket.WebSocket;
import org.xml.sax.SAXException;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.schema.GUI;

public final class GuiBereitung {

    private static final String XML_CONSTANTS = "http://www.w3.org/2001/XMLSchema";

    private final SessionVerwaltung sessionVerwaltung;
    private final GuiBehandlung guiBehandlung;
    private final String xsdFilename;

    public GuiBereitung(final SessionVerwaltung sessionVerwaltung, final GuiBehandlung guiBehandlung,
            final String xsdFilename) {
        this.sessionVerwaltung = sessionVerwaltung;
        this.guiBehandlung = guiBehandlung;
        this.xsdFilename = xsdFilename;
    }

    public void bereiteGui(final byte[] xmlDaten, final WebSocket conn) {
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xsdFilename);
        if (inputStream == null) {
            throw new GuiServerException("XSD-Datei konnte nicht gelesen werden.");
        }
        final GUI gui = bereiteGUI(xmlDaten, inputStream);
        if (gui.getType().getInitialGUI() != null) {
            if (sessionVerwaltung.useSession(UUID.fromString(gui.getSessionId()))) {
                System.out.println("InitialGUI.");
                guiBehandlung.baueInitialGUI(gui, conn);
            } else {
                throw new GuiServerException("session");
            }
        }
        if (gui.getType().getChangeGUI() != null) {
            if (sessionVerwaltung.isUsed(UUID.fromString(gui.getSessionId()))) {
                System.out.println("ChangeGUI.");
                guiBehandlung.changeGUI(gui, conn);
            } else {
                throw new GuiServerException("session");
            }
        }
        if (gui.getType().getExit() != null) {
            if (sessionVerwaltung.isUsed(UUID.fromString(gui.getSessionId()))) {
                guiBehandlung.exit(gui);
            } else {
                throw new GuiServerException("session");
            }
        }
    }

    private static GUI bereiteGUI(final byte[] xmlDaten, final InputStream schema) {
        GUI gui = new GUI();
        try {
            gui = unmarshal(schema, xmlDaten, GUI.class);
        } catch (JAXBException | SAXException e) {
            throw new GuiServerException(e);
        }
        return gui;
    }

    private static <T> T unmarshal(final InputStream inputStream, final byte[] xmlDaten, final Class<T> clss)
            throws JAXBException, SAXException {
        // final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XML_CONSTANTS);
        final StreamSource streamSource = new StreamSource(inputStream);
        final Schema schema = schemaFactory.newSchema(streamSource);
        final JAXBContext jaxbContext = JAXBContext.newInstance(clss.getPackage().getName());
        return unmarshal(jaxbContext, schema, xmlDaten, clss);
    }

    private static <T> T unmarshal(final JAXBContext jaxbContext, final Schema schema, final byte[] xmlDaten,
            final Class<T> clss) throws JAXBException {
        final InputStream is = new ByteArrayInputStream(xmlDaten);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return clss.cast(unmarshaller.unmarshal(is));
    }
}
