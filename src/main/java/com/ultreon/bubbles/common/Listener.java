package com.ultreon.bubbles.common;

/**
 * Event listener.
 * <p>
 * Use {@link #bindEvents()} for binding events to method(s).
 * And use {@link #unbindEvents()} for unbind events from method(s).
 * <p>
 * Also in the (un)bind methods assign a variable to true if the events were binded, false otherwise.
 * And last, use {@link #areEventsBound()} for the boolean assigned in the bind/unbind events methods.
 *
 * @deprecated events are made not in another way.
 */
@Deprecated
public abstract class Listener {
    @Deprecated
    protected boolean eventsActive;

    @Deprecated
    protected abstract void bindEvents();

    @Deprecated
    protected abstract void unbindEvents();

    @Deprecated
    protected abstract boolean areEventsBound();
}
