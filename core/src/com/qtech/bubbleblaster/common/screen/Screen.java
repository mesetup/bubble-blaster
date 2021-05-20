package com.qtech.bubbleblaster.common.screen;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.gui.Widget;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    public Screen() {

    }

    private boolean eventsActive;
    private final List<Widget> children = new ArrayList<>();
    protected final BubbleBlaster game = BubbleBlaster.getInstance();

    /**
     * <h1>Show Scene</h1>
     *
     * @author Quinten Jungblut
     */
    public abstract void init();

    /**
     * <h1>Hide Scene</h1>
     * Hide scene, unbind events.
     *
     * @param to the next scene to go.
     * @return true to cancel change screen.
     * @author Quinten Jungblut
     */
    public boolean onClose(Screen to) {
        return true;
    }

    public void bindEvents() {
        eventsActive = true;
    }

    public void unbindEvents() {
        eventsActive = false;
    }

    public boolean eventsAreActive() {
        return eventsActive;
    }

    public void tick() {

    }

    public abstract void render(BubbleBlaster game, GraphicsProcessor gg);

    public final <T extends Widget> T addWidget(T widget) {
        this.children.add(widget);
        return widget;
    }

    public void renderGUI(BubbleBlaster game, GraphicsProcessor gg) {
        for (Widget widget : this.children) {
            widget.paint(gg);
        }
    }

    public Cursor getDefaultCursor() {
        return BubbleBlaster.getInstance().getDefaultCursor();
    }

    public boolean doesPauseGame() {
        return true;
    }
}