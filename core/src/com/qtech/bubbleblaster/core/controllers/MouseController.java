package com.qtech.bubbleblaster.core.controllers;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.event.MouseMotionEvent;
import com.qtech.bubbleblaster.event.type.MouseEventType;


/**
 * @see KeyboardController
 * @see MouseAdapter
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

        BubbleBlaster.getEventBus().post(new com.qtech.bubbleblaster.event.MouseEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.CLICK));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new com.qtech.bubbleblaster.event.MouseEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.PRESS));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new com.qtech.bubbleblaster.event.MouseEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.RELEASE));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new MouseMotionEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.ENTER));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new MouseMotionEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.LEAVE));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new MouseMotionEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.DRAG));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;

        BubbleBlaster.getEventBus().post(new MouseMotionEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.MOTION));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        currentLocationOnScreen = e.getLocationOnScreen() != null ? e.getLocationOnScreen() : currentLocationOnScreen;
        currentPoint = e.getPoint() != null ? e.getPoint() : currentPoint;
        BubbleBlaster.getEventBus().post(new com.qtech.bubbleblaster.event.MouseWheelEvent(BubbleBlaster.getInstance(), this, e, MouseEventType.MOTION));

    }

    public Point getCurrentLocationOnScreen() {
        return currentLocationOnScreen;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }
}
