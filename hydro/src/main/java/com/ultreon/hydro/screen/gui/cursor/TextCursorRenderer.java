package com.ultreon.hydro.screen.gui.cursor;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;

public class TextCursorRenderer extends CursorRenderer {
    public TextCursorRenderer() {
        super("text_cursor");
    }

    @Override
    public void draw(Renderer g) {
        ((Renderer) g).hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.color(Color.white);
        g.line(0, 1, 0, 24);
        g.color(Color.white);
        g.line(1, 0, 1, 25);
        g.color(Color.white);
        g.line(2, 1, 2, 24);
        g.color(Color.black);
        g.line(1, 1, 1, 24);
        g.dispose();
    }
}
