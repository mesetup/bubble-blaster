package com.qtech.bubbles.event.screen;

import com.qtech.bubbles.screen.Screen;

public abstract class ScreenEvent {
    private final Screen screen;

    public ScreenEvent(Screen screen) {
        this.screen = screen;
    }
}
