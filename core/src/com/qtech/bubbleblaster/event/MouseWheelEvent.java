package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.core.controllers.MouseController;
import com.qtech.bubbleblaster.event.type.KeyEventType;
import com.qtech.bubbleblaster.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;


/**
 * <h1>Mouse Wheel Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see MouseMotionEvent
 * @see com.qtech.bubbleblaster.event.MouseEvent
 * @see MouseController
 * @see MouseEventType
 * @see java.awt.event.MouseWheelEvent
 */
public class MouseWheelEvent extends Event {
    private final BubbleBlaster main;
    private final @NotNull MouseController controller;
    private final MouseEvent parentEvent;
    private final MouseEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link BubbleBlaster} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseWheelEvent(BubbleBlaster main, @NotNull MouseController controller, MouseEvent event, MouseEventType type) {
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
    public BubbleBlaster getMain() {
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

    public MouseEvent getParentEvent() {
        return parentEvent;
    }

    public MouseEventType getType() {
        return type;
    }
}
