package com.qtech.bubbles.gui

import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Dimension

class QScrollBar : QComponent() {
    private val virtualSize: Dimension? = null
    override fun render(ngg: GraphicsProcessor) {}
    override fun renderComponent(ngg: GraphicsProcessor) {}
    override fun tick() {}
}