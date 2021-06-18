package com.qtech.bubbles.common.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.gui.Widget
import java.awt.Cursor

abstract class Screen {
    private var eventsActive = false
    private val children: MutableList<Widget> = ArrayList()
    @JvmField
    protected val game: BubbleBlaster = BubbleBlaster.instance

    /**
     * <h1>Show Scene</h1>
     *
     * @author Quinten Jungblut
     */
    abstract fun init()

    /**
     * <h1>Hide Scene</h1>
     * Hide scene, unbind events.
     *
     * @param to the next scene to go.
     * @return true to cancel change screen.
     * @author Quinten Jungblut
     */
    open fun onClose(to: Screen?): Boolean {
        return true
    }

    fun bindEvents() {
        eventsActive = true
    }

    fun unbindEvents() {
        eventsActive = false
    }

    open fun eventsAreActive(): Boolean {
        return eventsActive
    }

    open fun tick() {}
    abstract fun render(game: BubbleBlaster, gp: GraphicsProcessor)
    fun <T : Widget?> addWidget(widget: T): T {
        children.add(widget!!)
        return widget
    }

    open fun renderGUI(game: BubbleBlaster, gg: GraphicsProcessor) {
        for (widget in children) {
            widget.paint(gg)
        }
    }

    open val defaultCursor: Cursor?
        get() = BubbleBlaster.instance.defaultCursor

    open fun doesPauseGame(): Boolean {
        return true
    }
}