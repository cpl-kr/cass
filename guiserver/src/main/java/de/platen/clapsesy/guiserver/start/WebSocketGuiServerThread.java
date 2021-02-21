package de.platen.clapsesy.guiserver.start;

import de.platen.clapsesy.guiserver.websocket.WebSocketGuiServer;

public class WebSocketGuiServerThread implements Runnable {

    private final WebSocketGuiServer webSocketGuiServer;

    public WebSocketGuiServerThread(final WebSocketGuiServer webSocketGuiServer) {
        this.webSocketGuiServer = webSocketGuiServer;
    }

    @Override
    public void run() {
        webSocketGuiServer.run();
    }

}
