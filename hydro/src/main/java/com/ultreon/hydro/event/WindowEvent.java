package com.ultreon.hydro.event;

import com.ultreon.hydro.GameWindow;

public class WindowEvent extends Event {
    private final GameWindow window;

    public WindowEvent(GameWindow window) {
        this.window = window;
    }

    public GameWindow getWindow() {
        return window;
    }
}
