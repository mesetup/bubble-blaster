package com.ultreon.hydro.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @see MouseController
 * @see java.awt.event.KeyAdapter
 */
public class KeyboardController extends KeyAdapter {
    private static final KeyboardController INSTANCE = new KeyboardController();
    private final Set<Integer> pressed = new CopyOnWriteArraySet<>();

    public KeyboardController() {
    }

    public static KeyboardController instance() {
        return INSTANCE;
    }

    public boolean isPressed(int keyCode) {
        return pressed.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameEventBus.get() == null) return;

        if (isPressed(e.getKeyCode())) {
            GameEventBus.get().post(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.HOLD));
        } else {
            pressed.add(e.getKeyCode());
            GameEventBus.get().post(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.PRESS));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (GameEventBus.get() == null) return;

        pressed.remove(e.getKeyCode());
        GameEventBus.get().post(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.RELEASE));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (GameEventBus.get() == null) return;

        GameEventBus.get().post(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.TYPE));
    }
}
