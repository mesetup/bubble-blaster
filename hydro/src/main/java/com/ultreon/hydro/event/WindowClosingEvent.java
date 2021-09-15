package com.ultreon.hydro.event;

import com.ultreon.hydro.GameWindow;
import com.ultreon.hydro.event._common.ICancellable;

public class WindowClosingEvent extends WindowEvent implements ICancellable {
    public WindowClosingEvent(GameWindow window) {
        super(window);
    }
}
