package com.qtech.bubbles.gui

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.core.controllers.MouseController.Companion.instance
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import com.qtech.bubbles.event.KeyboardEvent
import com.qtech.bubbles.event.MouseEvent
import com.qtech.bubbles.event.RenderEventPriority
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.type.KeyEventType
import com.qtech.bubbles.graphics.Border
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.media.AudioSlot
import com.qtech.bubbles.util.Util
import com.qtech.bubbles.util.helpers.MathHelper
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.net.URISyntaxException
import java.util.*

class OptionsNumberInput : OptionsTextEntry {
    private val upButton: NumberInputButton
    private val downButton: NumberInputButton
    private var eventsBound = false

    // Value
    var value: Int
        set(value) {
            field = MathHelper.clamp(value, min, max)
        }
    var min: Int
        set(value) {
            field = value
            this.value = MathHelper.clamp(value, min, max)
        }
    var max: Int
        set(value) {
            field = value
            this.value = MathHelper.clamp(value, min, max)
        }

    constructor(bounds: Rectangle, value: Int, min: Int, max: Int) : super(bounds) {
        this.value = value
        this.min = min
        this.max = max
        upButton = NumberInputButton(0, 0, 0, 0)
        downButton = NumberInputButton(0, 0, 0, 0)
        upButton.command = { add() }
        downButton.command = { this.subtract() }
        text = value.toString()
        cursorIndex = value.toString().length
    }

    constructor(bounds: Rectangle, renderEventPriority: RenderEventPriority?, value: Int, min: Int, max: Int) : super(bounds, renderEventPriority) {
        this.value = value
        this.min = min
        this.max = max
        upButton = NumberInputButton(0, 0, 0, 0)
        downButton = NumberInputButton(0, 0, 0, 0)
        upButton.command = { add() }
        downButton.command = { this.subtract() }
        text = value.toString()
        cursorIndex = value.toString().length
    }

    constructor(x: Int, y: Int, width: Int, height: Int, value: Int, min: Int, max: Int) : super(x, y, width, height) {
        this.value = value
        this.min = min
        this.max = max
        upButton = NumberInputButton(0, 0, 0, 0)
        downButton = NumberInputButton(0, 0, 0, 0)
        upButton.command = { add() }
        downButton.command = { this.subtract() }
        text = value.toString()
        cursorIndex = value.toString().length
    }

    constructor(x: Int, y: Int, width: Int, height: Int, renderEventPriority: RenderEventPriority?, value: Int, min: Int, max: Int) : super(x, y, width, height, renderEventPriority) {
        this.value = value
        this.min = min
        this.max = max
        upButton = NumberInputButton(0, 0, 0, 0)
        downButton = NumberInputButton(0, 0, 0, 0)
        upButton.command = { add() }
        downButton.command = { this.subtract() }
        text = value.toString()
        cursorIndex = value.toString().length
    }

    private fun add() {
        value = MathHelper.clamp(value + 1, min, max)
        text = value.toString()
    }

    private fun subtract() {
        value = MathHelper.clamp(value - 1, min, max)
        text = value.toString()
    }

    @SubscribeEvent
    override fun onMouse(evt: MouseEvent) {
        super.onMouse(evt)
        if (!activated) {
            try {
                value = MathHelper.clamp(text.toInt(), min, max)
                if (text != value.toString()) cursorIndex = value.toString().length
                text = value.toString()
            } catch (e: NumberFormatException) {
                value = MathHelper.clamp(0, min, max)
                text = value.toString()
                cursorIndex = text.length
            }
        }
    }

