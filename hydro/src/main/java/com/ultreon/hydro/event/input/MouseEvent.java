package com.ultreon.hydro.event.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.core.input.KeyboardController;
import com.ultreon.hydro.core.input.MouseController;
import com.ultreon.hydro.event.Event;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.event.type.MouseEventType;
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

    private final Game game;
    private final @NotNull MouseController controller;
    private final java.awt.event.MouseEvent parentEvent;
    private final MouseEventType type;

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param game       The {@link Game} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public MouseEvent(Game game, @NotNull MouseController controller, java.awt.event.MouseEvent event, MouseEventType type) {
        this.game = game;
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
    public Game getGame() {
        return game;
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
