package com.ultreon.hydro.gui.cursor;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;

public class HoverCursorRenderer extends CursorRenderer {
    public HoverCursorRenderer() {
        super("pointer_cursor");
    }

    @Override
    public void draw(Renderer g) {
        Polygon poly = new Polygon(new int[]{10, 20, 15, 10}, new int[]{10, 22, 22, 26}, 4);

        ((Renderer) g).hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.color(Color.white);
        g.ovalLine(0, 0, 20, 20);
        g.color(Color.white);
        g.ovalLine(2, 2, 16, 16);
        g.color(Color.black);
        g.polygon(poly);
        g.color(Color.white);
        g.polygonLine(poly);
        g.color(Color.black);
        g.ovalLine(1, 1, 18, 18);
        g.dispose();

    }
}
