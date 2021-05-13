package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.QBubbles;

public class PauseEvent extends Event {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        return eventBus;
    }

    private final QBubbles main;
    private final boolean setToPaused;

    public PauseEvent(QBubbles main, boolean setToPaused) {
        this.main = main;
        this.setToPaused = setToPaused;
    }

    public QBubbles getMain() {
        return main;
    }

    public boolean isSetToPaused() {
        return setToPaused;
    }
}
