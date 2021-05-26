package com.qtech.bubbles.graphics;

import com.qtech.bubbles.common.GraphicsProcessor;

import java.awt.*;

public class Border {
    private final Insets borderInsets;
    private Paint paint = new Color(0, 0, 0);

    public Border(Insets insets) {
        this.borderInsets = insets;
    }

    public Border(int top, int left, int bottom, int right) {
        this.borderInsets = new Insets(top, left, bottom, right);
    }

    public void paintBorder(GraphicsProcessor gp, int x, int y, int width, int height) {
        Insets insets = getBorderInsets();

        //  Set paint.
        gp.setPaint(paint);

        //  Draw rectangles around the component, but do not draw
        //  in the component area itself.
        gp.fillRect(x + insets.left, y, width - insets.left - insets.right, insets.top);
        gp.fillRect(x, y, insets.left, height);
        gp.fillRect(x + width - insets.right, y, insets.right, height);
        gp.fillRect(x + insets.left, y + height - insets.bottom, width - insets.left - insets.right, insets.bottom);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Insets getBorderInsets() {
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