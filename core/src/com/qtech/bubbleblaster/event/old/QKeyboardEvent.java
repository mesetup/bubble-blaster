package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.scene.Scene;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.event.type.KeyEventType;
import org.jetbrains.annotations.NotNull;


/**
 * <h1>Keyboard Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see QMouseEvent
 */
@Deprecated
public class QKeyboardEvent extends QEvent<QKeyboardEvent> {
    private static final QKeyboardEvent instance = new QKeyboardEvent();
    private final BubbleBlaster main;
    private final KeyboardController controller;
    private final KeyEvent parentEvent;
    private final KeyEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link BubbleBlaster} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public QKeyboardEvent(BubbleBlaster main, Scene scene, @NotNull KeyboardController controller, KeyEvent event, KeyEventType type) {
        super();
        this.main = main;
        this.type = type;
        this.controller = controller;
        this.parentEvent = event;

//        getInstance().call(this, scene);
    }

    protected QKeyboardEvent() {
        super();
        this.main = null;
        this.type = null;
        this.controller = null;
        this.parentEvent = null;
    }

    public static QKeyboardEvent getInstance() {
        return instance;
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
    public KeyboardController getController() {
        return controller;
    }

    public KeyEvent getParentEvent() {
        return parentEvent;
    }

    public KeyEventType getType() {
        return type;
    }
}
