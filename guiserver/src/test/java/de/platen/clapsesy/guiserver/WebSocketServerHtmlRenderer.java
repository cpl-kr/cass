package de.platen.clapsesy.guiserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketServerHtmlRenderer extends WebSocketServer {

    public WebSocketServerHtmlRenderer(final InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        System.out.println("WebSocketServerHtmlRenderer: onOpen");
    }

    @Override
    public void onClose(final WebSocket conn, final int code, final String reason, final boolean remote) {
        System.out.println("WebSocketServerHtmlRenderer: onClose");
    }

    @Override
    public void onMessage(final WebSocket conn, final String message) {
        System.out.println("WebSocketServerHtmlRenderer: onMessage.");
        final File file = new File("src/test/resources/bild.png");
        FileInputStream fis = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(file);
            data = new byte[(int) file.length()];
            fis.read(data);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (final IOException e) {
            }
        }
        conn.send(ByteBuffer.wrap(data));
    }

    @Override
    public void onError(final WebSocket conn, final Exception ex) {
        System.out.println("WebSocketServerHtmlRenderer: onError: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocketServerHtmlRenderer: onStart");
    }
}
