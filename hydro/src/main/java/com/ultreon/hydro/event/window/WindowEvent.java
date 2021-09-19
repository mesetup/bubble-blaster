package com.ultreon.hydro.event.window;

import com.ultreon.hydro.GameWindow;
import com.ultreon.hydro.event.Event;

public class WindowEvent extends Event {
    private final GameWindow window;

    public WindowEvent(GameWindow window) {
        this.window = window;
    }

    public GameWindow getWindow() {
        return window;
    }
}
