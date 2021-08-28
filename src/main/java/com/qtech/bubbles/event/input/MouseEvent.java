package com.qtech.bubbles.event.input;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.core.controllers.MouseController;
import com.qtech.bubbles.event.Event;
import com.qtech.bubbles.event.type.KeyEventType;
import com.qtech.bubbles.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * <h1>Mouse Event</h1>
 * This event is used for handling mouse input.
 * @see MouseMotionEvent
 * @see MouseWheelEvent
 * @see MouseController
 * @see MouseEventType
 * @see java.awt.event.MouseEvent
 */
public class MouseEvent extends Event {
    private final int button;
    private final Point locationOnScreen;
    private final int clickCount;
    private final Point point;
    private final int x;
    private final int xOnScreen;
    private final int y;
    private final int yOnScreen;

    private final BubbleBlaster main;
    private final @NotNull MouseController controller;
    private final java.awt.event.MouseEvent parentEvent;
    private final MouseEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link BubbleBlaster} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseEvent(BubbleBlaster main, @NotNull MouseController controller, java.awt.event.MouseEvent event, MouseEventType type) {
        this.main = main;
        this.type = type;
        this.button = event.getButton();
        this.clickCount = event.getClickCount();
        this.locationOnScreen = event.getLocationOnScreen();
        this.point = event.getPoint();
        this.x = event.getX();
        this.xOnScreen = event.getXOnScreen();
        this.y = event.getY();
        this.yOnScreen = event.getYOnScreen();
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

    public java.awt.event.MouseEvent getParentEvent() {
        return parentEvent;
    }

    public MouseEventType getType() {
        return type;
    }

    public int getButton() {
        return button;
    }

    public Point getLocationOnScreen() {
        return locationOnScreen;
    }

    public int getClickCount() {
        return clickCount;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return x;
    }

    public int getXOnScreen() {
        return xOnScreen;
    }

    public int getY() {
        return y;
    }

    public int getYOnScreen() {
        return yOnScreen;
    }
}
