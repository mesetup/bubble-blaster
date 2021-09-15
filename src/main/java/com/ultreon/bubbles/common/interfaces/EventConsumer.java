package com.ultreon.bubbles.common.interfaces;

import com.ultreon.hydro.event._common.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
