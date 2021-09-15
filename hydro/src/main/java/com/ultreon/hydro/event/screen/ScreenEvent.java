package com.ultreon.hydro.event.screen;

import com.ultreon.hydro.screen.Screen;

public abstract class ScreenEvent {
    private final Screen screen;

    public ScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
