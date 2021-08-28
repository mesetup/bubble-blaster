package com.qtech.bubbles.event;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.event.screen.OpenScreenEvent;
import com.qtech.bubbles.screen.PauseScreen;

/**
 * @author Quinten J.
 * @deprecated replaced by {@linkplain OpenScreenEvent} with as screen the {@linkplain PauseScreen}.
 */
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
