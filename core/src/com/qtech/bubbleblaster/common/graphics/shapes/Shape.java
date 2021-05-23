package com.qtech.bubbleblaster.common.graphics.shapes;

public interface Shape {
    boolean doIntersect(Shape shape) throws UnsupportedOperationException;
}