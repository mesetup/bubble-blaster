package com.ultreon.hydro.event.window;

import com.ultreon.commons.lang.ICancellable;
import com.ultreon.hydro.GameWindow;

public class WindowClosingEvent extends WindowEvent implements ICancellable {
    public WindowClosingEvent(GameWindow window) {
        super(window);
    }
}
