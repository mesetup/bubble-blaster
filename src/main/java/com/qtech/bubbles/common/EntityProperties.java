package com.qtech.bubbles.common;

import java.io.Serializable;

/**
 * @author Quinten
 * @since 1.0.0
 */
public class EntityProperties implements Serializable {
    protected final int x;
    protected final int y;

    public EntityProperties(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
