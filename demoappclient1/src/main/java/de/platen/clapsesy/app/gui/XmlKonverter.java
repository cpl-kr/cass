package de.platen.clapsesy.app.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import de.platen.clapsesy.app.exception.AppException;
import de.platen.clapsesy.app.schema.GUI;

public class XmlKonverter {

    private static final String XML_CONSTANTS = "http://www.w3.org/2001/XMLSchema";

    public GUI konvertiereXml(final byte[] xmlDaten, final InputStream schema) {
        GUI gui = new GUI();
        try {
            gui = unmarshal(schema, xmlDaten, GUI.class);
        } catch (JAXBException | SAXException e) {
            throw new AppException(e);
        }
        return gui;
    }

    private static <T> T unmarshal(final InputStream inputStream, final byte[] xmlDaten, final Class<T> clss)
            throws JAXBException, SAXException {
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
