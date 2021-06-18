package com.qtech.bubbles.common.cursor

import com.qtech.bubbles.common.renderer.CursorRenderer
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Color
import java.awt.Polygon
import java.awt.RenderingHints

class HoverCursorRenderer : CursorRenderer("pointer_cursor") {
    override fun draw(g: GraphicsProcessor) {
        val poly = Polygon(intArrayOf(10, 20, 15, 10), intArrayOf(10, 22, 22, 26), 4)
        g.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color(Color.white)
        g.oval(0, 0, 20, 20)
        g.color(Color.white)
        g.oval(2, 2, 16, 16)
        g.color(Color.black)
        g.polyF(poly)
        g.color(Color.white)
        g.poly(poly)
        g.color(Color.black)
        g.oval(1, 1, 18, 18)
        g.dispose()
    }
}