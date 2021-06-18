package com.qtech.bubbles.gui

import com.qtech.bubbles.graphics.GraphicsProcessor

abstract class QContainer : QComponent() {
    var components: ArrayList<QComponent>? = null
    abstract fun renderComponents(ngg: GraphicsProcessor)
    open fun add(component: QComponent): QComponent {
        components!!.add(component)
        return component
    }

    open fun remove(component: QComponent): QComponent {
        components!!.remove(component)
        return component
    }

    fun removeComponent(index: Int): QComponent {
        return components!!.removeAt(index)
    }

    fun getComponent(index: Int): QComponent {
        return components!![index]
    }
}