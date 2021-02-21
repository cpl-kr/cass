package de.platen.htmlrenderer;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cz.vutbr.web.css.MediaSpec;

public class HtmlRenderer {

    public byte[] renderPng(String urlString, final int width, final int height) throws IOException {
        if (!urlString.startsWith("http:") //
                && !urlString.startsWith("https:") //
                && !urlString.startsWith("ftp:") //
                && !urlString.startsWith("file:")) {
            urlString = "http://" + urlString;
        }
        System.out.println("Rendern von Url " + urlString);
        final DocumentSource docSource = new DefaultDocumentSource(urlString);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        render(docSource, width, height, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] renderPng(final File file, final int width, final int height) throws IOException {
        System.out.println("Rendern von Datei " + file.getName());
        final byte[] data = Files.readAllBytes(file.toPath());
        return this.renderPng(data, width, height);
    }

    public byte[] renderPng(final byte[] data, final int width, final int height) throws IOException {
        System.out.println("Rendern von Html-Daten:");
        System.out.println(new String(data));
        System.out.println();
        final InputStream inputStream = new ByteArrayInputStream(data);
        final DocumentSource docSource = new StreamDocumentSource(inputStream, null, "text/html");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        render(docSource, width, height, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void render(final DocumentSource docSource, final int width, final int height, final OutputStream output)
            throws IOException {
        final DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = null;
        try {
            doc = parser.parse();
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        }
        final String mediaType = "screen";
        final Dimension windowSize = new Dimension(width, height);
        final MediaSpec media = erzeugeMediaSpec(mediaType, windowSize);
        final DOMAnalyzer da = erzeugeDOMAnalyser(docSource, doc, media);
        final BrowserCanvas contentCanvas = erzeugeBrowserCanvas(docSource, windowSize, da);
        ImageIO.write(contentCanvas.getImage(), "png", output);
        docSource.close();
    }

    private MediaSpec erzeugeMediaSpec(final String mediaType, final Dimension windowSize) {
        final MediaSpec media = new MediaSpec(mediaType);
        media.setDimensions(windowSize.width, windowSize.height);
        media.setDeviceDimensions(windowSize.width, windowSize.height);
        return media;
    }

    private DOMAnalyzer erzeugeDOMAnalyser(final DocumentSource docSource, final Document doc, final MediaSpec media) {
        final DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
        da.setMediaSpec(media);
        da.attributesToStyles(); // convert the HTML presentation attributes to inline styles
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); // use the standard style sheet
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); // use the additional style sheet
        da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); // render form fields using css
        da.getStyleSheets(); // load the author style sheets
        return da;
    }

    private BrowserCanvas erzeugeBrowserCanvas(final DocumentSource docSource, final Dimension windowSize,
            final DOMAnalyzer da) {
        final BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
        contentCanvas.setAutoMediaUpdate(false); // we have a correct media specification, do not update
        contentCanvas.getConfig().setClipViewport(false);
        contentCanvas.getConfig().setLoadImages(true);
        contentCanvas.getConfig().setLoadBackgroundImages(true);
        contentCanvas.createLayout(windowSize);
        return contentCanvas;
    }
}
