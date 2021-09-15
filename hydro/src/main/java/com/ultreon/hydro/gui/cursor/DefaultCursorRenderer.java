package com.ultreon.hydro.gui.cursor;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;

public class DefaultCursorRenderer extends CursorRenderer {
    public DefaultCursorRenderer() {
        super("default_cursor");
    }

    @Override
    public void draw(Renderer g) {
        Polygon poly = new Polygon(new int[]{0, 10, 5, 0}, new int[]{0, 12, 12, 16}, 4);

        ((Renderer) g).hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.color(Color.black);
        g.polygon(poly);
        g.color(Color.white);
        g.polygonLine(poly);
        g.dispose();
    }
}