    @SubscribeEvent
    override fun onKeyboard(evt: KeyboardEvent) {
        if (!activated) return
        if (evt.type === KeyEventType.PRESS || evt.type === KeyEventType.HOLD) {
            if (evt.parentEvent.keyCode == KeyEvent.VK_BACK_SPACE) {
                if (cursorIndex == 0) return
                val leftText = text.substring(0, cursorIndex - 1)
                val rightText = text.substring(cursorIndex)
                text = leftText + rightText
                cursorIndex--
                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_LEFT) {
                cursorIndex--
                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length)
                return
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_RIGHT) {
                cursorIndex++
                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length)
                return
            }
            var c = evt.parentEvent.keyChar
            if (evt.parentEvent.keyCode == KeyEvent.VK_DEAD_ACUTE) {
                c = '\''
            }
            if (evt.parentEvent.keyCode == KeyEvent.VK_QUOTEDBL) {
                c = '"'
            }
            if ("0123456789".contains(c.toString())) {
//                text += c;
                val leftText = text.substring(0, cursorIndex)
                val rightText = text.substring(cursorIndex)
                text = leftText + c + rightText
                cursorIndex++
                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length)
            }
        }
    }

    override fun bindEvents() {
        eventsBound = true
        upButton.bindEvents()
        downButton.bindEvents()
        BubbleBlaster.eventBus.register(this)
    }

    override fun unbindEvents() {
        eventsBound = false
        upButton.unbindEvents()
        downButton.unbindEvents()
        BubbleBlaster.eventBus.unregister(this)
    }

    override fun eventsAreBound(): Boolean {
        return eventsBound
    }

    override fun render(gg: GraphicsProcessor) {
        var vx = visualX!!
        if (visualX == null) vx = x
        var vy = visualY!!
        if (visualY == null) vy = y
        val bounds = Rectangle(vx, vy, bounds.width - 24, bounds.height)
        val mousePos = instance().currentPoint
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.instance.textCursor)
                hovered = true
            } else {
                var vx1 = visualX
                if (visualX == null) vx1 = x
                var vy1 = visualY
                if (visualY == null) vy1 = y
                val bounds1 = Rectangle(vx1!!, vy1!!, bounds.width, bounds.height)
                if (bounds1.contains(mousePos)) {
                    Util.setCursor(BubbleBlaster.instance.pointerCursor)
                    hovered = true
                } else {
                    if (hovered) {
                        Util.setCursor(BubbleBlaster.instance.defaultCursor)
                        hovered = false
                    }
                }
            }
        }
        upButton.y = vy
        upButton.x = (vx + this.bounds.width - 24)
        upButton.height = (this.bounds.height / 2)
        upButton.width = 24
        upButton.text = "+"
        downButton.y = (vy + this.bounds.height / 2)
        downButton.x = (vx + this.bounds.width - 24)
        downButton.height = (this.bounds.height / 2)
        downButton.width = 24
        downButton.text = "-"
        if (activated) {
            gg.color(Color(128, 128, 128))
            gg.shapeF(bounds)
            val old = gg.paint()
            val p = GradientPaint(vx.toFloat(), 0F, Color(0, 192, 255), (vx + width).toFloat(), 0F, Color(0, 255, 192))
            gg.paint(p)
            gg.shapeF(Rectangle(bounds.x, bounds.y + bounds.height - 2, bounds.width, 2))
            gg.paint(old)
        } else {
            gg.color(Color(79, 79, 79))
            gg.shapeF(bounds)
        }
        val gg1 = gg.create(bounds.x, bounds.y, bounds.width, bounds.height)
        gg1.color(Color(255, 255, 255, 255))
        cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length)
        drawLeftAnchoredString(gg1, text, Point2D.Double(8.0, (bounds.height - (bounds.height - 5)).toDouble()), (bounds.height - 5).toDouble(), defaultFont)
        val fontMetrics = gg.fontMetrics(defaultFont)
        upButton.paint(gg)
        downButton.paint(gg)
        val cursorX: Int
        gg1.color(Color(0, 192, 192, 255))
        if (cursorIndex >= text.length) {
            cursorX = if (text.isNotEmpty()) {
                fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 8
            } else {
                10
            }
            gg1.line(cursorX, 2, cursorX, bounds.height - 5)
            gg1.line(cursorX + 1, 2, cursorX + 1, bounds.height - 5)
        } else {
            cursorX = if (text.isNotEmpty()) {
                fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 8
            } else {
                10
            }
            val width = fontMetrics.charWidth(text[cursorIndex])
            gg1.line(cursorX, bounds.height - 5, cursorX + width, bounds.height - 5)
            gg1.line(cursorX, bounds.height - 4, cursorX + width, bounds.height - 4)
        }
        gg1.dispose()
    }

    @SubscribeEvent
    override fun tick() {
    }

    open class NumberInputButton : OptionsButton {
        private var previousCommand: Long = 0

        constructor(x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height)
        protected constructor(x: Int, y: Int, width: Int, height: Int, renderEventPriority: RenderEventPriority) : super(x, y, width, height, renderEventPriority)

        override fun paint(gp: GraphicsProcessor) {
            var vx = visualX
            if (visualX == null) vx = x
            var vy = visualY
            if (visualY == null) vy = y
            val bounds1 = Rectangle(vx!!, vy!!, bounds.width, bounds.height)
            val mousePos = instance().currentPoint
            if (mousePos != null) {
                if (bounds1.contains(mousePos)) {
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
            if (isPressed && instance().currentPoint != null && bounds1.contains(instance().currentPoint!!)) {

                // Shadow
                val old = gp.paint()
                val p = GradientPaint(0F, vy.toFloat(), Color(0, 192, 255), 0f, (vy + height).toFloat(), Color(0, 255, 192))
                gp.paint(p)
                gp.shapeF(bounds1)
                gp.paint(old)

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds1);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds1);
                textColor = Color.white
            } else if (isHovered) {
                gp.stroke(BasicStroke(2.0f))
                gp.color(Color(128, 128, 128))
                gp.shapeF(bounds1)

                // ShadowQ
//            Paint old = gg.getPaint();
                val shiftX = bounds1.width.toDouble() * 2 * BubbleBlaster.ticks / (BubbleBlaster.TPS * 10)
                val p = GradientPaint(bounds1.x + (shiftX.toFloat() - bounds1.width), 0F, Color(0, 192, 255), bounds1.x + shiftX.toFloat(), 0f, Color(0, 255, 192), true)
                //            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds1.x + 1, bounds1.y + 1, bounds1.width - 2, bounds1.height - 2));
//            gg.setPaint(old);
                val border = Border(0, 0, 2, 0)
                border.paint = p
                border.paintBorder(gp, bounds1.x, bounds1.y, bounds1.width, bounds1.height)
                //            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds1);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds1);
                textColor = Color(255, 255, 255)
            } else {
                gp.stroke(BasicStroke(1.0f))
                gp.color(Color(128, 128, 128))
                gp.shapeF(bounds1)

//            gg.setColor(new Color(128, 128, 128));
//            gg.draw(bounds1);
                textColor = Color(192, 192, 192)
            }
            gp.stroke(oldStroke)
            val gg1 = gp.create(bounds1.x + 1, bounds1.y + 1, bounds1.width - 2, bounds1.height - 2)
            gg1.color(textColor)
            drawCenteredString(gg1, text!!, Rectangle2D.Double(0.0, 0.0, (bounds1.width - 2).toDouble(), (bounds1.height - 2).toDouble()), Font(BubbleBlaster.instance.font.name, Font.BOLD, 16))
            gg1.dispose()
        }

        @SubscribeEvent
        override fun tick(gameMode: AbstractGameMode?) {
            super.tick(gameMode)
            if (isPressed) {
                if (pressedTime + 1000 < System.currentTimeMillis()) {
                    if (previousCommand < System.currentTimeMillis()) {
                        previousCommand = System.currentTimeMillis() + 25
                        command.invoke()
                    }
                }
            }
        }
    }
}