package qtech.utilities.datetime

import java.io.Serializable
import java.time.LocalDateTime

class Time(var hour: Int, var minute: Int, var second: Int) : Comparable<Time>, Serializable {
    /*****************************
     * Returns the total seconds,
     *
     * @return total seconds.
     */
    fun toSeconds(): Int {
        var sec = second
        sec += minute * 60
        sec += hour * 3600
        return sec
    }

    /*****************************
     * Returns the total minutes,
     *
     * @return total minutes.
     */
    fun toMinutes(): Float {
        var min = second.toFloat() / 60
        min += minute.toFloat()
        min += hour.toFloat() * 60
        return min
    }

    /*****************************
     * Returns the total hours,
     *
     * @return total hours.
     */
    fun toHours(): Float {
        var hor = second.toFloat() / 3600
        hor += minute.toFloat() / 60
        hor += hour.toFloat()
        return hor
    }

    /*************************************************************
     * Return flag meaning the object is between time1 and time2.
     *
     * @param lo low value.
     * @param hi high value.
     * @return true if the object is between time1 and time2.
     * @throws NullPointerException if ‘lo’ is higher than ‘hi’.
     */
    fun isBetween(lo: Time, hi: Time): Boolean {
        if (lo.toSeconds() > hi.toSeconds()) throw NullPointerException()
        return lo.toSeconds() <= toSeconds() && hi.toSeconds() >= toSeconds()
    }

    override fun compareTo(other: Time): Int {
        return toSeconds().compareTo(other.toSeconds())
    }

    companion object {
        fun current(): Time {
            val dateTime = LocalDateTime.now()
            val hour = dateTime.hour
            val minute = dateTime.minute
            val second = dateTime.second
            return Time(hour, minute, second)
        }
    }
}