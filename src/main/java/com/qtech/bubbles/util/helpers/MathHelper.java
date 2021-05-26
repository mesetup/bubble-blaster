package com.qtech.bubbles.util.helpers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Math helper, for all your math needs.
 */
public class MathHelper {
    // Clamp methods.
    @Deprecated
    public static byte clamp(byte value, byte min, byte max) {
        return (byte) Math.min(Math.max(value, min), max);
    }

    public static byte clamp(byte value, int min, int max) {
        return (byte) Math.min(Math.max(value, min), max);
    }

    @Deprecated
    public static short clamp(short value, short min, short max) {
        return (short) Math.min(Math.max(value, min), max);
    }

    public static short clamp(short value, int min, int max) {
        return (short) Math.min(Math.max(value, min), max);
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static BigInteger clamp(BigInteger value, BigInteger min, BigInteger max) {
        return value.max(min).min(max);
    }

    public static BigDecimal clamp(BigDecimal value, BigDecimal min, BigDecimal max) {
        return value.max(min).min(max);
    }

    public static double root(int value, int root) {
        return Math.pow(value, 1.0d / root);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
