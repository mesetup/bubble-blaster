package com.qtech.bubbles.common.cursor

import com.qtech.bubbles.common.renderer.CursorRenderer
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Color
import java.awt.RenderingHints

class TextCursorRenderer : CursorRenderer("text_cursor") {
    override fun draw(g: GraphicsProcessor) {
        g.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color(Color.white)
        g.line(0, 1, 0, 24)
        g.color(Color.white)
        g.line(1, 0, 1, 25)
        g.color(Color.white)
        g.line(2, 1, 2, 24)
        g.color(Color.black)
        g.line(1, 1, 1, 24)
        g.dispose()
    }
}