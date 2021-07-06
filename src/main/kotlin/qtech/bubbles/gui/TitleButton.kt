package qtech.bubbles.gui

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.interfaces.Listener
import qtech.bubbles.core.controllers.MouseController.Companion.instance
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.bubbles.event.MouseEvent
import qtech.bubbles.event.RenderEventPriority
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.type.MouseEventType
import qtech.hydro.Border
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.media.AudioSlot
import qtech.bubbles.util.Util
import java.awt.*
import java.awt.geom.Rectangle2D
import java.net.URISyntaxException
import java.util.*

class TitleButton private constructor(x: Int, y: Int, width: Int, height: Int, var renderEventPriority: RenderEventPriority = RenderEventPriority.AFTER_FILTER) : AbstractButton(), Listener {
    private val hash: Long = System.nanoTime()
    var bounds: Rectangle = Rectangle(x, y, width, height)
    var isHovered = false
        private set
    var isPressed = false
        private set
    private var eventsActive = false
    var text: String? = null

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
        private var _bounds = Rectangle(10, 10, 96, 48)
        private var _text = ""
        private var _renderPriority = RenderEventPriority.AFTER_FILTER
        private var command: () -> Unit = {}
        fun build(): TitleButton {
            val button = TitleButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority)
            button.text = _text
            button.command = command
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

        fun command(command: () -> Unit): Builder {
            this.command = command
            return this
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as TitleButton
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
                command.invoke()
            }
            isPressed = false
        }
    }

    override lateinit var command: () -> Unit

    //
    //    @SubscribeEvent
    //    public void onMouseMotion(MouseMotionEvent evt) {
    //        if (bounds.contains(evt.getParentEvent().getPoint())) {
    //            QBubbles.setCursor(Game.instance().getPointerCursor());
    //            hovered = true;
    //        } else {
    //            if (hovered) {
    //                QBubbles.setCursor(Game.instance().getDefaultCursor());
    //                hovered = false;
    //            }
    //        }
    //    }
    override fun paint(gp: GraphicsProcessor) {
//        hovered = MouseController.instance().getCurrentPoint() != null && bounds.contains(MouseController.instance().getCurrentPoint());
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
        gp.color(Color(96, 96, 96))
        gp.shapeF(bounds)
        if (isPressed && instance().currentPoint != null && bounds.contains(instance().currentPoint!!)) {
            // Shadow
            val old = gp.paint()
            val shiftX = bounds.width.toDouble() * 2 * BubbleBlaster.ticks / (BubbleBlaster.TPS * 10)
            val p = GradientPaint(bounds.x + (shiftX.toFloat() - bounds.width), 0F, Color(0, 192, 255), bounds.x + shiftX.toFloat(), 0f, Color(0, 255, 192), true)
            gp.color(Color(72, 72, 72))
            gp.shapeF(bounds)
            val border = Border(0, 0, 1, 0)
            border.paint = p
            border.paintBorder(gp, bounds.x, bounds.y, bounds.width, bounds.height)
            gp.paint(old)

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds);
            textColor = Color.white
        } else if (isHovered) {
            gp.stroke(BasicStroke(4.0f))

            // Shadow
            val old = gp.paint()
            val shiftX = bounds.width.toDouble() * 2 * BubbleBlaster.ticks / (BubbleBlaster.TPS * 10)
            val p = GradientPaint(bounds.x + (shiftX.toFloat() - bounds.width), 0F, Color(0, 192, 255), bounds.x + shiftX.toFloat(), 0f, Color(0, 255, 192), true)
            //            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
            val border = Border(0, 0, 2, 0)
            border.paint = p
            border.paintBorder(gp, bounds.x, bounds.y, bounds.width, bounds.height)
            gp.paint(old)
            //            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds);
            textColor = Color(255, 255, 255)
        } else {
            gp.stroke(BasicStroke(1.0f))

//            gg.setColor(new Color(255, 255, 255, 128));
//            gg.draw(bounds);
//            Border border = new Border(1, 1, 1, 1);
//            border.setPaint(new Color(255, 255, 255, 128));
//            border.paintBorder(QBubbles.getInstance(), gg, bounds.x, bounds.y, bounds.width, bounds.height);
            textColor = Color(224, 224, 224)
        }
        gp.stroke(oldStroke)
        val gg1 = gp.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2)
        gg1.color(textColor)
        drawCenteredString(gg1, text!!, Rectangle2D.Double(0.0, 0.0, (bounds.width - 2).toDouble(), (bounds.height - 2).toDouble()), Font(BubbleBlaster.instance.font.name, Font.BOLD, 16))
        gg1.dispose()
    }

    override fun destroy() {}
    @Suppress("UNUSED_PARAMETER")
    fun tick(gameMode: AbstractGameMode?) {}
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