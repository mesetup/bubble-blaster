package com.qtech.bubbles.common.interfaces;

import com.qtech.bubbles.event.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
