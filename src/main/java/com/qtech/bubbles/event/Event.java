package com.qtech.bubbles.event;

import com.qtech.bubbles.common.annotation.Cancelable;

public class Event {
    public Event() {

    }

    public boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }
}
