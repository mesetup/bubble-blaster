@file:Suppress("unused")

package com.qtech.utilities.datetime

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

class DateTime(var day: Int, var month: Int, var year: Int, var hour: Int, var minute: Int, var second: Int) : Comparable<DateTime>, Serializable {
    fun toEpochSeconds(): Long {
        val localDateTime = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute, second))
        return localDateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0))
    }

    constructor(date: Date, time: Time) : this(date.day, date.month, date.year, time.hour, time.minute, time.second)

    /*************************************************************
     * Return flag meaning the object is between time1 and time2.
     *
     * @param lo low value.
     * @param hi high value.
     * @return true if the object is between time1 and time2.
     * @throws NullPointerException if ‘lo’ is higher than ‘hi’.
     */
    fun isBetween(lo: DateTime, hi: DateTime): Boolean {
        if (lo.toEpochSeconds() > hi.toEpochSeconds()) throw NullPointerException("‘lo’ is higher than ‘hi’")
        return lo.toEpochSeconds() <= toEpochSeconds() && hi.toEpochSeconds() >= toEpochSeconds()

//        boolean flag1 = ((lo.hour <= hour) && (hi.hour >= hour));
//        boolean flag2 = ((lo.minute <= minute) && (hi.minute >= minute));
//        boolean flag3 = ((lo.second <= second) && (hi.second >= second));
//
//        boolean flag4 = ((lo.day <= day) && (hi.day >= day));
//        boolean flag5 = ((lo.month <= month) && (hi.month >= month));
//        boolean flag6 = ((lo.year <= year) && (hi.year >= year));
//
//        if (flag1 && flag2 && flag3 && flag4 && flag5 && flag6) {
//            return true;
//        }
//
//        if (flag1 && flag2 && flag3 && flag4 && flag5) {
//            return true;
//        }
//
//        if (flag1 && flag2 && flag3 && flag4) {
//            return true;
//        }
//
//        if (flag1 && flag2 && flag3) {
//            return true;
//        }
//
//        if (flag1 && flag2) {
//            return true;
//        }
//
//        if (flag1) {
//            return true;
//        }
//
//        return false;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val dateTime = other as DateTime
        return hour == dateTime.hour && minute == dateTime.minute && second == dateTime.second && day == dateTime.day && month == dateTime.month && year == dateTime.year
    }

    override fun hashCode(): Int {
        return Objects.hash(hour, minute, second, day, month, year)
    }

    val time: Time
        get() {
            return Time(hour, minute, second)
        }

    val date: Date
        get() {
            return Date(day, month, year)
        }

    override fun compareTo(other: DateTime): Int {
        return toEpochSeconds().compareTo(other.toEpochSeconds())
    }

    companion object {
        fun current(): DateTime {
            val dateTime = LocalDateTime.now()
            val hour = dateTime.hour
            val minute = dateTime.minute
            val second = dateTime.second
            val day = dateTime.dayOfMonth
            val month = dateTime.monthValue
            val year = dateTime.year
            return DateTime(day, month, year, hour, minute, second)
        }
    }
}