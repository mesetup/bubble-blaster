package com.ultreon.hydro.event.screen;

import com.ultreon.hydro.screen.Screen;

public class InitScreenEvent extends ScreenEvent {
    public InitScreenEvent(Screen screen) {
        super(screen);
    }
}
