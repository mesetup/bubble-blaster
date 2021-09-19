package com.ultreon.hydro.event;

import com.ultreon.hydro.render.Renderer;

public abstract class RenderGameEvent extends RenderEvent {
    public RenderGameEvent(Renderer graphics) {
        super(graphics);
    }

    public static class Before extends RenderGameEvent {
        public Before(Renderer graphics) {
            super(graphics);
        }
    }

    public static class After extends RenderGameEvent {
        public After(Renderer graphics) {
            super(graphics);
        }
    }
}
