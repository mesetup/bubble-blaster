package com.ultreon.hydro.event.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.core.input.KeyboardController;
import com.ultreon.hydro.core.input.MouseController;
import com.ultreon.hydro.event.Event;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * <h1>Mouse Motion Event</h1>
 * This event is used for handling mouse motion input.
 *
 * @see com.ultreon.hydro.event.input.MouseEvent
 * @see MouseMotionEvent
 * @see MouseController
 * @see MouseEventType
 * @see java.awt.event.MouseEvent
 */
public class MouseMotionEvent extends Event {
    private final Game main;
    private final @NotNull MouseController controller;
    private final java.awt.event.MouseEvent parentEvent;
    private final MouseEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link Game} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseMotionEvent(Game main, @NotNull MouseController controller, MouseEvent event, MouseEventType type) {
        this.main = main;
        this.type = type;
        this.controller = controller;
        this.parentEvent = event;
    }

    /**
     * Returns the Main instance used in the event.
     *
     * @return The Main instance.
     */
    public Game getMain() {
        return main;
    }

    /**
     * Returns the KeyboardController instance used in the event.
     *
     * @return The KeyboardController instance.
     */
    public @NotNull MouseController getController() {
        return controller;
    }

    public java.awt.event.MouseEvent getParentEvent() {
        return parentEvent;
    }

    public MouseEventType getType() {
        return type;
    }
}
