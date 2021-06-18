@file:Suppress("unused")

package com.qtech.bubbles.gui

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.interfaces.Listener
import com.qtech.bubbles.core.controllers.MouseController.Companion.instance
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import com.qtech.bubbles.event.KeyboardEvent
import com.qtech.bubbles.event.MouseEvent
import com.qtech.bubbles.event.RenderEventPriority
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.type.KeyEventType
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.util.Util
import com.qtech.bubbles.util.helpers.MathHelper
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.geom.Point2D

open class OptionsTextEntry(
// Bounds.
    var bounds: Rectangle, // Render priority/
    var renderEventPriority: RenderEventPriority?
) : Listener {
    // Fonts.
    protected val defaultFont = Font(Util.game.sansFontName, Font.PLAIN, 24)

    // Cursor Index/
    protected var cursorIndex = 0

    // Events/
    private var eventsActive = false

    // Values.
    lateinit var text: String

    // State
    protected var activated = false
    var visualX: Int? = null
    var visualY: Int? = null
    protected var hovered = false

    var x: Int
        get() = bounds.x
        set(value) {
            bounds.x = value
        }
    var y: Int
        get() = bounds.y
        set(value) {
            bounds.y = value
        }
    var width: Int
        get() = bounds.width
        set(value) {
            bounds.width = value
        }
    var height: Int
        get() = bounds.height
        set(value){
            bounds.height = value
        }
    var location: Point
        get() = bounds.location
        set(value){
            bounds.location = value
        }
    var size: Dimension
        get() = bounds.size
        set(value){
            bounds.size = value
        }

    // Constructor.
    constructor(bounds: Rectangle) : this(bounds.x, bounds.y, bounds.width, bounds.height, RenderEventPriority.AFTER_FILTER)
    constructor(x: Int, y: Int, width: Int, height: Int) : this(Rectangle(x, y, width, height))
    constructor(x: Int, y: Int, width: Int, height: Int, renderEventPriority: RenderEventPriority?) : this(Rectangle(x, y, width, height), renderEventPriority)

    @SubscribeEvent
    open fun onMouse(evt: MouseEvent) {
        if (evt.button == 1) {
            activated = bounds.contains(evt.point)
        }
    }

    @SubscribeEvent
    open fun onKeyboard(evt: KeyboardEvent) {
        if (!activated) return
        if (evt.type === KeyEventType.PRESS || evt.type === KeyEventType.HOLD) {
            if (evt.parentEvent.keyCode == KeyEvent.VK_BACK_SPACE) {
                if (text.isEmpty()) return
                val leftText = text.substring(0, cursorIndex - 1)
                val rightText = text.substring(cursorIndex)
                text = leftText + rightText
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_LEFT) {
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_RIGHT) {
                cursorIndex = MathHelper.clamp(cursorIndex + 1, 0, text.length)
                return
            }
            var c = evt.parentEvent.keyChar
            if (evt.parentEvent.keyCode == KeyEvent.VK_DEAD_ACUTE) {
                c = '\''
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_QUOTEDBL) {
                c = '"'
            }
            if (c.code.toShort() >= 32) {
//                text += c;
                val leftText = text.substring(0, cursorIndex)
                val rightText = text.substring(cursorIndex)
                text = leftText + c + rightText
                cursorIndex++
            }
        }
    }

    override fun bindEvents() {
        eventsActive = true
        BubbleBlaster.eventBus.register(this)
    }

    override fun unbindEvents() {
        eventsActive = false
        BubbleBlaster.eventBus.unregister(this)
    }

    override fun eventsAreBound(): Boolean {
        return eventsActive
    }

    open fun render(gg: GraphicsProcessor) {
        var vx = visualX
        if (visualX == null) vx = x
        var vy = visualY
        if (visualY == null) vy = y
        val bounds = Rectangle(vx!!, vy!!, bounds.width, bounds.height)
        val mousePos = instance().currentPoint
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.instance.textCursor)
                hovered = true
            } else {
                if (hovered) {
                    Util.setCursor(BubbleBlaster.instance.defaultCursor)
                    hovered = false
                }
            }
        }
        if (activated) {
            gg.color(Color(128, 128, 128))
            gg.shapeF(bounds)
            val old = gg.paint()
            val p = GradientPaint(0F, vy.toFloat(), Color(0, 192, 255), 0f, (vy + height).toFloat(), Color(0, 255, 192))
            gg.paint(p)
            gg.shapeF(Rectangle(bounds.x, bounds.y + bounds.height, bounds.width, 4))
            gg.paint(old)
        } else {
            gg.color(Color(64, 64, 64))
            gg.shapeF(bounds)
        }
        gg.color(Color(255, 255, 255, 255))
        drawLeftAnchoredString(gg, text, Point2D.Double(2.0, (y + height - (height - 4)).toDouble()), (height - 4).toDouble(), defaultFont)
        val fontMetrics = gg.fontMetrics(defaultFont)
        var cursorX: Int
        gg.color(Color(0, 192, 192, 255))
        if (cursorIndex >= text.length) {
            cursorX = if (text.isNotEmpty()) {
                fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 2 + x
            } else {
                x
            }
            cursorX += x
            gg.line(cursorX, y + 2, cursorX, y + height - 2)
            gg.line(cursorX + 1, y + 2, cursorX + 1, y + height - 2)
        } else {
            cursorX = if (text.isNotEmpty()) {
                fontMetrics.stringWidth(text.substring(0, cursorIndex)) + x
            } else {
                x
            }
            val width = fontMetrics.charWidth(text[cursorIndex])
            gg.line(cursorX, y + height - 2, cursorX + width, y + height - 2)
            gg.line(cursorX, y + height - 1, cursorX + width, y + height - 1)
        }
    }

    open fun tick() {}

    class Builder {
        var bounds: Rectangle? = null
        private var text = ""
        private var renderPriority: RenderEventPriority? = null
        fun build(): OptionsTextEntry {
            requireNotNull(bounds) { "Missing bounds for creating OptionsTextEntry." }
            val obj: OptionsTextEntry = if (renderPriority == null) OptionsTextEntry(bounds!!.x, bounds!!.y, bounds!!.width, bounds!!.height) else OptionsTextEntry(bounds!!.x, bounds!!.y, bounds!!.width, bounds!!.height, renderPriority)
            obj.text = text
            return obj
        }

        fun bounds(_bounds: Rectangle?): Builder {
            this.bounds = _bounds
            return this
        }

        fun text(_text: String): Builder {
            this.text = _text
            return this
        }

        fun renderPriority(_renderPriority: RenderEventPriority?): Builder {
            this.renderPriority = _renderPriority
            return this
        }
    }
}