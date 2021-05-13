package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.event.old.QEvent;
import com.qsoftware.bubbles.event.old.QRenderEvent;

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see QEvent
 * @see QRenderEvent
 */
public class TickEvent extends Event {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        return eventBus;
    }

    private final QBubbles main;
    private final double deltaTime;

    public TickEvent(QBubbles main, Scene scene, double deltaTime) {
        this.main = main;
        this.deltaTime = deltaTime;
    }

    public QBubbles getMain() {
        return main;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
