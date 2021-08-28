package com.qtech.bubbles.event._common;

import com.qtech.bubbles.event.Event;

public abstract class EventHandler<T extends Event> {
    public abstract void handle(T e);

    public abstract EventPriority getPriority();

    public abstract SubscribeEvent getAnnotation();

    public abstract Class<? extends Event> getType();
}
