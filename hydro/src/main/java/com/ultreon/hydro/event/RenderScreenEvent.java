package com.ultreon.hydro.event;

import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;

public abstract class RenderScreenEvent extends RenderEvent {
    private final Screen screen;

    public RenderScreenEvent(Screen screen, Renderer graphics) {
        super(graphics);
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public static class Before extends RenderScreenEvent {
        public Before(Screen screen, Renderer graphics) {
            super(screen, graphics);
        }
    }

    public static class After extends RenderScreenEvent {
        public After(Screen screen, Renderer graphics) {
            super(screen, graphics);
        }
    }
}
