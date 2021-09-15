package com.ultreon.bubbles.entity.bubble;

import java.awt.*;

public class BubbleCircle {
    private final int index;
    private final Color color;

    public BubbleCircle(int index, Color color) {
        this.index = index;
        this.color = color;
    }

    public int getIndex() {
        return index;
    }

    public Color getColor() {
        return color;
    }
}
