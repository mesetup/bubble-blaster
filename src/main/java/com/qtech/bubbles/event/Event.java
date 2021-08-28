package com.qtech.bubbles.event;

import com.qtech.bubbles.common.annotation.Cancelable;

public class Event {
    private boolean cancelled;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public final boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }
}
