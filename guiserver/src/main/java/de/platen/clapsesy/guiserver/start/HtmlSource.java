package de.platen.clapsesy.guiserver.start;

import java.io.File;
import java.io.IOException;

import de.platen.clapsesy.guiserver.exception.GuiServerException;
import de.platen.clapsesy.guiserver.renderer.HtmlRenderEngine;
import javafx.scene.image.Image;

public class HtmlSource {

    private final HtmlRenderEngine htmlRenderEngine;
    private final String url;
    private final byte[] html;
    private final File file;
    private final int breite;
    private final int hoehe;

    public HtmlSource(final HtmlRenderEngine htmlRenderEngine, final String url, final int breite, final int hoehe) {
        this.htmlRenderEngine = htmlRenderEngine;
        this.url = url;
        html = null;
        file = null;
        this.breite = breite;
        this.hoehe = hoehe;
    }

    public HtmlSource(final HtmlRenderEngine htmlRenderEngine, final byte[] html, final int breite, final int hoehe) {
        this.htmlRenderEngine = htmlRenderEngine;
        url = null;
        this.html = html;
        file = null;
        this.breite = breite;
        this.hoehe = hoehe;
    }

    public HtmlSource(final HtmlRenderEngine htmlRenderEngine, final File file, final int breite, final int hoehe) {
        this.htmlRenderEngine = htmlRenderEngine;
        url = null;
        html = null;
        this.file = file;
        this.breite = breite;
        this.hoehe = hoehe;
    }

    public Image renderImage() {
        if (url != null) {
            return new Image(htmlRenderEngine.renderPng(url, breite, hoehe));
        } else {
            if (html != null) {
                return new Image(htmlRenderEngine.renderPng(html, breite, hoehe));
            } else {
                try {
                    return new Image(htmlRenderEngine.renderPng(file, breite, hoehe));
                } catch (final IOException e) {
                    throw new GuiServerException(e);
                }
            }
        }
    }
}
