package com.qtech.bubbles.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record Percentage(double percentage) implements Serializable, Comparable<Percentage> {
    public static Percentage toPercentage(double value) {
        return new Percentage(value * 100);
    }

    public double getPercentage() {
        return percentage;
    }

    public double getValue() {
        return percentage / 100;
    }

    @Override
    public int compareTo(@NotNull Percentage o) {
        return Double.compare(percentage, o.percentage);
    }
}
