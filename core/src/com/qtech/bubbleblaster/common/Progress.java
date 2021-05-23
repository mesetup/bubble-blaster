package com.qtech.bubbleblaster.common;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Progress {
    private int progress;
    private final int max;

    public Progress(int max) {
        this(0, max);
    }

    public void increment() {
        if (progress + 1 <= max) {
            progress++;
        } else {
            throw new IllegalStateException("Progress increment at end: " + (progress + 1) + ", max: " + max);
        }
    }
}
