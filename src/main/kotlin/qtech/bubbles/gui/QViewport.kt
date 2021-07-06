package qtech.bubbles.gui

import qtech.hydro.GraphicsProcessor
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

class QViewport(private val viewportRect: Rectangle) : QContainer() {
    override fun renderComponents(ngg: GraphicsProcessor) {
        for (component in components!!) {
            val componentGraphics = ngg.create(component.x, component.y, component.width, component.height)
            component.render(componentGraphics)
            componentGraphics.dispose()
        }
    }

    override fun render(ngg: GraphicsProcessor) {
        renderComponent(ngg)
        val viewportGraphics = ngg.create(viewportRect.x, viewportRect.y, viewportRect.width, viewportRect.height)
        renderComponents(viewportGraphics)
    }

    override fun renderComponent(ngg: GraphicsProcessor) {
        ngg.color(backgroundColor)
        ngg.rectF(0, 0, size.width, size.height)
    }

    override fun tick() {}
    var viewportSize: Dimension?
        get() = viewportRect.size
        set(size) {
            viewportRect.size = size
        }
    var viewportLocation: Point?
        get() = viewportRect.location
        set(location) {
            viewportRect.location = location
        }

    init {
        backgroundColor = Color(64, 64, 64)
    }
}