package com.qtech.bubbles.common.cursor

import com.qtech.bubbles.common.renderer.CursorRenderer
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Color
import java.awt.Polygon
import java.awt.RenderingHints

class DefaultCursorRenderer : CursorRenderer("default_cursor") {
    override fun draw(g: GraphicsProcessor) {
        val poly = Polygon(intArrayOf(0, 10, 5, 0), intArrayOf(0, 12, 12, 16), 4)
        g.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color(Color.black)
        g.polyF(poly)
        g.color(Color.white)
        g.poly(poly)
        g.dispose()
    }
}