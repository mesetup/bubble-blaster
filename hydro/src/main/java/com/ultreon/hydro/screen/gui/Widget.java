package com.ultreon.hydro.screen.gui;

import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.vector.Vector2i;

import java.util.Objects;

@SuppressWarnings("unused")
public abstract class Widget implements IGuiListener {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private final long hash;

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.hash = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget that = (Widget) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    public abstract void render(Renderer renderer);

    public void onMouseClick(int x, int y, int button, int count) {

    }

    public void onMousePress(int x, int y, int button) {

    }

    public void onMouseRelease(int x, int y, int button) {

    }

    public void onMouseMove(int x, int y) {

    }

    public void onMouseDrag(int x, int y, int button) {

    }

    public void onMouseLeave() {

    }

    public void onMouseEnter(int x, int y) {

    }

    public void onMouseWheel(int x, int y, double rotation, int amount, int units) {

    }

    public void onKeyPress(int keyCode, char character) {

    }

    public void onKeyRelease(int keyCode, char character) {

    }

    public void onKeyType(int keyCode, char character) {

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Width should be positive.");
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (width < 0) {
            throw new IllegalArgumentException("Height should be positive.");
        }
        this.height = height;
    }

    public Vector2i getPos() {
        return new Vector2i(x, y);
    }

    public void setPos(int x, int y) {
        setX(x);
        setY(y);
    }

    public Vector2i getSize() {
        return new Vector2i(width, height);
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setBounds(int x, int y, int width, int height) {
        setPos(x, y);
        setSize(width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
    }

    public boolean isWithinBounds(Vector2i pos) {
        return pos.x >= this.x && pos.y >= this.y && pos.x <= this.x + width && pos.y <= this.y + height;
    }
}
