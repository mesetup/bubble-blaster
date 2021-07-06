package qtech.bubbles.common.graphics.shapes

import qtech.bubbles.core.utils.categories.Constants
import qtech.bubbles.core.utils.categories.IntersectionUtils
import java.awt.Polygon

class Polygon : Shape {
    var points: Array<Point>

    constructor(points: Array<Point>) {
        this.points = points
    }

    constructor(polygon: Polygon) {
        val xps = polygon.xpoints
        val yps = polygon.ypoints
        val np = polygon.npoints
        val points1 = ArrayList<Point>()
        val points2 = arrayOf<Point>()
        for (i in 0 until np) {
            val xp = xps[i]
            val yp = yps[i]
            points1.add(Point(xp.toDouble(), yp.toDouble()))
        }
        points1.toArray(points2)
        points = points2
    }

    override fun doIntersect(shape: Shape): Boolean {
        return when (shape) {
            is Circle -> IntersectionUtils.doIntersect(
                shape,
                this
            )
            is Line -> IntersectionUtils.doIntersect(
                this,
                shape
            )
            else -> throw UnsupportedOperationException(Constants.UNSUPPORTED_SHAPE)
        }
    }
}