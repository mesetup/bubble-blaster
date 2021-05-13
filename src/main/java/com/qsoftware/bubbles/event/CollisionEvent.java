package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.event.old.QEvent;
import com.qsoftware.bubbles.event.old.QTickEvent;

import java.awt.*;

/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see QEvent
 * @see QTickEvent
 * @see Graphics
 * @see Graphics2D
 */
public class CollisionEvent extends Event {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        return eventBus;
    }

    private final QBubbles main;
    private final double deltaTime;
    private final Entity source;
    private final Entity target;

    public CollisionEvent(QBubbles main, Scene scene, double deltaTime, Entity source, Entity target) {
        this.deltaTime = deltaTime;
        this.source = source;
        this.target = target;
        this.main = main;
    }

    public QBubbles getMain() {
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
