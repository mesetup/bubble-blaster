package com.qtech.bubbles.common.interfaces;

import com.qtech.bubbles.event._common.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
