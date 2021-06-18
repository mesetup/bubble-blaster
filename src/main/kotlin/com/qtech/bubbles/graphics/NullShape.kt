package com.qtech.bubbles.graphics

import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.geom.PathIterator
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

class NullShape : Shape {
    override fun getBounds(): Rectangle {
        return Rectangle(0, 0, 0, 0)
    }

    override fun getBounds2D(): Rectangle2D {
        return Rectangle2D.Double(.0, .0, .0, .0)
    }

    override fun contains(v: Double, v1: Double): Boolean {
        return false
    }

    override fun contains(point2D: Point2D): Boolean {
        return false
    }

    override fun intersects(v: Double, v1: Double, v2: Double, v3: Double): Boolean {
        return false
    }

    override fun intersects(rectangle2D: Rectangle2D): Boolean {
        return false
    }

    override fun contains(v: Double, v1: Double, v2: Double, v3: Double): Boolean {
        return false
    }

    override fun contains(rectangle2D: Rectangle2D): Boolean {
        return false
    }

    override fun getPathIterator(affineTransform: AffineTransform): PathIterator {
        return NullPathIterator()
    }

    override fun getPathIterator(affineTransform: AffineTransform, v: Double): PathIterator {
        return NullPathIterator()
    }
}