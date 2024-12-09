package de.platen.htmlrenderer.api;

import java.io.File;
import java.io.IOException;

public interface HtmlRenderer {

    byte[] renderPng(String urlString, int width, int height) throws IOException;

    byte[] renderPng(File file, int width, int height) throws IOException;

    byte[] renderPng(byte[] data, int width, int height) throws IOException;
}
