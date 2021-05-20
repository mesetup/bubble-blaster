package com.qtech.bubbleblaster.common.interfaces;

import com.qtech.bubbleblaster.event.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
