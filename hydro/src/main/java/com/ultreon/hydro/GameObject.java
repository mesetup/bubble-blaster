package com.ultreon.hydro;

import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.vector.Vector2f;

public abstract class GameObject {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    protected void setX(float x) {
        this.x = x;
    }

    protected void setY(float y) {
        this.y = y;
    }

    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }

    public abstract void render(Renderer renderer);
}
