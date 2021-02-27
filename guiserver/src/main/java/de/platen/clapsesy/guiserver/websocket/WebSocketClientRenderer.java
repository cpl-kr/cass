package de.platen.clapsesy.guiserver.websocket;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import de.platen.clapsesy.guiserver.exception.GuiServerException;

public class WebSocketClientRenderer extends WebSocketClient {

    private byte[] daten = null;

    public WebSocketClientRenderer(final URI serverUri, final Draft draft) {
        super(serverUri, draft);
    }

    public WebSocketClientRenderer(final URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(final String message) {
        System.out.println("received message: " + message);
    }

    @Override
    public void onMessage(final ByteBuffer message) {
        System.out.println("received ByteBuffer");
        daten = message.array();
    }

    @Override
    public void onError(final Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public byte[] holeErgebnis(final int timeoutMilliSec) {
        if (daten == null) {
            try {
                Thread.sleep(timeoutMilliSec);
            } catch (final InterruptedException e) {
                throw new GuiServerException(e);
            }
            if (daten == null) {
                throw new GuiServerException("Daten sind nach Timeout immer noh null");
            }
        }
        System.out.print("Daten geholt");
        final byte[] datenKopie = new byte[daten.length];
        System.arraycopy(daten, 0, datenKopie, 0, daten.length);
        daten = null;
        return datenKopie;
    }
}
