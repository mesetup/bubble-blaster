package com.qtech.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qtech.bubbles.QBubbles;

public class PauseTickEvent extends Event {
    private final QBubbles main;

    public PauseTickEvent(QBubbles main) {
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
