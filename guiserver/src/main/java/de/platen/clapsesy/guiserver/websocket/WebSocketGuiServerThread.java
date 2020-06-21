package de.platen.clapsesy.guiserver.websocket;

public class WebSocketGuiServerThread implements Runnable {

	private final WebSocketGuiServer webSocketGuiServer;
	
	public WebSocketGuiServerThread(WebSocketGuiServer webSocketGuiServer) {
		this.webSocketGuiServer = webSocketGuiServer;
	}
	
	@Override
	public void run() {
		webSocketGuiServer.run();
	}

}
