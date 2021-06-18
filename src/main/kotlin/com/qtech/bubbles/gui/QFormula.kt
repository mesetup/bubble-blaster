package com.qtech.bubbles.gui

import java.awt.geom.Point2D

class QFormula(var formula: IFormula) {
    fun getPoint(x: Double): Point2D.Double {
        val y = formula.calculate(x)
        return Point2D.Double(x, y)
    }

    fun getY(x: Double): Double {
        return formula.calculate(x)
    }
}