@file:Suppress("unused")

package com.qtech.utilities.datetime

import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class Duration(var duration: Double) : Comparable<Duration>, Serializable {
    @Throws(InterruptedException::class)
    fun sleep() {
        Thread.sleep(duration.toLong() * 1000)
    }

    fun getSeconds(): Double {
        return duration
    }

    fun getMinutes(): Double {
        return duration / 60
    }

    fun getHours(): Double {
        return duration / 3600
    }

    fun getDays(): Double {
        return duration / 86400
    }

    fun getWeeks(): Double {
        return duration / 604800
    }

    override fun toString(): String {
        return "Duration{" +
                "duration=" + duration +
                '}'
    }

    fun toSimpleString(): String {
        val g = LocalDateTime.ofEpochSecond(duration.toLong(), 0, ZoneOffset.ofTotalSeconds(0))
        val minute = g.minute
        val second = g.second
        var hourDouble = duration / 60 / 60
        hourDouble -= minute.toDouble() / 60
        hourDouble -= second.toDouble() / 60 / 60
        val hour = hourDouble.toInt()
        var minuteString = minute.toString()
        var secondString = second.toString()
        if (minuteString.length == 1) minuteString = "0$minuteString"
        if (secondString.length == 1) secondString = "0$secondString"
        return "$hour:$minuteString:$secondString"
    }

    fun toInt(): Int {
        return duration.toInt()
    }

    fun toLong(): Long {
        return duration.toLong()
    }

    fun toDouble(): Double {
        return duration
    }

    fun toFloat(): Float {
        return duration.toFloat()
    }

    fun toBigDecimal(): BigDecimal {
        return BigDecimal.valueOf(duration)
    }

    fun toBigInteger(): BigInteger {
        return BigInteger.valueOf(duration.toLong())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val duration1 = other as Duration
        return duration1.duration.compareTo(duration) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(duration)
    }

    override fun compareTo(other: Duration): Int {
        return toDouble().compareTo(other.toDouble())
    }
}