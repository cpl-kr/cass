package de.platen.htmlrenderer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Base64;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WebSocketServerHtmlRendererTest {

    private static final String URI = "ws://localhost:1111";
    private static final String HOST = "localhost";
    private static final int PORT = 1111;

    private WebSocketServerHtmlRenderer webSocketServerHtmlRenderer;
    private Thread thread;
    private final HtmlRenderer htmlRenderer = Mockito.mock(HtmlRenderer.class);
    private WebSocketClientHtmlRenderer client;

    @Before
    public void start() throws InterruptedException, URISyntaxException {
        Mockito.reset(htmlRenderer);
        webSocketServerHtmlRenderer = new WebSocketServerHtmlRenderer(new InetSocketAddress(HOST, PORT), htmlRenderer);
        thread = new Thread(new WebSocketServerHtmlRendererThread(webSocketServerHtmlRenderer));
        thread.start();
        Thread.sleep(1000);
        client = new WebSocketClientHtmlRenderer(new URI(URI));
        client.connect();
        Thread.sleep(1000);
    }

    @After
    public void stop() throws IOException, InterruptedException {
        client.close();
        Thread.sleep(1000);
        webSocketServerHtmlRenderer.stop();
        thread.interrupt();
        Thread.sleep(1000);
    }

    @Test
    public void testOnMessageStringUrl() throws IOException, InterruptedException {
        final byte[] ergebnis = { 0x01, 0x02, 0x03 };
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test"), Mockito.eq(200), Mockito.eq(100))).thenReturn(ergebnis);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getFehlertext());
        assertArrayEquals(ergebnis, client.getDaten());
    }

    @Test
    public void testOnMessageStringXml() throws IOException, InterruptedException {
        final byte[] ergebnis = { 0x01, 0x02, 0x03 };
        final Base64 base64 = new Base64();
        final String xml = base64.encodeToString("test".getBytes());
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test".getBytes()), Mockito.eq(200), Mockito.eq(100)))
                .thenReturn(ergebnis);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("xml", xml);
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getFehlertext());
        assertArrayEquals(ergebnis, client.getDaten());
    }

    @Test
    public void testOnMessageStringUrlUndXml() throws URISyntaxException, InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        jsonObject.put("xml", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch sich auschließende Url und Xml", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringFalscheVersion() throws InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "0.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        jsonObject.put("xml", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch falsche Version", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringVersionFehlt() throws InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        jsonObject.put("xml", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch fehlende Version", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringBreiteFehlt() throws InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        jsonObject.put("xml", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch fehlende Breite oder Hoehe", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringHoeheFehlt() throws InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("url", "test");
        jsonObject.put("xml", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch fehlende Breite oder Hoehe", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringUrlUndXmlFehlen() throws InterruptedException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler durch fehlende Url und Xml", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringUrlRenderPngNull() throws IOException, InterruptedException {
        final byte[] ergebnis = null;
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test"), Mockito.eq(200), Mockito.eq(100))).thenReturn(ergebnis);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler beim Rendern bei Url, (ergebnis null)", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringXmlRenderPngNull() throws IOException, InterruptedException {
        final byte[] ergebnis = null;
        final Base64 base64 = new Base64();
        final String xml = base64.encodeToString("test".getBytes());
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test".getBytes()), Mockito.eq(200), Mockito.eq(100)))
                .thenReturn(ergebnis);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("xml", xml);
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertEquals("Fehler beim Rendern bei Xml, (ergebnis null)", client.getFehlertext());
    }

    @Test
    public void testOnMessageStringUrlRenderPngIOException() throws IOException, InterruptedException {
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test"), Mockito.eq(200), Mockito.eq(100)))
                .thenThrow(new IOException());
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertTrue(client.getFehlertext().startsWith("Fehler beim Rendern bei Url: "));
    }

    @Test
    public void testOnMessageStringXmlRenderPngIOException() throws IOException, InterruptedException {
        final Base64 base64 = new Base64();
        final String xml = base64.encodeToString("test".getBytes());
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test".getBytes()), Mockito.eq(200), Mockito.eq(100)))
                .thenThrow(new IOException());
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("xml", xml);
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertTrue(client.getFehlertext().startsWith("Fehler beim Rendern bei Xml: "));
    }

    @Test
    public void testOnMessageStringRenderPngRuntimeException() throws IOException, InterruptedException {
        Mockito.when(htmlRenderer.renderPng(Mockito.eq("test"), Mockito.eq(200), Mockito.eq(100)))
                .thenThrow(new RuntimeException());
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "1.0.0");
        jsonObject.put("breite", 200);
        jsonObject.put("hoehe", 100);
        jsonObject.put("url", "test");
        client.send(jsonObject.toString());
        Thread.sleep(1000);
        assertNull(client.getDaten());
        assertTrue(client.getFehlertext().startsWith("Sonstiger Fehler: "));
    }

    private class WebSocketServerHtmlRendererThread implements Runnable {

        private final WebSocketServerHtmlRenderer webSocketServerHtmlRenderer;

        public WebSocketServerHtmlRendererThread(final WebSocketServerHtmlRenderer webSocketServerHtmlRenderer) {
            this.webSocketServerHtmlRenderer = webSocketServerHtmlRenderer;
        }

        @Override
        public void run() {
            webSocketServerHtmlRenderer.run();
        }
    }

    private class WebSocketClientHtmlRenderer extends WebSocketClient {

        private String fehlertext = null;
        private byte[] daten = null;

        public WebSocketClientHtmlRenderer(final URI serverUri, final Draft draft) {
            super(serverUri, draft);
        }

        public WebSocketClientHtmlRenderer(final URI serverURI) {
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
            fehlertext = message;
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

        public String getFehlertext() {
            return fehlertext;
        }

        public byte[] getDaten() {
            return daten;
        }
    }
}
