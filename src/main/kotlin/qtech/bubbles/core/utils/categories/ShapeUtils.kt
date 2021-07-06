package qtech.bubbles.core.utils.categories

import java.awt.Shape
import java.awt.geom.Area

object ShapeUtils {
    @JvmStatic
    fun checkIntersection(shapeA: Shape?, shapeB: Shape?): Boolean {
        val areaA = Area(shapeA)
        areaA.intersect(Area(shapeB))
        return !areaA.isEmpty
    }
}