package com.ultreon.hydro.event;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.screen.OpenScreenEvent;

/**
 * @author Qboi
 * @deprecated replaced by {@linkplain OpenScreenEvent} with as screen the {@linkplain PauseScreen}.
 */
@Deprecated
public class PauseEvent extends Event {
    @Deprecated
    private final Game main;
    @Deprecated
    private final boolean setToPaused;

    @Deprecated
    public PauseEvent(Game main, boolean setToPaused) {
        this.main = main;
        this.setToPaused = setToPaused;
    }

    @Deprecated
    public Game getMain() {
        return main;
    }

    @Deprecated
    public boolean isSetToPaused() {
        return setToPaused;
    }
}
