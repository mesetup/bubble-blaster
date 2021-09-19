package com.ultreon.hydro.vector;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vector2f implements Externalizable, Cloneable {
    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {

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
        this.x = (float) Math.pow(this.x, x);
        this.y = (float) Math.pow(this.y, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2f = (Vector2f) o;
        return Float.compare(vector2f.getX(), getX()) == 0 && Float.compare(vector2f.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public Vector2f clone() {
        try {
            Vector2f clone = (Vector2f) super.clone();

            clone.x = this.x;
            clone.y = this.y;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }
}
