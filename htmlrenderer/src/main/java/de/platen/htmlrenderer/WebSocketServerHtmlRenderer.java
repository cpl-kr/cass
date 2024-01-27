package de.platen.htmlrenderer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Base64;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

public class WebSocketServerHtmlRenderer extends WebSocketServer {

    private static final String VERSION = "1.0.0";
    private static final String JSON_VERSION = "version";
    private static final String JSON_BREITE = "breite";
    private static final String JSON_HOEHE = "hoehe";
    private static final String JSON_URL = "url";
    private static final String JSON_XML = "xml";

    private final HtmlRenderer htmlRenderer;

    public WebSocketServerHtmlRenderer(final InetSocketAddress address, final HtmlRenderer htmlRenderer) {
        super(address);
        this.htmlRenderer = htmlRenderer;
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
        try {
            final JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.has(JSON_VERSION)) {
                final String version = (String) jsonObject.get(JSON_VERSION);
                if (VERSION.equals(version)) {
                    if (jsonObject.has(JSON_BREITE) && jsonObject.has(JSON_HOEHE)) {
                        final int breite = (int) jsonObject.get(JSON_BREITE);
                        final int hoehe = (int) jsonObject.get(JSON_HOEHE);
                        if (jsonObject.has(JSON_URL) && jsonObject.has(JSON_XML)) {
                            conn.send("Fehler durch sich auschließende Url und Xml");
                        } else {
                            if (!jsonObject.has(JSON_URL) && !jsonObject.has(JSON_XML)) {
                                conn.send("Fehler durch fehlende Url und Xml");
                            } else {
                                if (jsonObject.has(JSON_URL)) {
                                    final String url = (String) jsonObject.get(JSON_URL);
                                    try {
                                        final byte[] bild = htmlRenderer.renderPng(url, breite, hoehe);
                                        if (bild != null) {
                                            conn.send(ByteBuffer.wrap(bild));
                                        } else {
                                            conn.send("Fehler beim Rendern bei Url, (ergebnis null)");
                                        }
                                    } catch (final IOException e) {
                                        conn.send("Fehler beim Rendern bei Url: " + e.getMessage());
                                    }
                                }
                                if (jsonObject.has(JSON_XML)) {
                                    final String xml = (String) jsonObject.get(JSON_XML);
                                    final Base64 base64 = new Base64();
                                    final byte[] data = base64.decode(xml);
                                    try {
                                        final byte[] bild = htmlRenderer.renderPng(data, breite, hoehe);
                                        if (bild != null) {
                                            conn.send(ByteBuffer.wrap(bild));
                                        } else {
                                            conn.send("Fehler beim Rendern bei Xml, (ergebnis null)");
                                        }
                                    } catch (final IOException e) {
                                        conn.send("Fehler beim Rendern bei Xml: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    } else {
                        conn.send("Fehler durch fehlende Breite oder Hoehe");
                    }
                } else {
                    conn.send("Fehler durch falsche Version");
                }
            } else {
                conn.send("Fehler durch fehlende Version");
            }
        } catch (final RuntimeException e) {
            conn.send("Sonstiger Fehler: " + e.getMessage());
        }
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
