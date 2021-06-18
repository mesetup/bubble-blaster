package com.qtech.bubbles.gui

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.interfaces.Listener
import com.qtech.bubbles.core.controllers.MouseController.Companion.instance
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.event.MouseEvent
import com.qtech.bubbles.event.RenderEventPriority
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.type.MouseEventType
import com.qtech.bubbles.graphics.Border
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.graphics.OuterBorder
import com.qtech.bubbles.util.Util
import java.awt.*
import java.awt.geom.Rectangle2D
import java.util.*

class CrashButton protected constructor(x: Int, y: Int, width: Int, height: Int, var renderEventPriority: RenderEventPriority) : Widget(), Listener {
    private val hash: Long = System.nanoTime()
    var bounds: Rectangle = Rectangle(x, y, width, height)
    var isHovered = false
        private set
    var isPressed = false
        private set
    private var eventsActive = false
    var command: Runnable? = null
    var text: String? = null

    class Builder {
        private var _bounds = Rectangle(10, 10, 96, 48)
        private var _text = ""
        private var _renderPriority = RenderEventPriority.AFTER_FILTER
        private var _command = Runnable {}
        fun build(): CrashButton {
            val button = CrashButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority)
            button.text = _text
            button.command = _command
            return button
        }

        fun bounds(bounds: Rectangle): Builder {
            _bounds = bounds
            return this
        }

        fun bounds(x: Int, y: Int, width: Int, height: Int): Builder {
            _bounds = Rectangle(x, y, width, height)
            return this
        }

        fun text(text: String): Builder {
            _text = text
            return this
        }

        fun renderPriority(renderPriority: RenderEventPriority): Builder {
            _renderPriority = renderPriority
            return this
        }

        fun command(command: Runnable): Builder {
            _command = command
            return this
        }
    }

    constructor(x: Int, y: Int, width: Int, height: Int) : this(x, y, width, height, RenderEventPriority.AFTER_FILTER) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CrashButton
        return hash == that.hash
    }

    override fun hashCode(): Int {
        return Objects.hash(hash)
    }

    @SubscribeEvent
    fun onMouse(evt: MouseEvent) {
        if (evt.type === MouseEventType.PRESS) {
            if (bounds.contains(evt.point)) {
                isPressed = true
            }
        } else if (evt.type === MouseEventType.RELEASE) {
            if (bounds.contains(evt.point)) {
                command!!.run()
            }
            isPressed = false
            //        } else if (evt.getType() == MouseEventType.CLICK) {
        }
    }

    override fun paint(gp: GraphicsProcessor) {
        val mousePos = instance().currentPoint
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.instance.pointerCursor)
                isHovered = true
            } else {
                if (isHovered) {
                    Util.setCursor(BubbleBlaster.instance.defaultCursor)
                    isHovered = false
                }
            }
        }
        val textColor: Color
        val oldStroke = gp.stroke()
        if (isPressed && instance().currentPoint != null && bounds.contains(instance().currentPoint!!)) {
            val old = gp.paint()
            val p = GradientPaint(0F, bounds.y.toFloat(), Color(255, 0, 0), bounds.width.toFloat(), (bounds.y + bounds.height).toFloat(), Color(255, 64, 0))
            gp.paint(p)
            gp.shapeF(bounds)
            gp.paint(old)
            textColor = Color.white
        } else if (isHovered) {
            gp.stroke(BasicStroke(4.0f))
            val old = gp.paint()
            val p = GradientPaint(0F, bounds.y.toFloat(), Color(255, 0, 0), bounds.width.toFloat(), (bounds.y + bounds.height).toFloat(), Color(255, 64, 0))
            gp.paint(p)
            val border: Border = OuterBorder(2, 2, 2, 2)
            border.paint = p
            border.paintBorder(gp, bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2)
            //            gg.draw(new Rectangle(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4));
            gp.paint(old)
            textColor = Color(255, 255, 255)
        } else {
            gp.stroke(BasicStroke(1.0f))
            gp.color(Color(255, 255, 255))
            val border = Border(1, 1, 1, 1)
            border.paint = Color(255, 255, 255)
            border.paintBorder(gp, bounds.x, bounds.y, bounds.width, bounds.height)
            //            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
            textColor = Color(255, 255, 255)
        }
        gp.stroke(oldStroke)
        val gg1 = gp.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2)
        gg1.color(textColor)
        drawCenteredString(gg1, text!!, Rectangle2D.Double(0.0, 0.0, (bounds.width - 2).toDouble(), (bounds.height - 2).toDouble()), Font(BubbleBlaster.instance.font.name, Font.BOLD, 16))
        gg1.dispose()
    }

    override fun destroy() {
        // Do nothing!
    }

    fun tick() {}
    override fun bindEvents() {
        BubbleBlaster.eventBus.register(this)
        eventsActive = true
        val mousePos = instance().currentPoint
        if (mousePos != null && bounds.contains(mousePos)) {
            Util.setCursor(BubbleBlaster.instance.pointerCursor)
            isHovered = true
        }
    }

    override fun unbindEvents() {
        BubbleBlaster.eventBus.unregister(this)
        eventsActive = false
        if (isHovered) {
            Util.setCursor(BubbleBlaster.instance.defaultCursor)
            isHovered = false
        }
    }

    override fun eventsAreBound(): Boolean {
        return eventsActive
    }

    fun setHeight(height: Int) {
        bounds.height = height
    }

    fun setWidth(width: Int) {
        bounds.width = width
    }

    fun setX(x: Int) {
        bounds.x = x
    }

    fun setY(y: Int) {
        bounds.y = y
    }
}