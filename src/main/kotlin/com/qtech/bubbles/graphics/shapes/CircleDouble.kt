package com.qtech.bubbles.graphics.shapes

import java.awt.Rectangle
import java.awt.geom.Dimension2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

@Suppress("DeprecatedCallableAddReplaceWith")
class CircleDouble : Ellipse2D() {
    private var x = 0.0
    override fun getX(): kotlin.Double {
        return x
    }

    private var y = 0.0
    override fun getY(): kotlin.Double {
        return y
    }

    var radius = 0.0

    override fun getWidth(): kotlin.Double {
        return radius
    }

    override fun getHeight(): kotlin.Double {
        return radius
    }

    fun setX(x: kotlin.Double) {
        this.x = x
    }

    fun setY(y: kotlin.Double) {
        this.y = y
    }

    fun setWidth(width: kotlin.Double) {
        radius = width
    }

    fun setHeight(height: kotlin.Double) {
        radius = height
    }

    override fun isEmpty(): Boolean {
        return radius == 0.0
    }

    @Deprecated("use {@link #setCircle(double, double, double) instead.}")
    override fun setFrame(x: kotlin.Double, y: kotlin.Double, width: kotlin.Double, height: kotlin.Double) {
        require(width == height) { "Width and height not equal" }
        this.x = x
        this.y = y
        radius = width
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

    fun setCircle(x: kotlin.Double, y: kotlin.Double, radius: kotlin.Double) {
        this.x = x
        this.y = y
        this.radius = radius
    }

    fun setCircleFromCenter(centerX: kotlin.Double, centerY: kotlin.Double, radius: kotlin.Double) {
        x = centerX - radius / 2
        y = centerY - radius / 2
        this.radius = radius
    }

    fun setCircleFromDiagonal(centerX: kotlin.Double, centerY: kotlin.Double, distance: kotlin.Double) {
        x = centerX - distance
        y = centerY - distance
        radius = distance * 2
    }

    override fun getBounds2D(): Rectangle2D {
        return Rectangle2D.Double(x, y, radius, radius)
    }

    override fun getBounds(): Rectangle {
        return bounds2D.bounds
    }
}