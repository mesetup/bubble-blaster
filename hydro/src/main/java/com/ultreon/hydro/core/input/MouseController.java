package com.ultreon.hydro.core.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.event.input.MouseMotionEvent;
import com.ultreon.hydro.event.type.MouseEventType;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 * Mouse controller for the Hydro Game Engine.
 *
 * @see KeyboardController
 * @see java.awt.event.MouseAdapter
 * @implNote should only for internal use.
 * @author Qboi
 */
@SuppressWarnings("ConstantConditions")
public abstract class MouseController extends MouseAdapter {
    // Mouse input values.
    private Point currentLocationOnScreen;
    private Point currentPoint;
    private int clickCount;

    // Logger
    private static final Logger logger = LogManager.getLogger("Game-Input");

    // Other fields.
    private final Map<Integer, Boolean> buttonMap = new HashMap<>();
    private final Game game;

    /**
     *
     */
    public MouseController() {
        this.game = Game.getInstance();
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        this.clickCount = e.getClickCount();

        logger.info("MouseInput[8078f1f3]: CLICK (" + e.getX() + ", " + e.getY() + ") (" + e.getClickCount() + ")");

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.CLICK));
        }

        ScreenManager screenManager = game.getScreenManager();
        logger.info("MouseInput[cd2116e5]: SCREEN_MANAGER (" + screenManager + ")");
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            logger.info("MouseInput[cfd85117]: CURRENT_SCREEN (" + currentScreen + ")");
            if (currentScreen != null) {
                logger.info("MouseInput[c3d4c2a6]: Sending event to: " + currentScreen.getClass().getName());
                currentScreen.onMouseClick(e.getX(), e.getY(), e.getButton(), e.getClickCount());
            }
        }
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        buttonMap.put(e.getButton(), true);

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.PRESS));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMousePress(e.getX(), e.getY(), e.getButton());
            }
        }
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        buttonMap.put(e.getButton(), false);

        logger.info("MouseInput[d22a46e6]: " + e.getPoint());
        logger.info("MouseInput[ac481ce9]: " + currentPoint);

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.RELEASE));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMouseRelease(e.getX(), e.getY(), e.getButton());
            }
        }
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.ENTER));
        }
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.LEAVE));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMouseExit();
            }
        }
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.DRAG));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMouseDrag(e.getX(), e.getY(), e.getButton());
            }
        }
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        out.printf("Mouse Moved: x=%s, y=%s", e.getX(), e.getY());

        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.MOTION));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMouseMove(e.getX(), e.getY());
            }
        }
    }

    @Override
    public final void mouseWheelMoved(MouseWheelEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        if (GameEventBus.get() != null) {
            GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseWheelEvent(Game.getInstance(), this, e, MouseEventType.MOTION));
        }

        ScreenManager screenManager = game.getScreenManager();
        if (screenManager != null) {
            Screen currentScreen = screenManager.getCurrentScreen();
            if (currentScreen != null) {
                currentScreen.onMouseWheel(e.getX(), e.getY(), e.getPreciseWheelRotation(), e.getScrollAmount(), e.getUnitsToScroll());
            }
        }

    }

    protected Point getCurrentLocationOnScreen() {
        return currentLocationOnScreen;
    }

    protected Point getCurrentPoint() {
        return currentPoint;
    }

    protected int getClickCount() {
        return clickCount;
    }

    protected boolean isPressed(int button) {
        return buttonMap.getOrDefault(button, false);
    }
}
