package de.platen.htmlrenderer.core;

import de.platen.htmlrenderer.api.HtmlRenderer;
import de.platen.syntaxparser.Parser;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class HtmlRendererService {

    private Thread thread;
    private final AtomicBoolean istInitialisiert = new AtomicBoolean(false);

    public  void start(final String host, final int port) {
        synchronized(this.istInitialisiert) {
            if (!this.istInitialisiert.get()) {
                this.istInitialisiert.set(true);
                final HtmlRenderer htmlRenderer = new HtmlRendererImpl();
                final InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
                final Parsererzeugung parsererzeugung = new Parsererzeugung();
                final Parser parser = parsererzeugung.erzeugeParser();
                final WebSocketServerHtmlRenderer webSocketServerHtmlRenderer = new WebSocketServerHtmlRenderer(inetSocketAddress, htmlRenderer, parser);
                this.thread = new Thread(webSocketServerHtmlRenderer);
                this.thread.start();
            }
        }
    }

    public void stop() {
        synchronized(this.istInitialisiert) {
            if (this.istInitialisiert.get()) {
                this.istInitialisiert.set(false);
                this.thread.interrupt();
                this.thread = null;
            }
        }
    }
}
