package com.qtech.bubbles.common.cursor;

import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.renderer.CursorRenderer;

import java.awt.*;

public class TextCursorRenderer extends CursorRenderer {
    public TextCursorRenderer() {
        super("text_cursor");
    }

    @Override
    public void draw(GraphicsProcessor g) {
        ((GraphicsProcessor) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawLine(0, 1, 0, 24);
        g.setColor(Color.white);
        g.drawLine(1, 0, 1, 25);
        g.setColor(Color.white);
        g.drawLine(2, 1, 2, 24);
        g.setColor(Color.black);
        g.drawLine(1, 1, 1, 24);
        g.dispose();
    }
}
