package com.ultreon.bubbles.util.position;

import com.ultreon.hydro.graphics.shapes.Point;

import java.io.Serializable;

public abstract class Position extends Point implements Serializable {
    public Position(double X, double Y) {
        super(X, Y);
    }

    public abstract boolean isAbsolute();
}
