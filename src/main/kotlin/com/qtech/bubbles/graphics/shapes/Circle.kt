@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.qtech.bubbles.graphics.shapes

import java.awt.Rectangle
import java.awt.geom.Dimension2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

class Circle : Ellipse2D() {
    var x = 0
    var y = 0
    var radius = 0

    override fun getX(): kotlin.Double {
        return x.toDouble()
    }

    override fun getY(): kotlin.Double {
        return y.toDouble()
    }

    override fun getWidth(): kotlin.Double {
        return radius.toDouble()
    }

    override fun getHeight(): kotlin.Double {
        return radius.toDouble()
    }

    fun setWidth(width: Int) {
        radius = width
    }

    fun setHeight(height: Int) {
        radius = height
    }

    override fun isEmpty(): Boolean {
        return radius == 0
    }

    @Deprecated("use {@link #setCircle(int, int, int) instead.}")
    override fun setFrame(x: kotlin.Double, y: kotlin.Double, width: kotlin.Double, height: kotlin.Double) {
        require(width == height) { "Width and height not equal" }
        this.x = x.toInt()
        this.y = y.toInt()
        radius = width.toInt()
    }

    @Deprecated("")
    override fun setFrame(r: Rectangle2D) {
        super.setFrame(r)
    }

    @Deprecated("")
    override fun setFrame(loc: Point2D, size: Dimension2D) {
        super.setFrame(loc, size)
    }

    @Deprecated("")
    override fun setFrameFromCenter(center: Point2D, corner: Point2D) {
        super.setFrameFromCenter(center, corner)
    }

    @Deprecated("")
    override fun setFrameFromCenter(centerX: kotlin.Double, centerY: kotlin.Double, cornerX: kotlin.Double, cornerY: kotlin.Double) {
        super.setFrameFromCenter(centerX, centerY, cornerX, cornerY)
    }

    @Deprecated("")
    override fun setFrameFromDiagonal(p1: Point2D, p2: Point2D) {
        super.setFrameFromDiagonal(p1, p2)
    }

    fun setCircle(x: Int, y: Int, radius: Int) {
        this.x = x
        this.y = y
        this.radius = radius
    }

    fun setCircleFromCenter(centerX: Int, centerY: Int, radius: Int) {
        x = centerX - radius / 2
        y = centerY - radius / 2
        this.radius = radius
    }

    fun setCircleFromDiagonal(centerX: Int, centerY: Int, distance: Int) {
        x = centerX - distance
        y = centerY - distance
        radius = distance * 2
    }

    override fun getBounds2D(): Rectangle2D {
        return Rectangle(x, y, radius, radius)
    }

    override fun getBounds(): Rectangle {
        return Rectangle(x, y, radius, radius)
    }
}