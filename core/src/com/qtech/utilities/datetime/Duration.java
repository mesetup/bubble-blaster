package com.qtech.utilities.datetime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@SuppressWarnings("unused")
public class Duration implements Comparable<Duration>, Serializable {
    private final double duration;

    public Duration(double duration) {
        this.duration = duration;
    }

    public void sleep() throws InterruptedException {
        Thread.sleep((long) duration * 1000);
    }

    public double getSeconds() {
        return duration;
    }

    public double getMinutes() {
        return duration / 60;
    }

    public double getHours() {
        return duration / 3600;
    }

    public double getDays() {
        return duration / 86400;
    }

    public double getWeeks() {
        return duration / 604800;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "duration=" + duration +
                '}';
    }

    public String toSimpleString() {
        LocalDateTime g = LocalDateTime.ofEpochSecond((long) (duration), 0, ZoneOffset.ofTotalSeconds(0));
        int minute = g.getMinute();
        int second = g.getSecond();

        double hourDouble = duration / 60 / 60;
        hourDouble -= (double) minute / 60;
        hourDouble -= (double) second / 60 / 60;

        int hour = (int) hourDouble;

        String minuteString = Integer.toString(minute);
        String secondString = Integer.toString(second);

        if (minuteString.length() == 1) minuteString = "0" + minuteString;
        if (secondString.length() == 1) secondString = "0" + secondString;

        return hour + ":" + minuteString + ":" + secondString;
    }

    public int toInt() {
        return (int) duration;
    }

    public long toLong() {
        return (long) duration;
    }

    public double toDouble() {
        return duration;
    }

    public float toFloat() {
        return (float) duration;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(duration);
    }

    public BigInteger toBigInteger() {
        return BigInteger.valueOf((long) duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration1 = (Duration) o;
        return Double.compare(duration1.duration, duration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration);
    }

    @Override
    public int compareTo(Duration o) {
        return Double.compare((toDouble()), o.toDouble());
    }
}
