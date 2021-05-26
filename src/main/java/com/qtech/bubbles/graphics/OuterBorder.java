package com.qtech.bubbles.graphics;

import com.qtech.bubbles.common.GraphicsProcessor;

import java.awt.*;

public class OuterBorder extends Border {
    public OuterBorder(Insets insets) {
        super(insets);
    }

    public OuterBorder(int top, int left, int bottom, int right) {
        super(top, left, bottom, right);
    }


    /**
     * Paints a border.
     *
     * @param gp      the graphics.
     * @param x      the x-positon.
     * @param y      the y-position.
     * @param width  the width.
     * @param height the height.
     */
    @Override
    public void paintBorder(GraphicsProcessor gp, int x, int y, int width, int height) {
        // Get insets
        Insets insets = getBorderInsets();

        // Cast to GraphicsProcessor
        GraphicsProcessor gg = (GraphicsProcessor) gp;

        // Save old paint.
        Paint oldPaint = gg.getPaint();

        // Set paint.
        gg.setPaint(getPaint());

        // Draw rectangles around the component, but do not draw
        // in the component area itself.
        gg.fillRect(x - insets.left, y - insets.top, width + insets.left + insets.right, insets.top);
        gg.fillRect(x - insets.left, y, insets.left, height);
        gg.fillRect(x + width, y, insets.right, height);
        gg.fillRect(x - insets.left, y + height, width + insets.left + insets.right, insets.bottom);

        // Set backup paint.
        gg.setPaint(oldPaint);
    }
}
