package com.ultreon.commons.lang;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Percentage utility object.
 *
 * @author Qboi
 * @since 1.0.0
 */
public record Percentage(double percentage) implements Serializable, Comparable<Percentage> {
    public static Percentage toPercentage(double value) {
        return new Percentage(value * 100);
    }

    public double value() {
        return percentage / 100;
    }

    @Override
    public int compareTo(@NotNull Percentage o) {
        return Double.compare(percentage, o.percentage);
    }
}
