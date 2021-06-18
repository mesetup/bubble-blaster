package com.qtech.bubbles.common.graphics.shapes

import com.qtech.bubbles.core.utils.categories.Constants
import com.qtech.bubbles.core.utils.categories.IntersectionUtils

class Circle(
    var center: Point,
    var radius: Double
) : Shape {
    override fun toString(): String {
        return "Circle{" +
                "center=" + center +
                ", radius=" + radius +
                '}'
    }

    val awtCenter: java.awt.Point
        get() = java.awt.Point(center.pointX.toInt(), center.pointY.toInt())

    override fun doIntersect(shape: Shape): Boolean {
        return if (shape is Polygon) IntersectionUtils.doIntersect(
            this,
            shape
        ) else if (shape is Line) IntersectionUtils.doIntersect(
            shape,
            this
        ) else throw UnsupportedOperationException(Constants.UNSUPPORTED_SHAPE)
    }
}