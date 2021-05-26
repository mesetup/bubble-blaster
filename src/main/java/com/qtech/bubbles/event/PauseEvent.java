package com.qtech.bubbles.event;

import com.qtech.bubbles.BubbleBlaster;

@Deprecated
public class PauseEvent extends Event {
    @Deprecated
    private final BubbleBlaster main;
    @Deprecated
    private final boolean setToPaused;

    @Deprecated
    public PauseEvent(BubbleBlaster main, boolean setToPaused) {
        this.main = main;
        this.setToPaused = setToPaused;
    }

    @Deprecated
    public BubbleBlaster getMain() {
        return main;
    }

    @Deprecated
    public boolean isSetToPaused() {
        return setToPaused;
    }
}
