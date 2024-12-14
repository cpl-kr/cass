package de.platen.clapsesy.htmlrenderer.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HtmlRendererImplTest {

    @Test
    void testRenderPngUrl() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("test.html");
        assert url != null;
        Assertions.assertDoesNotThrow(() -> new HtmlRendererImpl().renderPng(String.valueOf(url), 10, 20));
    }

    @Test
     void testRenderPngFile() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("test.html");
        assert url != null;
        File file =  new File(url.getPath());
        Assertions.assertDoesNotThrow(() -> new HtmlRendererImpl().renderPng(file, 10, 20));
    }

    @Test
    void testRenderPngBytes() throws IOException {
        Assertions.assertDoesNotThrow(() -> new HtmlRendererImpl().renderPng("<html><header></header<body></body</html>".getBytes(), 10, 20));
    }
}
