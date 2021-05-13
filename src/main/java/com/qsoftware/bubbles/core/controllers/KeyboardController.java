package com.qsoftware.bubbles.core.controllers;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.SceneManager;
import com.qsoftware.bubbles.event.KeyboardEvent;
import com.qsoftware.bubbles.event.type.KeyEventType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * @see MouseController
 * @see java.awt.event.KeyAdapter
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if (QBubbles.getEventBus() == null) return;

        if (isPressed(e.getKeyCode())) {
            QBubbles.getEventBus().post(new KeyboardEvent(QBubbles.getInstance(), SceneManager.getInstance().getCurrentScene(), this, e, KeyEventType.HOLD));
        } else {
            pressed.add(e.getKeyCode());
            QBubbles.getEventBus().post(new KeyboardEvent(QBubbles.getInstance(), SceneManager.getInstance().getCurrentScene(), this, e, KeyEventType.PRESS));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public synchronized void keyReleased(KeyEvent e) {
        if (QBubbles.getEventBus() == null) return;

        pressed.remove(e.getKeyCode());
        QBubbles.getEventBus().post(new KeyboardEvent(QBubbles.getInstance(), SceneManager.getInstance().getCurrentScene(), this, e, KeyEventType.RELEASE));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public synchronized void keyTyped(KeyEvent e) {
        if (QBubbles.getEventBus() == null) return;

        QBubbles.getEventBus().post(new KeyboardEvent(QBubbles.getInstance(), SceneManager.getInstance().getCurrentScene(), this, e, KeyEventType.TYPE));
    }
}
