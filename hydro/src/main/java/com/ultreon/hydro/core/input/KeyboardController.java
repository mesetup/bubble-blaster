package com.ultreon.hydro.core.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.GameEvents;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @see MouseController
 * @see java.awt.event.KeyAdapter
 */
public abstract class KeyboardController extends KeyAdapter {
    private final Set<Integer> keysDown = new CopyOnWriteArraySet<>();
    private final Game game;

    private static final Logger logger = LogManager.getLogger("Game-Input");

    public KeyboardController() {
        logger.info("test");
        logger.info("test");
        this.game = Game.getInstance();
    }

    public final boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        this.keysDown.add(e.getKeyCode());
        if (GameEvents.get() != null) {
            if (isKeyDown(e.getKeyCode())) {
                GameEvents.get().publish(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.HOLD));
            } else {
                GameEvents.get().publish(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.PRESS));
            }
        }

        logger.info("KeyInput: PRESS (" + e.getKeyCode() + ", " + e.getKeyChar() + ")");

        ScreenManager screenManager = this.game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onKeyPress(e.getKeyCode(), e.getKeyChar());
            }
        }
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        this.keysDown.remove(e.getKeyCode());
        if (GameEvents.get() != null) {
            GameEvents.get().publish(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.RELEASE));
        }

        logger.info("KeyInput: RELEASE (" + e.getKeyCode() + ", " + e.getKeyChar() + ")");

        ScreenManager screenManager = this.game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onKeyRelease(e.getKeyCode(), e.getKeyChar());
            }
        }
    }

    @Override
    public final void keyTyped(KeyEvent e) {
        if (GameEvents.get() != null) {
            GameEvents.get().publish(new KeyboardEvent(Game.getInstance(), this, e, KeyEventType.TYPE));
        }

        logger.info("KeyInput: TYPE (" + e.getKeyCode() + ", " + e.getKeyChar() + ")");

        ScreenManager screenManager = this.game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onKeyType(e.getKeyCode(), e.getKeyChar());
            }
        }
    }
}
