package qtech.utilities.datetime

import qtech.utilities.core.exceptions.InvalidOrderException
import java.io.Serializable

class TimeSpan(from: DateTime, to: DateTime) : Serializable {
    private val from: DateTime
    private val to: DateTime
    operator fun contains(dateTime: DateTime): Boolean {
        return dateTime.isBetween(from, to)
    }

    fun toDuration(): Duration {
        return Duration((to.toEpochSeconds() - from.toEpochSeconds()).toDouble())
    }

    init {
        if (from > to) throw InvalidOrderException("Parameter ‘from’ is later than ‘to’.")
        this.from = from
        this.to = to
    }
}