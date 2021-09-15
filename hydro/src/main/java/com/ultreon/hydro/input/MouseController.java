package com.ultreon.hydro.input;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.event.input.MouseMotionEvent;
import com.ultreon.hydro.event.type.MouseEventType;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @see KeyboardController
 * @see java.awt.event.MouseAdapter
 */
@SuppressWarnings("ConstantConditions")
public class MouseController extends MouseAdapter {
    private static final MouseController INSTANCE = new MouseController();
    private Point currentLocationOnScreen;
    private Point currentPoint;

    private MouseController() {

    }

    public static MouseController instance() {
        return INSTANCE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.CLICK));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.PRESS));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseEvent(Game.getInstance(), this, e, MouseEventType.RELEASE));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.ENTER));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.LEAVE));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.DRAG));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        GameEventBus.get().post(new MouseMotionEvent(Game.getInstance(), this, e, MouseEventType.MOTION));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;
        GameEventBus.get().post(new com.ultreon.hydro.event.input.MouseWheelEvent(Game.getInstance(), this, e, MouseEventType.MOTION));

    }

    public Point getCurrentLocationOnScreen() {
        return currentLocationOnScreen;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }
}
