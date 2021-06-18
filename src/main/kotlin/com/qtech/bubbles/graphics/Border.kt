package com.qtech.bubbles.graphics

import java.awt.Color
import java.awt.Paint

open class Border {
    val borderInsets: Insets
    var isBorderOpaque = false
    var paint: Paint = Color(0, 0, 0)

    constructor(insets: Insets) {
        borderInsets = insets
    }

    constructor(top: Int, left: Int, bottom: Int, right: Int) {
        borderInsets = Insets(top, left, bottom, right)
    }

    open fun paintBorder(g: GraphicsProcessor, x: Int, y: Int, width: Int, height: Int) {
        val insets = borderInsets

        //  Set paint.
        g.paint(paint)

        //  Draw rectangles around the component, but do not draw
        //  in the component area itself.
        g.rectF(x + insets.left, y, width - insets.left - insets.right, insets.top)
        g.rectF(x, y, insets.left, height)
        g.rectF(x + width - insets.right, y, insets.right, height)
        g.rectF(x + insets.left, y + height - insets.bottom, width - insets.left - insets.right, insets.bottom)
    }

    var borderTop: Int
        get() = borderInsets.top
        set(topWidth) {
            borderInsets.top = topWidth
        }
    var borderLeft: Int
        get() = borderInsets.left
        set(leftWidth) {
            borderInsets.left = leftWidth
        }
    var borderBottom: Int
        get() = borderInsets.bottom
        set(bottomWidth) {
            borderInsets.bottom = bottomWidth
        }
    var borderRight: Int
        get() = borderInsets.right
        set(rightWidth) {
            borderInsets.right = rightWidth
        }
}