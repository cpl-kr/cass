package de.platen.clapsesy.guiserver.frontend.guielement;

import de.platen.clapsesy.guiserver.frontend.Height;
import de.platen.clapsesy.guiserver.frontend.Width;
import de.platen.clapsesy.guiserver.frontend.X;
import de.platen.clapsesy.guiserver.frontend.Y;

public class ElementPosition {

    private final X x;
    private final Y y;
    private final Width width;
    private final Height height;

    public ElementPosition(final X x, final Y y, final Width width, final Height height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Width getWidth() {
        return width;
    }

    public Height getHeight() {
        return height;
    }
}
