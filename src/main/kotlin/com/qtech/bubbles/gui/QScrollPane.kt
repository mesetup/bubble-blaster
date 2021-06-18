package com.qtech.bubbles.gui

import com.google.common.annotations.Beta
import com.qtech.bubbles.graphics.GraphicsProcessor

@Beta
class QScrollPane : QContainer() {
    var viewport: QViewport? = null
    override fun renderComponents(ngg: GraphicsProcessor) {}
    override fun render(ngg: GraphicsProcessor) {}
    override fun renderComponent(ngg: GraphicsProcessor) {}
    override fun tick() {}
    override fun add(component: QComponent): QComponent {
        viewport!!.components!!.add(component)
        return component
    }

    override fun remove(component: QComponent): QComponent {
        viewport!!.components!!.remove(component)
        return component
    }
}