package com.qtech.bubbleblaster.event;

public abstract class EventHandler<T extends Event> {
    public abstract void handle(T e);

    public abstract EventPriority getPriority();

    public abstract SubscribeEvent getAnnotation();

    public abstract Class<? extends Event> getType();
}
