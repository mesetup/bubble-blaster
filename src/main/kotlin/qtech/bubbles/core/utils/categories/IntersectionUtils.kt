package qtech.bubbles.core.utils.categories

import qtech.bubbles.common.graphics.shapes.Circle
import qtech.bubbles.common.graphics.shapes.Line
import qtech.bubbles.common.graphics.shapes.Point
import qtech.bubbles.common.graphics.shapes.Polygon
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * @author akubatoor
 *
 *
 * This class provides the utility methods for
 * calculating various intersection properties of Geometric shapes
 */
object IntersectionUtils {
    /**
     * Returns point of intersection of two given lines
     *
     * @param line1 ?
     * @param line2 ?
     * @return Point of intersection else null
     */
    fun getIntersectionPoint(line1: Line, line2: Line): Point? {
        return if (doIntersect(line1, line2)) {
            val slope1 = line1.slope
            val yIntercept1 = line1.yIntercept
            val slope2 = line2.slope
            val yIntercept2 = line2.yIntercept

            /*
              calculate the intersection point coordinates (X,Y) using the formula
              X = (yIntercept2-yIntercept1)/(slope1-slope2)
              Y = (slope1*X)+yIntercept1
             */
            val x = roundValue((yIntercept2 - yIntercept1) / (slope1 - slope2))
            val y = roundValue(x * slope1 + yIntercept1)
            Point(x, y)
        } else null
    }

    /**
     * Checks if the given two lines intersect
     *
     * @param line1 ?
     * @param line2 ?
     * @return true if the given 2 lines intersect else return false
     */
    fun doIntersect(line1: Line, line2: Line): Boolean {
        val slope1 = line1.slope
        val yIntercept1 = line1.slope
        val slope2 = line2.slope
        val yIntercept2 = line2.yIntercept
        return if (roundValue(abs(slope1 - slope2)) < Constants.EPSILON) {
            roundValue(abs(yIntercept1 - yIntercept2)) < Constants.EPSILON
        } else true
    }

    /**
     * This method checks if a given line segment and circle intersect
     * To check that we construct a new line which is perpendicular to the given line
     * and which passes through the center of the circle and find out the intersection point of 2 lines.
     * If the distance of the circle's center to the intersection point is greater than the radius
     * then it is considered non intersecting.If the distance is less than or equal to radius and if the distance
     * of circle center to the intersectionPoint is greater than the distance of circle's center to either of the line
     * segment points then its considered intersecting
     *
     *
     * Special Cases:
     * (1)INFINITE slope
     * When the slope of the given line is infinite then difference in x coordinates of circle's center
     * and a given point on the line is calculated.If the difference is more than the radius of the circle
     * then the line and circle do not intersect
     *
     *
     * (2)ZERO slope
     * When the slope of the given line is 0 then the difference in y coordinates of the circle's center
     * and given point on the line is calculated.If the difference is more than the radius of the circle
     * then the line and circle do not intersect else they intersect each other
     *
     * @param line   ?
     * @param circle ?
     * @return boolean value indicating if given line and circle intersect
     * @see IntersectionUtils.doIntersect
     */
    fun doIntersect(line: Line, circle: Circle): Boolean {
        //extract the slope of the given line
        val originalSlope = line.slope
        val center = circle.center
        val radius = circle.radius
        val pointA = line.pointA
        val pointB = line.pointB
        val distance: Double
        val intPoint: Point?
        when {
            java.lang.Double.isInfinite(originalSlope) -> {
                distance = abs(center.pointX - pointA!!.pointX)
                intPoint = Point(pointA.pointX, center.pointY)
            }
            originalSlope == 0.0 -> {
                distance = abs(center.pointY - pointA!!.pointY)
                intPoint = Point(center.pointX, pointA.pointY)
            }
            else -> {
                val perpendicularLine = Line(center, -1 / originalSlope)
                intPoint = getIntersectionPoint(line, perpendicularLine)
                distance = distBtwPoints(center, intPoint)
            }
        }
        return (distance <= radius && (distBtwPoints(center, pointA) <= radius
                || distBtwPoints(center, pointB) <= radius)
                || distance <= radius && isOnLineSegment(pointA, pointB, intPoint))
    }

