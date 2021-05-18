package com.qtech.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qtech.bubbles.QBubbles;

public class PauseEvent extends Event {
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
