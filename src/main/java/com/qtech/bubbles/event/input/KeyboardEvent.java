package com.qtech.bubbles.event.input;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.event.Event;
import com.qtech.bubbles.event.type.KeyEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * <h1>Keyboard Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see KeyboardEvent
 * @see KeyboardController
 * @see KeyEvent
 * @see KeyEventType
 */
public class KeyboardEvent extends Event {
    private final int extendedKeyCode;
    private final int keyCode;
    private final char keyChar;
    private final int modifiers;
    private final int keyLocation;
    private final long when;

    private final BubbleBlaster main;
    private final KeyboardController controller;
    private final KeyEvent parentEvent;
    private final KeyEventType type;
    private final HashMap<Integer, Boolean> pressed = new HashMap<>();

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The {@link BubbleBlaster} instance.
     * @param controller The {@link KeyboardController} instance.
     * @param event      The {@link KeyEvent} instance.
     * @param type       One of the {@link KeyEventType} constants.
     */
    public KeyboardEvent(BubbleBlaster main, @NotNull KeyboardController controller, KeyEvent event, KeyEventType type) {
        this.main = main;
        this.type = type;
        this.controller = controller;
        this.parentEvent = event;
        this.keyCode = event.getKeyCode();
        this.extendedKeyCode = event.getExtendedKeyCode();
        this.keyChar = event.getKeyChar();
        this.modifiers = event.getModifiersEx();
        this.keyLocation = event.getKeyLocation();
        this.when = event.getWhen();
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

    public int getKeyCode() {
        return keyCode;
    }

    public int getExtendedKeyCode() {
        return extendedKeyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getKeyLocation() {
        return keyLocation;
    }

    public long getWhen() {
        return when;
    }

    public HashMap<Integer, Boolean> getPressed() {
        return pressed;
    }
}
