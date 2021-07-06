package qtech.hydro.shapes

import java.awt.Rectangle
import java.awt.geom.Dimension2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

@Suppress("DeprecatedCallableAddReplaceWith")
class CircleFloat : Ellipse2D() {
    var x = 0f
    var y = 0f
    var radius = 0f
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

    fun setWidth(width: kotlin.Float) {
        radius = width
    }

    fun setHeight(height: kotlin.Float) {
        radius = height
    }

    override fun isEmpty(): Boolean {
        return radius == 0f
    }

    @Deprecated("use {@link #setCircle(float, float, float) instead.}")
    override fun setFrame(x: kotlin.Double, y: kotlin.Double, width: kotlin.Double, height: kotlin.Double) {
        require(width == height) { "Width and height not equal" }
        this.x = x.toFloat()
        this.y = y.toFloat()
        radius = width.toFloat()
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

    fun setCircle(x: kotlin.Float, y: kotlin.Float, radius: kotlin.Float) {
        this.x = x
        this.y = y
        this.radius = radius
    }

    fun setCircleFromCenter(centerX: kotlin.Float, centerY: kotlin.Float, radius: kotlin.Float) {
        x = centerX - radius / 2
        y = centerY - radius / 2
        this.radius = radius
    }

    fun setCircleFromDiagonal(centerX: kotlin.Float, centerY: kotlin.Float, distance: kotlin.Float) {
        x = centerX - distance
        y = centerY - distance
        radius = distance * 2
    }

    override fun getBounds2D(): Rectangle2D {
        return Rectangle2D.Float(x, y, radius, radius)
    }

    override fun getBounds(): Rectangle {
        return bounds2D.bounds
    }
}