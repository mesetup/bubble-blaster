package com.qtech.bubbles.common.runnables;

@Deprecated
@FunctionalInterface
public interface EventHandler<T> {
    void run(T evt);
}