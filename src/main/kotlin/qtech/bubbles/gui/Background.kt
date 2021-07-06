package qtech.bubbles.gui

import qtech.bubbles.core.utils.categories.PolygonUtils.buildPolygon
import java.awt.Shape

object Background {
    fun getShape(x: Int, y: Int, width: Int, height: Int): Shape {
        val x1 = 0 * width / 10
        val y1 = -25 * height / 50
        val x2 = 5 * width / 10
        val y2 = -25 * height / 50
        val x3 = 0 * width / 10
        val y3 = +25 * height / 50
        val x4 = -5 * width / 10
        val y4 = +25 * height / 50
        return buildPolygon(x, y, intArrayOf(x1, x2, x3, x4), intArrayOf(y1, y2, y3, y4), 0.0)
    }
}