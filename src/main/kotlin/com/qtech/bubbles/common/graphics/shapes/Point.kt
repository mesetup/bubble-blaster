package com.qtech.bubbles.common.graphics.shapes

open class Point(var pointX: Double, var pointY: Double) {
    override fun toString(): String {
        return "Point{" +
                "X=" + pointX +
                ", Y=" + pointY +
                '}'
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        var temp: Long = java.lang.Double.doubleToLongBits(pointX)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(pointY)
        result = prime * result + (temp xor (temp ushr 32)).toInt()
        return result
    }

    /** (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (other !is Point) {
            return false
        }
        return if (java.lang.Double.doubleToLongBits(pointX) != java.lang.Double.doubleToLongBits(other.pointX)) {
            false
        } else java.lang.Double.doubleToLongBits(pointY) == java.lang.Double.doubleToLongBits(other.pointY)
    }
}