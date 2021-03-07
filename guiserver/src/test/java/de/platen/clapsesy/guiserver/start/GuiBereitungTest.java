package de.platen.clapsesy.guiserver.start;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.junit.Test;
import org.mockito.Mockito;

import de.platen.clapsesy.guiserver.start.GuiBehandlung;
import de.platen.clapsesy.guiserver.start.GuiBereitung;
import de.platen.clapsesy.guiserver.start.SessionVerwaltung;

public class GuiBereitungTest {

    @Test
    public void testOk() throws IOException {
        final SessionVerwaltung sessionVerwaltung = Mockito.mock(SessionVerwaltung.class);
        Mockito.when(sessionVerwaltung.useSession(Mockito.any(UUID.class))).thenReturn(true);
        final GuiBehandlung guiBehandlung = Mockito.mock(GuiBehandlung.class);
        final String xsdFilename = "xsd/GuiServer2.0.0.xsd";
        final GuiBereitung guiBereitung = new GuiBereitung(sessionVerwaltung, guiBehandlung, xsdFilename);
        final WebSocket webSocket = Mockito.mock(WebSocket.class);
        final byte[] xml = leseDatei("src/test/resources/InitialGui.xml");
        guiBereitung.bereiteGui(xml, webSocket);
    }

    private static byte[] leseDatei(final String dateiname) throws IOException {
        byte[] daten = new byte[0];
        FileInputStream fis = null;
        ByteArrayOutputStream fos = null;
        try {
            fis = new FileInputStream(dateiname);
            fos = new ByteArrayOutputStream();
            int b = 0;
            boolean ende = false;
            while (!ende) {
                b = fis.read();
                if (b != -1) {
                    fos.write(b);
                } else {
                    ende = true;
                }
            }
            fos.flush();
            daten = fos.toByteArray();
        } catch (final IOException ex) {
            System.out.println(ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (final IOException ex) {
                    throw ex;
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException ex) {
                    throw ex;
                }
            }
        }
        return daten;
    }
}
