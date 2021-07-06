package qtech.bubbles.util.helpers

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

/**
 * Math helper, for all your math needs.
 */
object MathHelper {
    // Clamp methods.
    fun clamp(value: Byte, min: Byte, max: Byte): Byte {
        return value.toInt().coerceAtLeast(min.toInt()).coerceAtMost(max.toInt()).toByte()
    }

    fun clamp(value: Byte, min: Int, max: Int): Byte {
        return value.toInt().coerceAtLeast(min).coerceAtMost(max).toByte()
    }

    fun clamp(value: Short, min: Short, max: Short): Short {
        return value.toInt().coerceAtLeast(min.toInt()).coerceAtMost(max.toInt()).toShort()
    }

    fun clamp(value: Short, min: Int, max: Int): Short {
        return value.toInt().coerceAtLeast(min).coerceAtMost(max).toShort()
    }

    fun clamp(value: Int, min: Int, max: Int): Int {
        return value.coerceAtLeast(min).coerceAtMost(max)
    }

    fun clamp(value: Long, min: Long, max: Long): Long {
        return value.coerceAtLeast(min).coerceAtMost(max)
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return value.coerceAtLeast(min).coerceAtMost(max)
    }

    fun clamp(value: Double, min: Double, max: Double): Double {
        return value.coerceAtLeast(min).coerceAtMost(max)
    }

    fun clamp(value: BigInteger, min: BigInteger?, max: BigInteger?): BigInteger {
        return value.max(min).min(max)
    }

    fun clamp(value: BigDecimal, min: BigDecimal?, max: BigDecimal?): BigDecimal {
        return value.max(min).min(max)
    }

    @Suppress("unused")
    fun root(value: Int, root: Int): Double {
        return value.toDouble().pow(1.0 / root)
    }

    fun round(value: Double, places: Int): Double {
        require(places >= 0)
        var bd = BigDecimal(value.toString())
        bd = bd.setScale(places, RoundingMode.HALF_UP)
        return bd.toDouble()
    }
}