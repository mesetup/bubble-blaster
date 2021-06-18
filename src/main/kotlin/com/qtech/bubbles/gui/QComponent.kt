package com.qtech.bubbles.gui

import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

abstract class QComponent {
    var x = 0
    var y = 0
    var width = 0
    var height = 0
    var backgroundColor: Color? = null
    private val graphics: GraphicsProcessor? = null
    abstract fun render(ngg: GraphicsProcessor)
    abstract fun renderComponent(ngg: GraphicsProcessor)
    abstract fun tick()
    var bounds: Rectangle
        get() = Rectangle(x, y, width, height)
        set(bounds) {
            x = bounds.x
            y = bounds.y
            width = bounds.width
            height = bounds.height
        }
    var size: Dimension
        get() = Dimension(width, height)
        set(size) {
            width = size.width
            height = size.height
        }
    var location: Point
        get() = Point(x, y)
        set(location) {
            x = location.x
            y = location.y
        }
}