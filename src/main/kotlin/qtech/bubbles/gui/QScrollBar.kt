package qtech.bubbles.gui

import qtech.hydro.GraphicsProcessor
import java.awt.Dimension

class QScrollBar : QComponent() {
    private val virtualSize: Dimension? = null
    override fun render(ngg: GraphicsProcessor) {}
    override fun renderComponent(ngg: GraphicsProcessor) {}
    override fun tick() {}
}