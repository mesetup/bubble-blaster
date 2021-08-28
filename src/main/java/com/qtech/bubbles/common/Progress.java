package com.qtech.bubbles.common;

import java.util.Objects;

/**
 * @author Quinten
 * @since 1.0.0
 */
public class Progress {
    private int progress;
    private final int max;

    public Progress(int max) {
        this(0, max);
    }

    public Progress(int progress, int max) {
        this.progress = progress;
        this.max = max;
    }

    public void increment() {
        if (progress + 1 <= max) {
            progress++;
        } else {
            throw new IllegalStateException("Progress increment at end: " + (progress + 1) + ", max: " + max);
        }
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "progress=" + progress +
                ", max=" + max +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Progress progress1 = (Progress) o;
        return getProgress() == progress1.getProgress() && getMax() == progress1.getMax();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProgress(), getMax());
    }
}
