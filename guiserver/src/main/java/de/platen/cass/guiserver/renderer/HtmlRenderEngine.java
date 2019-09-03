package de.platen.cass.guiserver.renderer;

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

public class HtmlRenderEngine {

	public InputStream renderPng(String urlString, int width, int height) throws IOException, SAXException {
		if (!urlString.startsWith("http:") //
				&& !urlString.startsWith("https:") //
				&& !urlString.startsWith("ftp:") //
				&& !urlString.startsWith("file:"))
			urlString = "http://" + urlString;

		DocumentSource docSource = new DefaultDocumentSource(urlString);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		this.render(docSource, width, height, byteArrayOutputStream);
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	public InputStream renderPng(File file, int width, int height) throws IOException, SAXException {
		byte[] data = Files.readAllBytes(file.toPath());
		return this.renderPng(data, width, height);
	}

	public InputStream renderPng(byte[] data, int width, int height) throws IOException, SAXException {
		InputStream inputStream = new ByteArrayInputStream(data);
		DocumentSource docSource = new StreamDocumentSource(inputStream, null, "text/html");
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		this.render(docSource, width, height, byteArrayOutputStream);
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	private void render(DocumentSource docSource, int width, int height, OutputStream output)
			throws SAXException, IOException {
		DOMSource parser = new DefaultDOMSource(docSource);
		Document doc = parser.parse();

		String mediaType = "screen";
		Dimension windowSize = new Dimension(width, height);

		MediaSpec media = new MediaSpec(mediaType);
		media.setDimensions(windowSize.width, windowSize.height);
		media.setDeviceDimensions(windowSize.width, windowSize.height);

		DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
		da.setMediaSpec(media);
		da.attributesToStyles(); // convert the HTML presentation attributes to inline styles
		da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); // use the standard style sheet
		da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); // use the additional style sheet
		da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); // render form fields using css
		da.getStyleSheets(); // load the author style sheets

		BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
		contentCanvas.setAutoMediaUpdate(false); // we have a correct media specification, do not update
		contentCanvas.getConfig().setClipViewport(false);
		contentCanvas.getConfig().setLoadImages(true);
		contentCanvas.getConfig().setLoadBackgroundImages(true);

		contentCanvas.createLayout(windowSize);
		ImageIO.write(contentCanvas.getImage(), "png", output);

		docSource.close();
	}
}
