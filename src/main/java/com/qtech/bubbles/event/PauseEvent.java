package com.qtech.bubbles.event;

import com.qtech.bubbles.QBubbles;

@Deprecated
public class PauseEvent extends Event {
    @Deprecated
    private final QBubbles main;
    @Deprecated
    private final boolean setToPaused;

    @Deprecated
    public PauseEvent(QBubbles main, boolean setToPaused) {
        this.main = main;
        this.setToPaused = setToPaused;
    }

    @Deprecated
    public QBubbles getMain() {
        return main;
    }

    @Deprecated
    public boolean isSetToPaused() {
        return setToPaused;
    }
}
