package com.qtech.bubbles.event.old;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.scene.Scene;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.event.type.KeyEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

/**
 * <h1>Keyboard Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see QMouseEvent
 */
@Deprecated
public class QKeyboardEvent extends QEvent<QKeyboardEvent> {
    private static final QKeyboardEvent instance = new QKeyboardEvent();
    private final QBubbles main;
    private final KeyboardController controller;
    private final KeyEvent parentEvent;
    private final KeyEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link QBubbles} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public QKeyboardEvent(QBubbles main, Scene scene, @NotNull KeyboardController controller, KeyEvent event, KeyEventType type) {
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
    public QBubbles getMain() {
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
