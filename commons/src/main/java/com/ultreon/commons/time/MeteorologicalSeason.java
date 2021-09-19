package com.ultreon.commons.time;

public enum MeteorologicalSeason {
    WINTER(),
    SPRING(),
    SUMMER(),
    AUTUMN();

    MeteorologicalSeason() {

    }

    public Date getStartDate(int year) {
        return switch (this) {
            case WINTER -> new Date(1, 12, year);
            case SPRING -> new Date(1, 3, year);
            case SUMMER -> new Date(1, 6, year);
            case AUTUMN -> new Date(1, 9, year);
        };
    }

    public Date getEndDate(int year) {
        return switch (this) {
            case WINTER -> new Date(DateTime.isLeapYear(year) ? 29 : 28, 2, year);
            case SPRING -> new Date(31, 5, year);
            case SUMMER -> new Date(31, 8, year);
            case AUTUMN -> new Date(30, 11, year);
        };
    }
}
