package com.ultreon.hydro.event.screen;

import com.ultreon.hydro.screen.Screen;

public class OpenScreenEvent extends ScreenEvent {
    public OpenScreenEvent(Screen screen) {
        super(screen);
    }
}
