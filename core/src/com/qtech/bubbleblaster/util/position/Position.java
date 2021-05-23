package com.qtech.bubbleblaster.util.position;

import com.qtech.bubbleblaster.common.graphics.shapes.Point;

import java.io.Serializable;

public abstract class Position extends Point implements Serializable {
    public Position(double X, double Y) {
        super(X, Y);
    }

    public abstract boolean isAbsolute();
}
