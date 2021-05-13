package com.qsoftware.utilities.datetime;

import com.qsoftware.utilities.core.exceptions.InvalidOrderException;

import java.io.Serializable;

public class TimeSpan implements Serializable {
    private final DateTime from;
    private final DateTime to;

    public TimeSpan(DateTime from, DateTime to) {
        if (from.compareTo(to) > 0) throw new InvalidOrderException("Parameter ‘from’ is later than ‘to’.");

        this.from = from;
        this.to = to;
    }

    public boolean contains(DateTime dateTime) {
        return dateTime.isBetween(from, to);
    }

    public Duration toDuration() {
        return new Duration(to.toEpochSeconds() - from.toEpochSeconds());
    }
}
