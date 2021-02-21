package de.platen.clapsesy.app.client.demo1;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import de.platen.clapsesy.app.Version;
import de.platen.clapsesy.app.gui.GuiFactory;

public class WebSocketAppClient extends WebSocketClient {

    private final MessageHandlerClient messageHandler;
    private final GuiFactory guiElementFactory;
    private final String initialGuiDateiname;

    public WebSocketAppClient(final URI serverUri, final Draft draft, final MessageHandlerClient messageHandler,
            final GuiFactory guiElementFactory, final String initialGuiDateiname) {
        super(serverUri, draft);
        this.messageHandler = messageHandler;
        this.guiElementFactory = guiElementFactory;
        this.initialGuiDateiname = initialGuiDateiname;
    }

    public WebSocketAppClient(final URI serverURI, final MessageHandlerClient messageHandler,
            final GuiFactory guiElementFactory, final String initialGuiDateiname) {
        super(serverURI);
        this.messageHandler = messageHandler;
        this.guiElementFactory = guiElementFactory;
        this.initialGuiDateiname = initialGuiDateiname;
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        System.out.println("new connection opened");
        messageHandler.sendGetSession(this);
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(final String message) {
        System.out.println("received message: " + message);
        if (message.startsWith("app:" + Version.VERSION)) {
            messageHandler.handleMessage(message, this);
        }
        if (message.startsWith("session:" + Version.VERSION)) {
            final String[] parts = message.split(":");
            if (parts.length == 3) {
                final UUID sessionId = UUID.fromString(parts[2]);
                final String xml = guiElementFactory.baueInitialGui(sessionId, initialGuiDateiname);
                messageHandler.sendInitialGui(this, xml);
            }
        }
    }

    @Override
    public void onMessage(final ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(final Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}
