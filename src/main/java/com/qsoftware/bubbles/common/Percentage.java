package com.qsoftware.bubbles.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Percentage implements Serializable, Comparable<Percentage> {
    private final double percentage;

    public Percentage(double percentage) {
        this.percentage = percentage * 100;
    }

    public Percentage(float value) {
        this.percentage = value;
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
