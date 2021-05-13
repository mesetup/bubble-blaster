package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.controllers.KeyboardController;
import com.qsoftware.bubbles.core.controllers.MouseController;
import com.qsoftware.bubbles.event.old.QMouseEvent;
import com.qsoftware.bubbles.event.type.KeyEventType;
import com.qsoftware.bubbles.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

/**
 * <h1>Keyboard Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see QMouseEvent
 */
public class MouseWheelEvent extends Event {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        return eventBus;
    }

    private final QBubbles main;
    private final @NotNull MouseController controller;
    private final java.awt.event.MouseEvent parentEvent;
    private final MouseEventType type;
    private final Scene scene;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link QBubbles} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseWheelEvent(QBubbles main, Scene scene, @NotNull MouseController controller, java.awt.event.MouseEvent event, MouseEventType type) {
        this.main = main;
        this.scene = scene;
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
