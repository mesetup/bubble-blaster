package com.qsoftware.bubbles.common.cursor;

import com.qsoftware.bubbles.common.renderer.CursorRenderer;

import java.awt.*;

public class DefaultCursorRenderer extends CursorRenderer {
    public DefaultCursorRenderer() {
        super("default_cursor");
    }

    @Override
    public void draw(Graphics g) {
        Polygon poly = new Polygon(new int[]{0, 10, 5, 0}, new int[]{0, 12, 12, 16}, 4);

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillPolygon(poly);
        g.setColor(Color.white);
        g.drawPolygon(poly);
        g.dispose();
    }
}
