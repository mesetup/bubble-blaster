package com.qtech.bubbles.common.graphics.shapes

interface Shape {
    @Throws(UnsupportedOperationException::class)
    fun doIntersect(shape: Shape): Boolean
}