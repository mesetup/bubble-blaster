package com.qtech.utilities

import java.awt.Color
import java.awt.Point
import java.io.Serializable

class Pixel : Serializable {
    private val color: Color
    private val pos: Point

    constructor(x: Int, y: Int, color: Color) {
        pos = Point(x, y)
        this.color = color
    }

    constructor(pos: Point, color: Color) {
        this.pos = pos
        this.color = color
    }

    fun getColor(): Color {
        return color
    }

    fun getPos(): Point {
        return pos
    }

    fun getX(): Int {
        return pos.x
    }

    fun getY(): Int {
        return pos.y
    }
}