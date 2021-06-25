package com.qtech.utilities.datetime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;

@SuppressWarnings("unused")
public class DateTime implements Comparable<DateTime>, Serializable {
    private int hour;
    private int minute;
    private int second;

    private int day;
    private int month;
    private int year;

    public static DateTime current() {
        LocalDateTime dateTime = LocalDateTime.now();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();

        return new DateTime(day, month, year, hour, minute, second);
    }

    public long toEpochSeconds() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute, second));
        return localDateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(0));
    }

    public DateTime(Date date, Time time) {
        this(date.getDay(), date.getMonth(), date.getYear(), time.getHour(), time.getMinute(), time.getSecond());
    }

    public DateTime(int day, int month, int year, int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /*************************************************************
     * Return flag meaning the object is between time1 and time2.
     *
     * @param lo low value.
     * @param hi high value.
     * @return true if the object is between time1 and time2.
     * @throws NullPointerException if ‘lo’ is higher than ‘hi’.
     */
    public boolean isBetween(DateTime lo, DateTime hi) {
        if (lo.toEpochSeconds() > hi.toEpochSeconds()) throw new NullPointerException("‘lo’ is higher than ‘hi’");

        return ((lo.toEpochSeconds() <= toEpochSeconds()) && (hi.toEpochSeconds() >= toEpochSeconds()));

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

    /**
     * Get hour.
     *
     * @return the hour.
     */
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return getHour() == dateTime.getHour() &&
                getMinute() == dateTime.getMinute() &&
                getSecond() == dateTime.getSecond() &&
                getDay() == dateTime.getDay() &&
                getMonth() == dateTime.getMonth() &&
                getYear() == dateTime.getYear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHour(), getMinute(), getSecond(), getDay(), getMonth(), getYear());
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Time getTime() {
        return new Time(hour, minute, second);
    }

    public Date getDate() {
        return new Date(day, month, year);
    }

    @Override
    public int compareTo(DateTime o) {
        return Long.compare(toEpochSeconds(), o.toEpochSeconds());
    }
}
