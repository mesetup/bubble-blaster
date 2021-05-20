package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.common.annotation.Cancelable;

public class Event {
    public Event() {

    }

    public boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }
}
