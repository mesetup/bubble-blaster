package com.qtech.bubbles.common.runnables;

@FunctionalInterface
public interface Applier<T, R> {
    R apply(T obj);
}
