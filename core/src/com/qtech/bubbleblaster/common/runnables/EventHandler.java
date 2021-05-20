package com.qtech.bubbleblaster.common.runnables;

@Deprecated
@FunctionalInterface
public interface EventHandler<T> {
    void run(T evt);
}
