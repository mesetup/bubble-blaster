package com.qtech.bubbles.common

/**
 * Event listener.
 *
 *
 * Use [.bindEvents] for binding events to method(s).
 * And use [.unbindEvents] for unbind events from method(s).
 *
 *
 * Also in the (un)bind methods assign a variable to true if the events were bound, false otherwise.
 * And last, use [.areEventsBound] for the boolean assigned in the bind/unbind events methods.
 */
abstract class Listener {
    protected var eventsActive = false
    protected abstract fun bindEvents()
    protected abstract fun unbindEvents()
    protected abstract fun areEventsBound(): Boolean
}