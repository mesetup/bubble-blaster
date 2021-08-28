package com.qtech.bubbles.event;

import com.qtech.bubbles.BubbleBlaster;

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see Event
 * @see RenderEvent
 */
public class TickEvent extends Event {
    private final BubbleBlaster main;

    public TickEvent(BubbleBlaster main) {
        this.main = main;
    }

    public BubbleBlaster getGame() {
        return main;
    }
}
