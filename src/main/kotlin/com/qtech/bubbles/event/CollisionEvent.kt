package com.qtech.bubbles.event

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.event.bus.EventBus

/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see Graphics
 *
 * @see GraphicsProcessor
 */
class CollisionEvent(val main: BubbleBlaster, val deltaTime: Double, val source: Entity, val target: Entity) : Event() {

    companion object {
        @get:Deprecated("")
        @Deprecated("")
        val eventBus: EventBus? = null
    }
}