package qtech.bubbles.gui

import qtech.hydro.GraphicsProcessor
import java.awt.Color

class QLabel(var text: String) : QComponent() {
    var isWrapped = false
    var foregroundColor: Color
    override fun render(ngg: GraphicsProcessor) {
        val ngg1 = ngg.create(x, y, width, height)
        renderComponent(ngg1)
        ngg1.dispose()
    }

    override fun renderComponent(ngg: GraphicsProcessor) {
        ngg.color(backgroundColor)
        ngg.shapeF(bounds)
        ngg.color(foregroundColor)
        if (isWrapped) ngg.wrappedText(text, 0, 0, width) else ngg.multilineText(text, 0, 0)
    }

    override fun tick() {}

    init {
        backgroundColor = Color(0, 0, 0, 0)
        foregroundColor = Color.white
    }
}