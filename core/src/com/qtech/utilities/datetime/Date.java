package com.qtech.utilities.datetime;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.chrono.IsoEra;
import java.util.Objects;

@SuppressWarnings("unused")
public class Date implements Serializable {
    private int day;
    private int month;
    private int year;

    public static Date current() {
        LocalDateTime dateTime = LocalDateTime.now();
        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();

        return new Date(day, month, year);
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day &&
                month == date.month &&
                year == date.year;
    }

    public boolean equalsIgnoreYear(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day &&
                month == date.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
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

    public DayOfWeek getDayOfWeek() {
        LocalDate localDate = getLocalDate();
        return localDate.getDayOfWeek();
    }

    public int getDayOfYear() {
        LocalDate localDate = getLocalDate();
        return localDate.getDayOfYear();
    }

    public IsoEra getEra() {
        LocalDate localDate = getLocalDate();
        return localDate.getEra();
    }

    public IsoChronology getChronology() {
        LocalDate localDate = getLocalDate();
        return localDate.getChronology();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean isLeapYear() {
        LocalDate localDate = getLocalDate();
        localDate.toString();
        return localDate.getChronology().isLeapYear(getYear());
    }

    private LocalDate getLocalDate() {
        return LocalDate.of(year, month, day);
    }
}
