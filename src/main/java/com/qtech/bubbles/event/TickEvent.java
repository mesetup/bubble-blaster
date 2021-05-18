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

    public TickEvent(QBubbles main) {
        this.main = main;
    }

    public QBubbles getMain() {
        return main;
    }

    /**
     * Get the current tick speed. (TPS)
     *
     * @deprecated Is since 1.0.0 always 0.05d and therefore not needed.
     * @return always 0.05d (20th of a second).
     * @see QBubbles#TPS
     */
    @Deprecated
    public double getDeltaTime() {
        return 0.05; // Is always a 20th of a second.
    }
}
