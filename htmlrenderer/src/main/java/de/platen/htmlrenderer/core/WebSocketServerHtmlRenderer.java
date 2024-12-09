package de.platen.htmlrenderer.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;

import de.platen.htmlrenderer.api.HtmlRenderer;
import de.platen.syntaxparser.Parser;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import org.apache.commons.codec.binary.Base64;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import static java.util.Objects.requireNonNull;

public class WebSocketServerHtmlRenderer extends WebSocketServer {

    private static final String VERSION = "1.0.0";
    private static final String BREITE = "Breite";
    private static final String URL = "Url";
    public static final String WEB_SOCKET_SERVER_HTML_RENDERER_ON_CLOSE = "WebSocketServerHtmlRenderer: onClose";
    public static final String WEB_SOCKET_SERVER_HTML_RENDERER_ON_MESSAGE = "WebSocketServerHtmlRenderer: onMessage.";
    public static final String WEB_SOCKET_SERVER_HTML_RENDERER_ON_ERROR = "WebSocketServerHtmlRenderer: onError: ";
    public static final String WEB_SOCKET_SERVER_HTML_RENDERER_ON_START = "WebSocketServerHtmlRenderer: onStart";
    public static final String WEB_SOCKET_SERVER_HTML_RENDERER_ON_OPEN = "WebSocketServerHtmlRenderer: onOpen";
    public static final String TEXT_IST_LEER_ODER_ENTHAELT_NUR_WHITESPACES = "Text ist leer oder enthält nur Whitespaces.";
    public static final String FEHLER_BEIM_PARSEN = "Fehler beim Parsen.";
    public static final String VERSION_IST_NICHT_1_0_0 = "Version ist nicht 1.0.0.";
    public static final String FEHLER_BEIM_RENDERN_BEI_URL_ERGEBNIS_NULL = "Fehler beim Rendern bei Url, (ergebnis null)";
    public static final String FEHLER_BEIM_RENDERN_BEI_URL = "Fehler beim Rendern bei Url: ";
    public static final String FEHLER_BEIM_RENDERN_BEI_HTML_ERGEBNIS_NULL = "Fehler beim Rendern bei html, (ergebnis null)";
    public static final String FEHLER_BEIM_RENDERN_BEI_HTML = "Fehler beim Rendern bei html: ";

    private final HtmlRenderer htmlRenderer;
    private final Parser parser;

    public WebSocketServerHtmlRenderer(final InetSocketAddress address, final HtmlRenderer htmlRenderer, final Parser parser) {
        super(requireNonNull(address));
        this.htmlRenderer = requireNonNull(htmlRenderer);
        this.parser = requireNonNull(parser);
    }

    @Override
    public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
        System.out.println(WEB_SOCKET_SERVER_HTML_RENDERER_ON_OPEN);
    }

    @Override
    public void onClose(final WebSocket conn, final int code, final String reason, final boolean remote) {
        System.out.println(WEB_SOCKET_SERVER_HTML_RENDERER_ON_CLOSE);
    }

    @Override
    public void onMessage(final WebSocket conn, final String message) {
        System.out.println(WEB_SOCKET_SERVER_HTML_RENDERER_ON_MESSAGE);
        bearbeiteMessage(conn, message);
    }

    @Override
    public void onError(final WebSocket conn, final Exception ex) {
        System.out.println(WEB_SOCKET_SERVER_HTML_RENDERER_ON_ERROR + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println(WEB_SOCKET_SERVER_HTML_RENDERER_ON_START);
    }

    private void bearbeiteMessage(WebSocket conn, String message) {
        if (message.isBlank()) {
            conn.send(TEXT_IST_LEER_ODER_ENTHAELT_NUR_WHITESPACES);
            return;
        }
        for (char c : message.toCharArray()) {
            if (!this.parser.verarbeiteZeichen(c)) {
                conn.send(FEHLER_BEIM_PARSEN);
                return;
            }
        }
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = parser.ermittleSyntaxpfadeMitWort(true);
        if (!VERSION.equals(syntaxpfadeMitWort.get(0).getWort())) {
            conn.send(VERSION_IST_NICHT_1_0_0);
            return;
        }
        String wort1 = syntaxpfadeMitWort.get(1).getWort();
        String wort2 = syntaxpfadeMitWort.get(2).getWort();
        int breite;
        int hoehe;
        if (BREITE.equals(syntaxpfadeMitWort.get(1).getSyntaxpfad().gebeKnotenfolge().get(2).getSymbolbezeichnung().getSymbolbezeichnung())) {
            breite = Integer.parseInt(wort1);
            hoehe = Integer.parseInt(wort2);
        } else {
            hoehe = Integer.parseInt(wort1);
            breite = Integer.parseInt(wort2);
        }
        behandleRendern(conn, syntaxpfadeMitWort, breite, hoehe);
    }

    private void behandleRendern(WebSocket conn, List<SyntaxpfadMitWort> syntaxpfadeMitWort, int breite, int hoehe) {
        String wort = syntaxpfadeMitWort.get(3).getWort();
        String url = "";
        String html = "";
        if (URL.equals(syntaxpfadeMitWort.get(3).getSyntaxpfad().gebeKnotenfolge().get(2).getSymbolbezeichnung().getSymbolbezeichnung())) {
            url = wort;
        } else {
            html = wort;
        }
        if (!url.isEmpty()) {
            renderUrl(conn, url, breite, hoehe);
        } else {
            renderHtml(conn, html, breite, hoehe);
        }
    }

    private void renderUrl(WebSocket conn, String url, int breite, int hoehe) {
        try {
            final byte[] bild = this.htmlRenderer.renderPng(url, breite, hoehe);
            if (bild != null) {
                conn.send(ByteBuffer.wrap(bild));
            } else {
                conn.send(FEHLER_BEIM_RENDERN_BEI_URL_ERGEBNIS_NULL);
            }
        } catch (IOException e) {
            conn.send(FEHLER_BEIM_RENDERN_BEI_URL + e.getMessage());
        }
    }

    private void renderHtml(WebSocket conn, String html, int breite, int hoehe) {
        final Base64 base64 = new Base64();
        final byte[] data = base64.decode(html);
        try {
            final byte[] bild = this.htmlRenderer.renderPng(data, breite, hoehe);
            if (bild != null) {
                conn.send(ByteBuffer.wrap(bild));
            } else {
                conn.send(FEHLER_BEIM_RENDERN_BEI_HTML_ERGEBNIS_NULL);
            }
        } catch (final IOException e) {
            conn.send(FEHLER_BEIM_RENDERN_BEI_HTML + e.getMessage());
        }
    }
}
