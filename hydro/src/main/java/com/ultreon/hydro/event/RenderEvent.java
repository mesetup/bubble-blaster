package com.ultreon.hydro.event;

import com.ultreon.hydro.render.Renderer;

public abstract class RenderEvent {
    private final Renderer graphics;

    public RenderEvent(Renderer graphics) {
        this.graphics = graphics;
    }

    public Renderer graphics() {
        return graphics;
    }
}
