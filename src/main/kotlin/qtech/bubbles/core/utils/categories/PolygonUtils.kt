package qtech.bubbles.core.utils.categories

import java.awt.Polygon
import java.awt.geom.AffineTransform
import java.awt.geom.Path2D
import java.awt.geom.Point2D

object PolygonUtils {
    /**
     * <h1>Polygon Utilities</h1>
     *
     *
     * Builds a polygon from a set of points, rotated around a point, at the
     * specified rotation angle.
     *
     * @param centerX       the int center x coordinate around which to rotate
     * @param centerY       the int center y coordinate around which to rotate
     * @param xp            the int[] of x points which make up our polygon points. This
     * array is parallel to the yp array where each index in this array
     * corresponds to the same index in the yp array.
     * @param yp            the int[] of y points which make up our polygon points. This
     * array is parallel to the xp array where each index in this array
     * corresponds to the same index in the xp array.
     * @param rotationAngle the double angle in which to rotate the provided
     * coordinates (specified in degrees).
     * @return a Polygon of the provided coordinates rotated around the center point
     * at the specified angle.
     * @throws IllegalArgumentException when the provided x points array is not the
     * same length as the provided y points array
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun buildPolygon(centerX: Int, centerY: Int, xp: IntArray, yp: IntArray, rotationAngle: Double): Polygon {
        // copy the arrays so that we don't manipulate the originals, that way we can
        // reuse them if necessary
        val xPoints = xp.copyOf(xp.size)
        val yPoints = yp.copyOf(yp.size)
        require(xPoints.size == yPoints.size) { "The provided x points are not the same length as the provided y points." }

        // create a list of Point2D pairs
        val list = ArrayList<Point2D>()
        for (i in yPoints.indices) {
            list.add(Point2D.Double(xPoints[i].toDouble(), yPoints[i].toDouble()))
        }

        // create an array which will hold the rotated points
        val rotatedPoints = arrayOfNulls<Point2D>(list.size)

        // rotate the points
        val transform = AffineTransform.getRotateInstance(Math.toRadians(rotationAngle), centerX.toDouble(), centerY.toDouble())
        transform.transform(list.toTypedArray(), 0, rotatedPoints, 0, rotatedPoints.size)

        // build the polygon from the rotated points and return it
        val ixp = IntArray(list.size)
        val iyp = IntArray(list.size)
        for (i in ixp.indices) {
            ixp[i] = rotatedPoints[i]!!.x.toInt()
            iyp[i] = rotatedPoints[i]!!.y.toInt()
        }
        return Polygon(ixp, iyp, ixp.size)
    }

    /**
     * <h1>Polygon Utilities</h1>
     *
     *
     * Builds a polygon from a set of points, rotated around a point, at the
     * specified rotation angle.
     *
     * @param centerX       the double center x coordinate around which to rotate
     * @param centerY       the double center y coordinate around which to rotate
     * @param xp            the double[] of x points which make up our polygon points. This
     * array is parallel to the yp array where each index in this array
     * corresponds to the same index in the yp array.
     * @param yp            the double[] of y points which make up our polygon points. This
     * array is parallel to the xp array where each index in this array
     * corresponds to the same index in the xp array.
     * @param rotationAngle the double angle in which to rotate the provided
     * coordinates (specified in degrees).
     * @return a Polygon of the provided coordinates rotated around the center point
     * at the specified angle.
     * @throws IllegalArgumentException when the provided x points array is not the
     * same length as the provided y points array
     */
    @Suppress("unused")
    @Throws(IllegalArgumentException::class)
    fun buildPolygonPath(centerX: Double, centerY: Double, xp: Array<Double>, yp: Array<Double>, rotationAngle: Double): Path2D {
        // copy the arrays so that we don't manipulate the originals, that way we can
        // reuse them if necessary
        val xPoints = xp.copyOf(xp.size)
        val yPoints = yp.copyOf(yp.size)
        require(xPoints.size == yPoints.size) { "The provided x points are not the same length as the provided y points." }

        // create a list of Point2D pairs
        val list = ArrayList<Point2D>()
        for (i in yPoints.indices) {
            list.add(Point2D.Double(xPoints[i]!!, yPoints[i]!!))
        }

        // create an array which will hold the rotated points
        val rotatedPoints = arrayOfNulls<Point2D>(list.size)

        // rotate the points
        val transform = AffineTransform.getRotateInstance(Math.toRadians(rotationAngle), centerX, centerY)
        transform.transform(list.toTypedArray(), 0, rotatedPoints, 0, rotatedPoints.size)

        // build the polygon from the rotated points and return it
        val ixp = DoubleArray(list.size)
        //        double[] iyp = new double[list.size()];
        val path: Path2D = Path2D.Double()
        path.moveTo(rotatedPoints[0]!!.x, rotatedPoints[0]!!.y)
        for (i in 1 until ixp.size) {
//            ixp[i] = (double)rotatedPoints[i].getX();
//            iyp[i] = (double)rotatedPoints[i].getY();
            path.lineTo(rotatedPoints[i]!!.x, rotatedPoints[i]!!.y)
        }
        path.closePath()
        return path
    }
}