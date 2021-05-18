package com.qtech.bubbles.util.position;

import com.qtech.bubbles.common.graphics.shapes.Point;

import java.io.Serializable;

public abstract class Position extends Point implements Serializable {
    public Position(double X, double Y) {
        super(X, Y);
    }

    public abstract boolean isAbsolute();
}
