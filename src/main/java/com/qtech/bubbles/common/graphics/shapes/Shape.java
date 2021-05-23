package com.qtech.bubbles.common.graphics.shapes;

public interface Shape {
    boolean doIntersect(Shape shape) throws UnsupportedOperationException;
}