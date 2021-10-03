package com.ultreon.bubbles.event;

import com.ultreon.bubbles.entity.Entity;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.CollisionEvent;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.render.Renderer;

/**
 * Render Event
 * This event is for rendering the game objects on the canvas.
 *
 * @see Renderer
 * @see Renderer
 */
public class EntityCollisionEvent extends CollisionEvent {
    @SuppressWarnings("unused")
    @Deprecated
    private static AbstractEvents eventManager;

    @Deprecated
    public static AbstractEvents getEventBus() {
        return eventManager;
    }

    private final Game main;
    private final double deltaTime;
    private final Entity source;
    private final Entity target;

    public EntityCollisionEvent(Game main, double deltaTime, Entity source, Entity target) {
        super(main, source, target);
        this.deltaTime = deltaTime;
        this.source = source;
        this.target = target;
        this.main = main;
    }

    public Game getMain() {
        return main;
    }

    public Entity getSource() {
        return source;
    }

    public Entity getTarget() {
        return target;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
