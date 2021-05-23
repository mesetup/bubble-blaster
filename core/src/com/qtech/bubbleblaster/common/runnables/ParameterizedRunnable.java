package com.qtech.bubbleblaster.common.runnables;

@FunctionalInterface
public interface ParameterizedRunnable<T> {
    void run(T t);
}
