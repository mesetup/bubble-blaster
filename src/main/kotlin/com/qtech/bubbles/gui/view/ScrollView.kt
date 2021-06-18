@file:Suppress("unused", "UNUSED_PARAMETER")

package com.qtech.bubbles.gui.view

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.TickEvent
import com.qtech.bubbles.event.bus.EventBus
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.gui.Widget
import java.awt.geom.Rectangle2D

class ScrollView(private val screen: Screen, private var innerBounds: Rectangle2D, private var outerBounds: Rectangle2D) : View() {
    private val tickEventCode = 0
    private val renderEventCode = 0
    private val children: MutableSet<Widget>? = null
    private var eventsBound = false
    private val binding: EventBus.Handler? = null
    fun setOuterBounds(outerBounds: Rectangle2D) {
        this.outerBounds = outerBounds
    }

    fun setInnerBounds(innerBounds: Rectangle2D) {
        this.innerBounds = innerBounds
    }

    fun bindEvents() {
        BubbleBlaster.eventBus.register(this)
        eventsBound = true
    }

    @SubscribeEvent
    private fun tick(event: TickEvent) {
    }

    private fun render(gg: GraphicsProcessor) {

    }

    fun add(widget: Widget) {
        children!!.add(widget)
    }

    fun unbindEvents() {
        BubbleBlaster.eventBus.unregister(this)
        eventsBound = false
    }

    fun areEventsBound(): Boolean {
        return eventsBound
    }

    override fun destroy() {
        unbindEvents()
    }

    override fun paint(gp: GraphicsProcessor) {
        containerGraphics = gp.create(outerBounds.x.toInt(), outerBounds.y.toInt(), outerBounds.width.toInt(), outerBounds.height.toInt())
        for (child in children!!) {
            child.paint(containerGraphics!!)
        }
    }

    init {
        bindEvents()
    }
}