package com.qtech.bubbles.common;

public class TimeProcessor {
    public static double getTime() {
        return (double) System.nanoTime() / 1000000000d;
    }
}
