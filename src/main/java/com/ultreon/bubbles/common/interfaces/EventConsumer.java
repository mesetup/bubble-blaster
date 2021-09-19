package com.ultreon.bubbles.common.interfaces;

import com.ultreon.hydro.event.SubscribeEvent;

@FunctionalInterface
public interface EventConsumer<T> {
    @SubscribeEvent
    void accept(T evt);
}
