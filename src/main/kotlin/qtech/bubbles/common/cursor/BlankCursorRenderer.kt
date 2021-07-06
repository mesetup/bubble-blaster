package qtech.bubbles.common.cursor

import qtech.bubbles.common.renderer.CursorRenderer
import qtech.hydro.GraphicsProcessor
import java.awt.Point

class BlankCursorRenderer : CursorRenderer("blank_cursor") {
    override val hotSpot: Point
        get() = Point(16, 16)

    override fun draw(g: GraphicsProcessor) {}
}