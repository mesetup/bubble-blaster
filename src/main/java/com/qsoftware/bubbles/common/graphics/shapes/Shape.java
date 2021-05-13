package com.qsoftware.bubbles.common.graphics.shapes;

public interface Shape {
    boolean doIntersect(Shape shape) throws UnsupportedOperationException;
}