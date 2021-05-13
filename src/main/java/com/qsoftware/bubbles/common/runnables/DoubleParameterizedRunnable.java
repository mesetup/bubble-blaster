package com.qsoftware.bubbles.common.runnables;

@FunctionalInterface
public interface DoubleParameterizedRunnable<A, B> {
    void run(A a, B b);
}
