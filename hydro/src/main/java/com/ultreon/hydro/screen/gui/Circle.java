package com.ultreon.hydro.screen.gui;

import static java.lang.Math.*;

public class Circle extends Shape {
    private int x;
    private int y;
    private int radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - radius / 2, y - radius / 2, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        int dx = abs(x-this.x);
        int dy = abs(y-this.y);
        int r = radius;

        if (dx > r) {
            return false;
        }
        if (dy > r) {
            return false;
        }
        if (dx + dy <= r) {
            return true;
        }

        return pow(dx, 2) + pow(dy, 2) <= pow(r, 2);
    }
}
