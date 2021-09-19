package com.ultreon.commons.time;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Time implements Comparable<Time>, Serializable {
    private int hour;
    private int minute;
    private int second;

    public static Time current() {
        LocalDateTime dateTime = LocalDateTime.now();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        return new Time(hour, minute, second);
    }

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /*****************************
     * Returns the total seconds,
     *
     * @return total seconds.
     */
    public int toSeconds() {
        int sec = second;
        sec += minute * 60;
        sec += hour * 3600;

        return sec;
    }

    /*****************************
     * Returns the total minutes,
     *
     * @return total minutes.
     */
    public float toMinutes() {
        float min = (float) second / 60;
        min += (float) minute;
        min += (float) hour * 60;

        return min;
    }

    /*****************************
     * Returns the total hours,
     *
     * @return total hours.
     */
    public float toHours() {
        float hor = (float) second / 3600;
        hor += (float) minute / 60;
        hor += (float) hour;

        return hor;
    }

    /*************************************************************
     * Return flag meaning the object is between time1 and time2.
     *
     * @param lo low value.
     * @param hi high value.
     * @return true if the object is between time1 and time2.
     * @throws NullPointerException if ‘lo’ is higher than ‘hi’.
     */
    public boolean isBetween(Time lo, Time hi) {
        if (lo.toSeconds() > hi.toSeconds()) throw new NullPointerException();

        return ((lo.toSeconds() <= toSeconds()) && (hi.toSeconds() >= toSeconds()));

//        boolean flag1 = ((lo.hour <= hour) && (hi.hour >= hour));
//        boolean flag2 = ((lo.minute <= minute) && (hi.minute >= minute));
//        boolean flag3 = ((lo.second <= second) && (hi.second >= second));
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
    public int compareTo(Time o) {
        return Integer.compare(toSeconds(), o.toSeconds());
    }
}
