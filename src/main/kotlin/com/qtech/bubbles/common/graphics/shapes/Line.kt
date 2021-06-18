package com.qtech.bubbles.common.graphics.shapes

import com.qtech.bubbles.core.utils.categories.Constants
import com.qtech.bubbles.core.utils.categories.IntersectionUtils
import kotlin.math.abs

class Line : Shape {
    var slope = 0.0
        private set
    var yIntercept = 0.0
    var pointA: Point? = null
    var pointB: Point? = null

    /**
     * Constructs a Line object given 2 points
     * Point A and Point B
     */
    constructor(pointA: Point?, pointB: Point?) {
        this.pointA = pointA
        this.pointB = pointB
        calculateAndSetSlope()
        calculateAndSetYIntercept()
    }

    constructor(point: Point?, slope: Double) {
        pointA = point
        this.slope = slope
    }

    constructor(slope: Double, yintercept: Double) {
        this.slope = slope
        this.yIntercept = yintercept
    }

    private fun calculateAndSetSlope() {
        slope = if (abs(pointA!!.pointY - pointB!!.pointY) < Constants.EPSILON) 0.0 else if (abs(pointA!!.pointX - pointB!!.pointX) < Constants.EPSILON) Double.POSITIVE_INFINITY else (pointA!!.pointY - pointB!!.pointY) / (pointA!!.pointX - pointB!!.pointX)
    }

    private fun calculateAndSetYIntercept() {
        yIntercept = if (slope == 0.0) pointA!!.pointY else if (java.lang.Double.isInfinite(slope)) Double.POSITIVE_INFINITY else pointA!!.pointY - slope * pointA!!.pointX
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (pointA == null) 0 else pointA.hashCode()
        result = prime * result + if (pointB == null) 0 else pointB.hashCode()
        var temp: Long = java.lang.Double.doubleToLongBits(slope)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(yIntercept)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val other1 = other as Line
        if (pointA == null) {
            if (other1.pointA != null) return false
        } else if (pointA != other1.pointA) return false
        if (pointB == null) {
            if (other1.pointB != null) return false
        } else if (pointB != other1.pointB) return false
        return if (java.lang.Double.doubleToLongBits(slope) != java.lang.Double
                .doubleToLongBits(other1.slope)
        ) false else java.lang.Double.doubleToLongBits(yIntercept) == java.lang.Double
            .doubleToLongBits(other1.yIntercept)
    }

    override fun doIntersect(shape: Shape): Boolean {
        return if (shape is Circle) IntersectionUtils.doIntersect(
            this,
            shape
        ) else if (shape is Polygon) IntersectionUtils.doIntersect(
            shape,
            this
        ) else if (shape is Line) IntersectionUtils.doIntersectLineSegments(
            this,
            shape
        ) else throw UnsupportedOperationException(Constants.UNSUPPORTED_SHAPE)
    }
}