package de.platen.cass.guiserver.websocket;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import de.platen.cass.guiserver.Version;
import de.platen.cass.guiserver.exception.GuiServerException;
import de.platen.cass.guiserver.renderer.HtmlRenderEngine;
import de.platen.cass.guiserver.start.GuiServer;
import de.platen.cass.guiserver.start.InfoResolution;
import de.platen.cass.guiserver.schema.GUI;

public class WebSocketGuiServer extends WebSocketServer {

	private static final String XSD_FILENAME = "GUIServer.xsd";

	private final HtmlRenderEngine htmlRenderEngine = new HtmlRenderEngine();
	private final SessionVerwaltung sessionVerwaltung = new SessionVerwaltung();

	public WebSocketGuiServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("onOpen");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("onClose");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("onMessage.");
		if (message.startsWith("get:")) {
			handleGet(conn, message);
		} else {
			handleXml(conn, message);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("onError: " + ex.getMessage());
	}

	@Override
	public void onStart() {
		System.out.println("onStart");
	}

	private void handleGet(WebSocket conn, String message) {
		System.out.println(message);
		String[] parts = message.split(":");
		if (parts.length > 2) {
			if (parts[1].equals(Version.VERSION)) {
				if (parts[2].equals("resolution")) {
					InfoResolution info = new InfoResolution();
					GuiServer.getResolution(info);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					conn.send("info:" + Version.VERSION + ":resolution:X:" + info.getX() + ":Y:"
							+ info.getY());
				} else {
					if (parts[2].equals("session")) {
						conn.send("session:" + Version.VERSION + ":" + this.sessionVerwaltung.requestSession().toString());
					} else {
						this.sendError(conn, "get:unknown");
					}
				}
			} else {
				this.sendError(conn, "get:" + parts[1]);
			}
		} else {
			this.sendError(conn, "get:length");
		}
	}

	private void handleXml(WebSocket conn, String message) {
		try {
			GUI gui = GuiBereitung.bereiteGUI(message.getBytes(),
					this.getClass().getClassLoader().getResourceAsStream(XSD_FILENAME));
			if (gui.getType().getInitialGUI() != null) {
				if (this.sessionVerwaltung.useSession(UUID.fromString(gui.getSessionId()))) {
					System.out.println("InitialGUI.");
					GuiServer.baueInitialGUI(gui, conn, this.htmlRenderEngine);
				} else {
					this.sendError(conn, "session");
				}
			}
			if (gui.getType().getChangeGUI() != null) {
				if (this.sessionVerwaltung.isUsed(UUID.fromString(gui.getSessionId()))) {
					System.out.println("ChangeGUI.");
					GuiServer.changeGUI(gui, conn, this.htmlRenderEngine);
				} else {
					this.sendError(conn, "session");
				}
			}
			if (gui.getType().getExit() != null) {
				if (this.sessionVerwaltung.isUsed(UUID.fromString(gui.getSessionId()))) {
					GuiServer.exit(gui);
				} else {
					this.sendError(conn, "session");
				}
			}
		} catch (GuiServerException e) {
			e.printStackTrace();
			this.sendError(conn, e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.sendError(conn, e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
			this.sendError(conn, e.getMessage());
		}
	}

	private void sendError(WebSocket conn, String message) {
		conn.send("error:" + Version.VERSION + ":" + message);
	}
}
