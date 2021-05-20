package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.event.bus.EventBus;


/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see Graphics
 * @see GraphicsProcessor
 */
public class CollisionEvent extends Event {
    @SuppressWarnings("unused")
    @Deprecated
    private static EventBus eventBus;

    @Deprecated
    public static EventBus getEventBus() {
        return eventBus;
    }

    private final BubbleBlaster main;
    private final double deltaTime;
    private final Entity source;
    private final Entity target;

    public CollisionEvent(BubbleBlaster main, double deltaTime, Entity source, Entity target) {
        this.deltaTime = deltaTime;
        this.source = source;
        this.target = target;
        this.main = main;
    }

    public BubbleBlaster getMain() {
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
