package de.platen.clapsesy.guiserver;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import org.apache.commons.cli.ParseException;
import org.junit.Ignore;
import org.junit.Test;

public class IntegrationTest {

    @Test
    @Ignore
    public void testOk() throws InterruptedException {
        final Thread threadHtmlRenderer = new Thread(
                new WebSocketServerHtmlRenderer(new InetSocketAddress("localhost", 1111)));
        threadHtmlRenderer.start();
        Thread.sleep(3000);
        final GuiServer guiServer = new GuiServer();
        final Thread threadGuiServer = new Thread(guiServer);
        threadGuiServer.start();
        // TODO Breakpoint in folgender Zeile und Client separat starten
        Thread.sleep(3000);
    }

    private class GuiServer implements Runnable {

        @Override
        public void run() {
            final String[] args = new String[0];
            try {
                Main.main(args);
            } catch (ParseException | URISyntaxException e) {
            }
        }
    }
}
