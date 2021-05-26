package com.qtech.bubbles.common.runnables;

@FunctionalInterface
public interface ParameterizedRunnable<T> {
    void run(T t);
}
