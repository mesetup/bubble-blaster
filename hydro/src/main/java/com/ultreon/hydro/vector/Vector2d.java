package com.ultreon.hydro.vector;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vector2d implements Externalizable, Cloneable {
    public double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void mul(double i) {
        x *= i;
        y *= i;
    }

    public void div(double i) {
        x /= i;
        y /= i;
    }

    public void add(double i) {
        x += i;
        y += i;
    }

    public void sub(double i) {
        x -= i;
        y -= i;
    }

    public void pow(double i) {
        x = Math.pow(x, i);
        y = Math.pow(y, i);
    }

    public void mul(double x, double y) {
        this.x *= x;
        this.y *= y;
    }

    public void div(double x, double y) {
        this.x /= x;
        this.y /= y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void sub(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void pow(double x, double y) {
        this.x = Math.pow(this.x, x);
        this.y = Math.pow(this.y, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return Double.compare(vector2d.getX(), getX()) == 0 && Double.compare(vector2d.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public Vector2d clone() {
        try {
            Vector2d clone = (Vector2d) super.clone();

            clone.x = this.x;
            clone.y = this.y;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
    }
}
