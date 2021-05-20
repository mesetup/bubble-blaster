package com.qtech.bubbleblaster.common.runnables;

@FunctionalInterface
public interface Applier<T, R> {
    R apply(T obj);
}
