package com.qtech.utilities.datetime

import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.IsoChronology
import java.time.chrono.IsoEra
import java.util.*

class Date(var day: Int, var month: Int, var year: Int) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val date = other as Date
        return day == date.day && month == date.month && year == date.year
    }

    fun equalsIgnoreYear(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val date = o as Date
        return day == date.day &&
                month == date.month
    }

    override fun hashCode(): Int {
        return Objects.hash(day, month, year)
    }

    val dayOfWeek: DayOfWeek
        get() {
            val localDate = localDate
            return localDate.dayOfWeek
        }

    val dayOfYear: Int
        get() {
            val localDate = localDate
            return localDate.dayOfYear
        }

    val era: IsoEra
        get() {
            val localDate = localDate
            return localDate.era
        }

    val chronology: IsoChronology
        get() {
            val localDate = localDate
            return localDate.chronology
        }

    val isLeapYear: Boolean
        get() {
            val localDate = localDate
            localDate.toString()
            return localDate.chronology.isLeapYear(year.toLong())
        }

    private val localDate: LocalDate
        get() {
            return LocalDate.of(year, month, day)
        }

    companion object {
        fun current(): Date {
            val dateTime = LocalDateTime.now()
            val day = dateTime.dayOfMonth
            val month = dateTime.monthValue
            val year = dateTime.year
            return Date(day, month, year)
        }
    }
}