package com.ultreon.hydro.event;

import com.ultreon.hydro.annotation.Cancelable;

public class Event {
    private boolean cancelled;

    public final void cancel() {
        this.cancelled = true;
    }

    public final boolean isCancelled() {
        return isCancelable() && cancelled;
    }

    public final boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }
}
