package com.ultreon.hydro.vector;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vector2i implements Externalizable, Cloneable {
    public int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i() {

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

    public void mul(int i) {
        x *= i;
        y *= i;
    }

    public void div(int i) {
        x /= i;
        y /= i;
    }

    public void add(int i) {
        x += i;
        y += i;
    }

    public void sub(int i) {
        x -= i;
        y -= i;
    }

    public void pow(int i) {
        x = (int) Math.pow(x, i);
        y = (int) Math.pow(y, i);
    }

    public void mul(int x, int y) {
        this.x *= x;
        this.y *= y;
    }

    public void div(int x, int y) {
        this.x /= x;
        this.y /= y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void sub(int x, int y) {
        this.x -= x;
        this.y -= y;
    }

    public void pow(int x, int y) {
        this.x = (int) Math.pow(this.x, x);
        this.y = (int) Math.pow(this.y, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2i = (Vector2i) o;
        return getX() == vector2i.getX() && getY() == vector2i.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Vector2i{" +
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
        out.writeInt(x);
        out.writeInt(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.x = in.readInt();
        this.y = in.readInt();
    }
}
