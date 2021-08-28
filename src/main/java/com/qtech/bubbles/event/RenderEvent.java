package com.qtech.bubbles.event;

import java.awt.*;

public abstract class RenderEvent {
    private final Graphics2D graphics;

    public RenderEvent(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public Graphics2D graphics() {
        return graphics;
    }
}
