package de.platen.clapsesy.guiserver.websocket;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import de.platen.clapsesy.guiserver.Version;
import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.start.GuiBehandlung;
import de.platen.clapsesy.guiserver.start.GuiBereitung;
import de.platen.clapsesy.guiserver.start.InfoResolution;
import de.platen.clapsesy.guiserver.start.SessionVerwaltung;

public class WebSocketGuiServer extends WebSocketServer {

    private final SessionVerwaltung sessionVerwaltung;
    private final GuiBereitung guiBereitung;
    private final GuiBehandlung guiBehandlung;

    public WebSocketGuiServer(final InetSocketAddress address, final SessionVerwaltung sessionVerwaltung,
            final GuiBereitung guiBereitung, final GuiBehandlung guiBehandlung) {
        super(address);
        this.sessionVerwaltung = sessionVerwaltung;
        this.guiBereitung = guiBereitung;
        this.guiBehandlung = guiBehandlung;
    }

    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        System.out.println("onOpen");
    }

    @Override
    public void onClose(final WebSocket conn, final int code, final String reason, final boolean remote) {
        System.out.println("onClose");
    }

    @Override
    public void onMessage(final WebSocket conn, final String message) {
        System.out.println("onMessage.");
        if (message.startsWith("get:")) {
            handleGet(conn, message);
        } else {
            if (message.startsWith("set:")) {
                handleSet(conn, message);
            } else {
                System.out.println("Xml:");
                System.out.println(message);
                handleXml(conn, message);
            }
        }
    }

    @Override
    public void onError(final WebSocket conn, final Exception ex) {
        System.out.println("onError: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("onStart");
    }

    private void handleGet(final WebSocket conn, final String message) {
        System.out.println(message);
        final String[] parts = message.split(":");
        if (parts.length > 2) {
            if (parts[1].equals(Version.VERSION)) {
                if (parts[2].equals("resolution")) {
                    final InfoResolution info = new InfoResolution();
                    guiBehandlung.getResolution(info);
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    conn.send("info:" + Version.VERSION + ":resolution:X:" + info.getX() + ":Y:" + info.getY());
                } else {
                    if (parts[2].equals("session")) {
                        conn.send("session:" + Version.VERSION + ":" + sessionVerwaltung.requestSession().toString());
                    } else {
                        sendError(conn, "get:unknown");
                    }
                }
            } else {
                sendError(conn, "get:" + parts[1]);
            }
        } else {
            sendError(conn, "get:length");
        }
    }

    private void handleSet(final WebSocket conn, final String message) {
        System.out.println(message);
        final String[] parts = message.split(":");
        if (parts.length > 6) {
            if (parts[1].equals(Version.VERSION)) {
                if (parts[2].equals("session")) {
                    if (parts[4].equals("path")) {
                        final String session = parts[3];
                        final String path = parts[5];
                        final String fileSeparator = "" + File.separatorChar;
                        try {
                            if (!path.endsWith(fileSeparator)) {
                                sendError(conn, "set:path:" + path);
                            } else {
                                if (!sessionVerwaltung.setPath(UUID.fromString(session), path)) {
                                    sendError(conn, "set:path:" + path);
                                }
                            }
                        } catch (final IllegalArgumentException e) {
                            sendError(conn, "set:session:" + session);
                        }
                    } else {
                        sendError(conn, "set:path");
                    }
                } else {
                    sendError(conn, "set:session");
                }
            } else {
                sendError(conn, "set:version");
            }
        } else {
            sendError(conn, "set:length");
        }
    }

    private void handleXml(final WebSocket conn, final String message) {
        try {
            guiBereitung.bereiteGui(message.getBytes(), conn);
        } catch (final GuiServerException e) {
            e.printStackTrace();
            sendError(conn, e.getMessage());
        } catch (final RuntimeException e) {
            e.printStackTrace();
            sendError(conn, e.getMessage());
        } catch (final Throwable e) {
            e.printStackTrace();
            sendError(conn, e.getMessage());
        }
    }

    private void sendError(final WebSocket conn, final String message) {
        conn.send("error:" + Version.VERSION + ":" + message);
    }
}
