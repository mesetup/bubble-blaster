package com.qtech.bubbleblaster.common.runnables;

@FunctionalInterface
public interface DoubleParameterizedRunnable<A, B> {
    void run(A a, B b);
}
