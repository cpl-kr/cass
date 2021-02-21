package de.platen.clapsesy.guiserver.renderer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import de.platen.clapsesy.guiserver.websocket.WebSocketClientRenderer;

public class HtmlRenderEngine {

    private static final String VERSION_RENDERER = "1.0.0";
    private static final String JSON_KEY_VERSION = "version";
    private static final String JSON_KEY_BREITE = "breite";
    private static final String JSON_KEY_HOEHE = "hoehe";
    private static final String JSON_KEY_URL = "url";
    private static final String JSON_KEY_XML = "xml";
    private static final int TIMEOUT_MILLISEC = 1000;

    private final WebSocketClientRenderer webSocketClientRenderer;

    public HtmlRenderEngine(final WebSocketClientRenderer webSocketClientRenderer) {
        this.webSocketClientRenderer = webSocketClientRenderer;
    }

    public InputStream renderPng(String urlString, final int width, final int height) {
        if (!urlString.startsWith("http:") //
                && !urlString.startsWith("https:") //
                && !urlString.startsWith("ftp:") //
                && !urlString.startsWith("file:")) {
            urlString = "http://" + urlString;
        }
        final JSONObject jsonObject = erzeugeJSONObject(width, height);
        jsonObject.put(JSON_KEY_URL, urlString);
        webSocketClientRenderer.send(jsonObject.toString());
        return holeErgebnis();
    }

    public InputStream renderPng(final File file, final int width, final int height) throws IOException {
        final byte[] data = Files.readAllBytes(file.toPath());
        return this.renderPng(data, width, height);
    }

    public InputStream renderPng(final byte[] data, final int width, final int height) {
        final Base64 base64 = new Base64();
        final String dataBase64 = base64.encodeToString(data);
        final JSONObject jsonObject = erzeugeJSONObject(width, height);
        jsonObject.put(JSON_KEY_XML, dataBase64);
        webSocketClientRenderer.send(jsonObject.toString());
        return holeErgebnis();
    }

    private JSONObject erzeugeJSONObject(final int breite, final int hoehe) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_VERSION, VERSION_RENDERER);
        jsonObject.put(JSON_KEY_BREITE, breite);
        jsonObject.put(JSON_KEY_HOEHE, hoehe);
        return jsonObject;
    }

    private InputStream holeErgebnis() {
        final byte[] ergebnis = webSocketClientRenderer.holeErgebnis(TIMEOUT_MILLISEC);
        InputStream inputStream = null;
        if (ergebnis != null) {
            inputStream = new ByteArrayInputStream(ergebnis);
        }
        return inputStream;
    }
}
