package com.qtech.bubbles.util.position;

public class AbsolutePosition extends Position {
    public AbsolutePosition(double x, double y) {
        super(x, y);
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }
}
