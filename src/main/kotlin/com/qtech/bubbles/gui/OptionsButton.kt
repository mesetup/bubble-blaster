package com.qtech.bubbles.gui

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.interfaces.Listener
import com.qtech.bubbles.core.controllers.MouseController.Companion.instance
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.event.MouseEvent
import com.qtech.bubbles.event.RenderEventPriority
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.type.MouseEventType
import com.qtech.bubbles.graphics.Border
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.media.AudioSlot
import com.qtech.bubbles.util.Util
import java.awt.*
import java.awt.geom.Rectangle2D
import java.net.URISyntaxException
import java.util.*

open class OptionsButton protected constructor(x: Int, y: Int, width: Int, height: Int, var renderEventPriority: RenderEventPriority = RenderEventPriority.AFTER_FILTER) : AbstractButton(), Listener {
    protected var pressedTime: Long = 0
    protected val hash: Long = System.nanoTime()
    var bounds: Rectangle = Rectangle(x, y, width, height)
    var isHovered = false
        protected set
    var isPressed = false
        protected set
    protected var eventsActive = false
    override var command: () -> Unit = { }
    var text: String? = null
    var visualX: Int? = null
    var visualY: Int? = null
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

    class Builder {
        private var bounds = Rectangle(10, 10, 96, 48)
        private var text = ""
        private var renderPriority = RenderEventPriority.AFTER_FILTER
        private var command: () -> Unit = {}
        fun build(): OptionsButton {
            val button = OptionsButton(bounds.x, bounds.y, bounds.width, bounds.height, renderPriority)
            button.text = text
            button.command = command
            return button
        }

        fun bounds(bounds: Rectangle): Builder {
            this.bounds = bounds
            return this
        }

        fun bounds(x: Int, y: Int, width: Int, height: Int): Builder {
            bounds = Rectangle(x, y, width, height)
            return this
        }

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun renderPriority(renderPriority: RenderEventPriority): Builder {
            this.renderPriority = renderPriority
            return this
        }

        fun command(command: () -> Unit): Builder {
            this.command = command
            return this
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as OptionsButton
        return hash == that.hash
    }

    override fun hashCode(): Int {
        return Objects.hash(hash)
    }

    @SubscribeEvent
    fun onMouse(evt: MouseEvent) {
        if (evt.type === MouseEventType.PRESS) {
            if (bounds.contains(evt.point)) {
                pressedTime = System.currentTimeMillis()
                isPressed = true
            }
        } else if (evt.type === MouseEventType.RELEASE) {
            if (bounds.contains(evt.point)) {
                command.invoke()
            }
            isPressed = false
        }
    }

    override fun paint(gp: GraphicsProcessor) {
        var vx = visualX
        if (visualX == null) vx = x
        var vy = visualY
        if (visualY == null) vy = y
        val bounds = Rectangle(vx!!, vy!!, bounds.width, bounds.height)
        val mousePos = instance().currentPoint
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.instance.pointerCursor)
                if (!isHovered) {
                    try {
                        val focusChangeSFX = AudioSlot(Objects.requireNonNull(javaClass.getResource("/assets/bubbleblaster/audio/sfx/ui/button/focus_change.wav")), "focusChange")
                        focusChangeSFX.volume = 0.2
                        focusChangeSFX.play()
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }
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

            // Shadow
            val old = gp.paint()
            val p = GradientPaint(0F, vy.toFloat(), Color(0, 192, 255), 0f, (vy + height).toFloat(), Color(0, 255, 192))
            gp.paint(p)
            gp.shapeF(bounds)
            gp.paint(old)

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds);
            textColor = Color.white
        } else if (isHovered) {
            gp.stroke(BasicStroke(2.0f))
            gp.color(Color(128, 128, 128))
            gp.shapeF(bounds)

            // ShadowQ
//            Paint old = gg.getPaint();
            val shiftX = bounds.width.toDouble() * 2 * BubbleBlaster.ticks / (BubbleBlaster.TPS * 10)
            val p = GradientPaint(bounds.x + (shiftX.toFloat() - bounds.width), 0F, Color(0, 192, 255), bounds.x + shiftX.toFloat(), 0f, Color(0, 255, 192), true)
            //            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
//            gg.setPaint(old);
            val border = Border(0, 0, 2, 0)
            border.paint = p
            border.paintBorder(gp, bounds.x, bounds.y, bounds.width, bounds.height)
            //            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds);
            textColor = Color(255, 255, 255)
        } else {
            gp.stroke(BasicStroke(1.0f))
            gp.color(Color(128, 128, 128))
            gp.shapeF(bounds)

//            gg.setColor(new Color(128, 128, 128));
//            gg.draw(bounds);
            textColor = Color(192, 192, 192)
        }
        gp.stroke(oldStroke)
        val gg1 = gp.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2)
        gg1.color(textColor)
        drawCenteredString(gg1, text!!, Rectangle2D.Double(0.0, 0.0, (bounds.width - 2).toDouble(), (bounds.height - 2).toDouble()), Font(BubbleBlaster.instance.font.name, Font.BOLD, 16))
        gg1.dispose()
    }

    override fun destroy() {}
    open fun tick(gameMode: AbstractGameMode?) {}
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

}