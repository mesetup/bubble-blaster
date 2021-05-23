package com.qtech.bubbleblaster.core.controllers;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.event.KeyboardEvent;
import com.qtech.bubbleblaster.event.type.KeyEventType;

import java.util.HashSet;

/**
 * @see MouseController
 * @see KeyAdapter
 */
public class KeyboardController extends KeyAdapter {
    private static final KeyboardController INSTANCE = new KeyboardController();
    private final HashSet<Integer> pressed = new HashSet<>();

    public KeyboardController() {
    }

    public static KeyboardController instance() {
        return INSTANCE;
    }

    public synchronized boolean isPressed(int keyCode) {
        return pressed.contains(keyCode);
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if (BubbleBlaster.getEventBus() == null) return;

        if (isPressed(e.getKeyCode())) {
            BubbleBlaster.getEventBus().post(new KeyboardEvent(BubbleBlaster.getInstance(), this, e, KeyEventType.HOLD));
        } else {
            pressed.add(e.getKeyCode());
            BubbleBlaster.getEventBus().post(new KeyboardEvent(BubbleBlaster.getInstance(), this, e, KeyEventType.PRESS));
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        if (BubbleBlaster.getEventBus() == null) return;

        pressed.remove(e.getKeyCode());
        BubbleBlaster.getEventBus().post(new KeyboardEvent(BubbleBlaster.getInstance(), this, e, KeyEventType.RELEASE));
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
        if (BubbleBlaster.getEventBus() == null) return;

        BubbleBlaster.getEventBus().post(new KeyboardEvent(BubbleBlaster.getInstance(), this, e, KeyEventType.TYPE));
    }
}
