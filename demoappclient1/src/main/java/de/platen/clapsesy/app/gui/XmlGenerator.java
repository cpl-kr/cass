package de.platen.clapsesy.app.gui;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.platen.clapsesy.app.exception.AppException;
import de.platen.clapsesy.app.schema.GUI;

public class XmlGenerator {

    public String generiereGUI(final GUI gui) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(GUI.class);
            final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            final Writer writer = new StringWriter();
            jaxbMarshaller.marshal(gui, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new AppException(e);
        }
    }
}