    /**
     * This method checks if a given point is on
     * a line segment.
     *
     * @param pointA   ?
     * @param pointB   ?
     * @param intPoint ?
     * @return boolean true if intPoint falls on a line segment with PointA and PointB as 2 end points
     */
    fun isOnLineSegment(pointA: Point?, pointB: Point?, intPoint: Point?): Boolean {
        //calculate distance between pointA and intPoint
        val distanceAtoInt = distBtwPoints(pointA, intPoint)
        //calculate distance between intPoint and pointB
        val distanceIntToB = distBtwPoints(intPoint, pointB)
        //calculate distance between pointA and pointB
        val distanceAtoB = distBtwPoints(pointA, pointB)
        //calculate the difference in distances.
        val totalDistanceDiff = roundValue(distanceAtoInt + distanceIntToB - distanceAtoB)
        return totalDistanceDiff < Constants.EPSILON
    }

    /**
     * Checks if the given Circle and a Polygon intersect
     *
     * @param circle  ?
     * @param polygon ?
     * @return true if intersect else return false
     */
    fun doIntersect(circle: Circle, polygon: Polygon): Boolean {
        val points = polygon.points
        var doIntersect = false
        if (points.size == 1) {
            //The polygon is just a point
            return isOnCircle(circle, points[0])
        } else if (points.size == 2) {
            return doIntersect(Line(points[0], points[1]), circle)
        } else {
            for (i in points.indices) {
                val line: Line = if (i == points.size - 1) Line(points[i], points[0]) else Line(points[i], points[i + 1])
                if (doIntersect(line, circle)) {
                    doIntersect = true
                    break
                }
            }
        }
        return doIntersect
    }

    fun doIntersect(polygon: Polygon, line: Line): Boolean {
        val points = polygon.points
        var doIntersect = false
        for (i in points.indices) {
            val line2: Line = if (i == points.size - 1) Line(points[i], points[0]) else Line(points[i], points[i + 1])
            if (doIntersectLineSegments(line, line2)) {
                doIntersect = true
                break
            }
        }
        return doIntersect
    }

    /**
     * Calculates the absolute distance between the 2 graphical points by constructing
     * a line using the 2 points.If the slope of the line is 0 then the distance
     * is calculated by taking the difference of x coordinates.If the slope is infinite
     * then the distance is calculated by taking the difference in y coordinates.In other
     * cases the Pythagorean theorem is applied to calculate the hypotenuse
     *
     * @param pointA a
     * @param pointB b
     * @return distance between 2 graphical points rounded to 2 decimal points
     */
    fun distBtwPoints(pointA: Point?, pointB: Point?): Double {
        //construct a line using the 2 points
        val line = Line(pointA, pointB)
        val distance: Double
        val slope = line.slope
        distance = when {
            java.lang.Double.isInfinite(slope) -> abs(pointA!!.pointY - pointB!!.pointY)
            slope == 0.0 -> abs(pointA!!.pointX - pointB!!.pointX)
            else -> { //Apply Pythagorean theorem
                sqrt(
                    (pointA!!.pointY - pointB!!.pointY).pow(2.0)
                            + (pointA.pointX - pointB.pointX).pow(2.0)
                )
            }
        }
        return roundValue(distance)
    }

    fun isOnCircle(circle: Circle, point: Point?): Boolean {
        val distance = distBtwPoints(circle.center, point)
        return distance - circle.radius < Constants.EPSILON
    }

    private fun roundValue(value: Double): Double {
        return (value * 100).roundToLong() / 100.0
    }

    /**
     * Utility method to check if 2 line segments intersect
     *
     * @param line1 1
     * @param line2 2
     * @return boolean indicating if line segments intersect
     */
    fun doIntersectLineSegments(line1: Line, line2: Line): Boolean {
        if (doIntersect(line1, line2)) {
            val pointA = line1.pointA
            val pointB = line1.pointB
            val pointC = line2.pointA
            val pointD = line2.pointB
            return (isOnLineSegment(pointA, pointB, pointC)
                    || isOnLineSegment(pointA, pointB, pointD)
                    || isOnLineSegment(pointC, pointD, pointA)
                    || isOnLineSegment(pointC, pointD, pointB))
        }
        return false
    }
}