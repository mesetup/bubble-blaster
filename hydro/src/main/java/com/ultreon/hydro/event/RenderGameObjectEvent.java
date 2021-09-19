package com.ultreon.hydro.event;

import com.ultreon.hydro.GameObject;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;

public abstract class RenderGameObjectEvent extends RenderEvent {
    private final GameObject gameObject;

    public RenderGameObjectEvent(GameObject gameObject, Renderer graphics) {
        super(graphics);
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public static class Before extends RenderGameObjectEvent {
        public Before(GameObject gameObject, Renderer graphics) {
            super(gameObject, graphics);
        }
    }

    public static class After extends RenderGameObjectEvent {
        public After(GameObject gameObject, Renderer graphics) {
            super(gameObject, graphics);
        }
    }
}
