package com.ultreon.hydro.event;

public abstract class Subscriber<T extends AbstractEvent> {
    public abstract void handle(T e);

    public abstract EventPriority getPriority();

    public abstract SubscribeEvent getAnnotation();

    public abstract Class<? extends Event> getType();
}
