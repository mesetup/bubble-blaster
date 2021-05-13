package com.qsoftware.bubbles.event;

import com.qsoftware.bubbles.common.annotation.Cancelable;

public class Event {
    public Event() {

    }

    public boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }
}
