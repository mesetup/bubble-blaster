package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.BubbleBlaster;

public class PauseTickEvent extends Event {
    private final BubbleBlaster main;

    public PauseTickEvent(BubbleBlaster main) {
        this.main = main;
    }

    public BubbleBlaster getMain() {
        return main;
    }

    /**
     * Get the current tick speed. (TPS)
     *
     * @return always 0.05d (20th of a second).
     * @see BubbleBlaster#TPS
     * @deprecated Is since 1.0.0 always 0.05d and therefore not needed.
     */
    @Deprecated
    public double getDeltaTime() {
        return 0.05; // Is always a 20th of a second.
    }
}
