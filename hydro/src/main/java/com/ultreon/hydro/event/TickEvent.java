package com.ultreon.hydro.event;

import com.ultreon.hydro.Game;

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see Event
 * @see RenderEvent
 */
public class TickEvent extends Event {
    private final Game main;

    public TickEvent(Game main) {
        this.main = main;
    }

    public Game getGame() {
        return main;
    }
}
