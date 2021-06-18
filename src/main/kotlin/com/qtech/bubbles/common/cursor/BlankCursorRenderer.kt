package com.qtech.bubbles.common.cursor

import com.qtech.bubbles.common.renderer.CursorRenderer
import com.qtech.bubbles.graphics.GraphicsProcessor

class BlankCursorRenderer : CursorRenderer("blank_cursor") {
    override fun draw(g: GraphicsProcessor) {}
}