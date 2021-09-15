package com.ultreon.hydro.vector;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void mul(float i) {
        x *= i;
        y *= i;
    }

    public void div(float i) {
        x /= i;
        y /= i;
    }

    public void add(float i) {
        x += i;
        y += i;
    }

    public void sub(float i) {
        x -= i;
        y -= i;
    }

    public void pow(float i) {
        x = (float) Math.pow(x, i);
        y = (float) Math.pow(y, i);
    }

    public void mul(float x, float y) {
        this.x *= x;
        this.y *= y;
    }

    public void div(float x, float y) {
        this.x /= x;
        this.y /= y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public void pow(float x, float y) {
        this.x = (float) Math.pow(x, x);
        this.y = (float) Math.pow(y, y);
    }
}
