package qtech.bubbles.gui

import qtech.hydro.GraphicsProcessor
import java.util.function.Consumer

class QPanel : QContainer() {
    override fun renderComponents(ngg: GraphicsProcessor) {
        components!!.forEach(Consumer { c: QComponent -> c.render(ngg) })
    }

    override fun render(ngg: GraphicsProcessor) {
        val ngg2 = ngg.create(x, y, width, height)
        renderComponent(ngg2)
        ngg2.dispose()
        val ngg3 = ngg.create(x, y, width, height)
        renderComponents(ngg3)
        ngg3.dispose()
    }

    override fun renderComponent(ngg: GraphicsProcessor) {
        ngg.color(backgroundColor)
        ngg.shapeF(bounds)
    }

    override fun tick() {}
}