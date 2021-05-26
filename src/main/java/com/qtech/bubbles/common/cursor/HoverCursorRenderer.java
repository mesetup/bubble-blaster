package com.qtech.bubbles.common.cursor;

import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.renderer.CursorRenderer;

import java.awt.*;

public class HoverCursorRenderer extends CursorRenderer {
    public HoverCursorRenderer() {
        super("pointer_cursor");
    }

    @Override
    public void draw(GraphicsProcessor g) {
        Polygon poly = new Polygon(new int[]{10, 20, 15, 10}, new int[]{10, 22, 22, 26}, 4);

        ((GraphicsProcessor) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawOval(0, 0, 20, 20);
        g.setColor(Color.white);
        g.drawOval(2, 2, 16, 16);
        g.setColor(Color.black);
        g.fillPolygon(poly);
        g.setColor(Color.white);
        g.drawPolygon(poly);
        g.setColor(Color.black);
        g.drawOval(1, 1, 18, 18);
        g.dispose();

    }
}
