package com.ultreon.hydro.event.window;

import com.ultreon.hydro.GameWindow;
import com.ultreon.hydro.vector.Vector2f;
import com.ultreon.hydro.vector.Vector2i;

public class WindowResizeEvent extends WindowEvent {
    private final Vector2i oldSize;
    private final Vector2f newSize;

    public WindowResizeEvent(GameWindow window, Vector2i oldSize, Vector2f newSize) {
        super(window);
        this.oldSize = oldSize;
        this.newSize = newSize;
    }

    public Vector2i getOldSize() {
        return oldSize;
    }

    public Vector2f getNewSize() {
        return newSize;
    }
}
