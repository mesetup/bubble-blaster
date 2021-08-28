package com.qtech.bubbles.gui.border;

import com.qtech.bubbles.graphics.Insets;

import java.awt.*;

public class OuterBorder extends Border {
    public OuterBorder(com.qtech.bubbles.graphics.Insets insets) {
        super(insets);
    }

    public OuterBorder(int top, int left, int bottom, int right) {
        super(top, left, bottom, right);
    }


    /**
     * Paints a border.
     *
     * @param c      the component.
     * @param g      the graphics.
     * @param x      the x-positon.
     * @param y      the y-position.
     * @param width  the width.
     * @param height the height.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // Get insets
        Insets insets = getBorderInsets(c);

        // Cast to Graphics2D
        Graphics2D gg = (Graphics2D) g;

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
