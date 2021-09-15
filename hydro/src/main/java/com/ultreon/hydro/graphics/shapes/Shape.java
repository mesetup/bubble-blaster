package com.ultreon.hydro.graphics.shapes;

public interface Shape {
    boolean doIntersect(Shape shape) throws UnsupportedOperationException;
}