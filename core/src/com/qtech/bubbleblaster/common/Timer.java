package com.qtech.bubbleblaster.common;

public class Timer {
    public static double getTime() {
        return (double) System.nanoTime() / 1000000000d;
    }
}
