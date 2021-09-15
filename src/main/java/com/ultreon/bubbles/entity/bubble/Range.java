package com.ultreon.bubbles.entity.bubble;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Range {
    private final double start;
    private final double end;
    private final double step;

    public Range(double end) {
        this(0, end);
    }

    public Range(double start, double end) {
        this(start, end, 1);
    }

    public Range(double start, double end, double step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public boolean contains(double value) {
        return (start <= value) && (end > value);
    }

    @SuppressWarnings("Convert2Diamond")
    public @NotNull Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private double current = start;

            @Override
            public boolean hasNext() {
                return current < end;
            }

            @Override
            public Double next() {
                return current += step;
            }
        };
    }

    public Iterable<Double> iterable() {
        return Range.this::iterator;
    }

    public double getStep() {
        return step;
    }
}
