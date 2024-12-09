package de.platen.htmlrenderer.core;

import de.platen.syntaxparser.Parser;

import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebSocketServerHtmlRendererTest {

    private static final String EINGABE_FALSCHE_VERSION = "Version 1.1.1 Breite 10 Höhe 20 Html " + Base64.getEncoder().encodeToString("<html></html>".getBytes());
    private static final String EINGABE_HTML = "Version 1.0.0 Breite 10 Höhe 20 Html " + Base64.getEncoder().encodeToString("<html></html>".getBytes());
    private static final String EINGABE_URL = "Version 1.0.0 Breite 10 Höhe 20 Url http://localhost/test.html";

    private WebSocketServerHtmlRenderer webSocketServerHtmlRenderer;
    private final InetSocketAddress inetSocketAddress = mock(InetSocketAddress.class);
    private final HtmlRendererImpl htmlRenderer = mock(HtmlRendererImpl.class);
    private final Parser parser = mock(Parser.class);
    private final WebSocket webSocket = mock(WebSocket.class);

    @BeforeEach
    void init() {
        this.webSocketServerHtmlRenderer = new WebSocketServerHtmlRenderer(this.inetSocketAddress, this.htmlRenderer, this.parser);
    }

    @Test
    void testKonstruktor() {
        assertThrows(NullPointerException.class, () -> new WebSocketServerHtmlRenderer(null, this.htmlRenderer, this.parser));
        assertThrows(NullPointerException.class, () -> new WebSocketServerHtmlRenderer(this.inetSocketAddress, null, this.parser));
        assertThrows(NullPointerException.class, () -> new WebSocketServerHtmlRenderer(this.inetSocketAddress, this.htmlRenderer, null));
        assertDoesNotThrow(() -> new WebSocketServerHtmlRenderer(this.inetSocketAddress, this.htmlRenderer, this.parser));
    }

    @Test
    void testOnMessageMessageEmpty() {
        this.webSocketServerHtmlRenderer.onMessage(this.webSocket, "");
        verify(this.webSocket).send("Text ist leer oder enthält nur Whitespaces.");
    }

    @Test
    void testOnMessageMessageWhitespace() {
        this.webSocketServerHtmlRenderer.onMessage(this.webSocket, " ");
        verify(this.webSocket).send("Text ist leer oder enthält nur Whitespaces.");
    }

    @Test
    void testOnMessageMessageParserFehler() {
        when(this.parser.verarbeiteZeichen(anyChar())).thenReturn(false);
        this.webSocketServerHtmlRenderer.onMessage(this.webSocket, "xxx");
        verify(this.webSocket).send("Fehler beim Parsen.");
    }

    @Test
    void testFalscheVersion() {
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = ermittleSyntaxpfade(EINGABE_FALSCHE_VERSION);
        when(this.parser.verarbeiteZeichen(anyChar())).thenReturn(true);
        when(this.parser.ermittleSyntaxpfadeMitWort(true)).thenReturn(syntaxpfadeMitWort);
        this.webSocketServerHtmlRenderer.onMessage(this.webSocket, EINGABE_FALSCHE_VERSION);
        verify(this.webSocket).send("Version ist nicht 1.0.0.");
    }

    // TODO Test Render Url IOException
    // TODO Test Render Url Ergebnis null
    // TODO Test Render Url mit Ergebnis
    // TODO Test Render Html IOException
    // TODO Test Render Html Ergebnis null
    // TODO Test Render Html mit Ergebnis

    private static List<SyntaxpfadMitWort> ermittleSyntaxpfade(final String eingabe) {
        Parsererzeugung parsererzeugung = new Parsererzeugung();
        Parser parser = parsererzeugung.erzeugeParser();
        for (char c : eingabe.toCharArray()) {
            parser.verarbeiteZeichen(c);
        }
        return parser.ermittleSyntaxpfadeMitWort(true);
    }
}
