package de.platen.clapsesy.guiserver.websocket;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import de.platen.cass.guiserver.schema.GUI;

final class GuiBereitung {

	public static GUI bereiteGUI(byte[] xmlDaten, InputStream schema) {
		GUI gui = new GUI();

		try {
			gui = unmarshal(schema, xmlDaten, GUI.class);
		} catch (JAXBException | SAXException e) {
			e.printStackTrace();
		}
		return gui;
	}

	private static <T> T unmarshal(InputStream inputStream, byte[] xmlDaten, Class<T> clss)
			throws JAXBException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		StreamSource streamSource = new StreamSource(inputStream);
		Schema schema = schemaFactory.newSchema(streamSource);
		JAXBContext jaxbContext = JAXBContext.newInstance(clss.getPackage().getName());
		return unmarshal(jaxbContext, schema, xmlDaten, clss);
	}

	private static <T> T unmarshal(JAXBContext jaxbContext, Schema schema, byte[] xmlDaten, Class<T> clss)
			throws JAXBException {
		InputStream is = new ByteArrayInputStream(xmlDaten);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		return clss.cast(unmarshaller.unmarshal(is));
	}
}
