package com.qtech.bubbles.gui.border;

import com.qtech.bubbles.graphics.Insets;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class Border extends AbstractBorder {
    private final com.qtech.bubbles.graphics.Insets borderInsets;
    private boolean borderOpaque;
    private Paint paint = new Color(0, 0, 0);

    public Border(com.qtech.bubbles.graphics.Insets insets) {
        this.borderInsets = insets;
    }

    public Border(int top, int left, int bottom, int right) {
        this.borderInsets = new com.qtech.bubbles.graphics.Insets(top, left, bottom, right);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        com.qtech.bubbles.graphics.Insets insets = getBorderInsets(c);

        Graphics2D gg = (Graphics2D) g;

        //  Set paint.
        gg.setPaint(paint);

        //  Draw rectangles around the component, but do not draw
        //  in the component area itself.
        gg.fillRect(x + insets.left, y, width - insets.left - insets.right, insets.top);
        gg.fillRect(x, y, insets.left, height);
        gg.fillRect(x + width - insets.right, y, insets.right, height);
        gg.fillRect(x + insets.left, y + height - insets.bottom, width - insets.left - insets.right, insets.bottom);
    }

    @Override
    public boolean isBorderOpaque() {
        return borderOpaque;
    }

    public void setBorderOpaque(boolean borderOpaque) {
        this.borderOpaque = borderOpaque;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public com.qtech.bubbles.graphics.Insets getBorderInsets() {
        return borderInsets;
    }

    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }

    public void setBorderTop(int topWidth) {
        borderInsets.top = topWidth;
    }

    public void setBorderLeft(int leftWidth) {
        borderInsets.left = leftWidth;
    }

    public void setBorderBottom(int bottomWidth) {
        borderInsets.bottom = bottomWidth;
    }

    public void setBorderRight(int rightWidth) {
        borderInsets.right = rightWidth;
    }

    public int getBorderTop() {
        return borderInsets.top;
    }

    public int getBorderLeft() {
        return borderInsets.left;
    }

    public int getBorderBottom() {
        return borderInsets.bottom;
    }

    public int getBorderRight() {
        return borderInsets.right;
    }
}