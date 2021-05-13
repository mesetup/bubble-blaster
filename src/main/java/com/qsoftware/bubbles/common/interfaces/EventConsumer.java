package com.qsoftware.bubbles.common.interfaces;

import com.qsoftware.bubbles.event.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
