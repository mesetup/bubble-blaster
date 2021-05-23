package com.qtech.bubbles.common;

/**
 * Event listener.
 * <p>
 * Use {@link #bindEvents()} for binding events to method(s).
 * And use {@link #unbindEvents()} for unbind events from method(s).
 * <p>
 * Also in the (un)bind methods assign a variable to true if the events were binded, false otherwise.
 * And last, use {@link #areEventsBound()} for the boolean assigned in the bind/unbind events methods.
 */
public abstract class Listener {
    protected boolean eventsActive;

    protected abstract void bindEvents();

    protected abstract void unbindEvents();

    protected abstract boolean areEventsBound();
}
