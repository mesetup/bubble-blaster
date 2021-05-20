package com.qtech.bubbleblaster.common.cursor;

import com.qtech.bubbleblaster.common.renderer.CursorRenderer;


public class DefaultCursorRenderer extends CursorRenderer {
    public DefaultCursorRenderer() {
        super("default_cursor");
    }

    @Override
    public void draw(Graphics g) {
        Polygon poly = new Polygon(new int[]{0, 10, 5, 0}, new int[]{0, 12, 12, 16}, 4);

        ((GraphicsProcessor) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillPolygon(poly);
        g.setColor(Color.white);
        g.drawPolygon(poly);
        g.dispose();
    }
}
