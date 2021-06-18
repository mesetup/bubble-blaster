package com.qtech.bubbles.event

import com.qtech.bubbles.BubbleBlaster

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see Event
 *
 * @see RenderEvent
 */
class TickEvent(val main: BubbleBlaster) : Event() {

    /**
     * Get the current tick speed. (TPS)
     *
     * @return always 0.05d (20th of a second).
     * @see BubbleBlaster.TPS
     *
     */
    @get:Deprecated("Is since 1.0.0 always 0.05d and therefore not needed.", ReplaceWith("0.05"))
    val deltaTime: Double
        get() = 0.05 // Is always a 20th of a second.
}