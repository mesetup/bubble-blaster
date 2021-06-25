package com.qtech.bubbles.event;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.core.controllers.MouseController;
import com.qtech.bubbles.event.type.KeyEventType;
import com.qtech.bubbles.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * <h1>Mouse Motion Event</h1>
 * This event is used for handling mouse motion input.
 *
 * @see com.qtech.bubbles.event.MouseEvent
 * @see MouseMotionEvent
 * @see MouseController
 * @see MouseEventType
 * @see java.awt.event.MouseEvent
 */
public class MouseMotionEvent extends Event {
    private final QBubbles main;
    private final @NotNull MouseController controller;
    private final java.awt.event.MouseEvent parentEvent;
    private final MouseEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link QBubbles} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseMotionEvent(QBubbles main, @NotNull MouseController controller, MouseEvent event, MouseEventType type) {
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
    public QBubbles getMain() {
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
