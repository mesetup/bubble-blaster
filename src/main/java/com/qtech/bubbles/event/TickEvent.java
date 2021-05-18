package com.qtech.bubbles.event;

import com.qtech.bubbles.QBubbles;

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see Event
 * @see RenderEvent
 */
public class TickEvent extends Event {
    private final QBubbles main;
    private final double deltaTime;

    public TickEvent(QBubbles main, double deltaTime) {
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
