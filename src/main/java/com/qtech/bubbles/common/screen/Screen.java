package com.qtech.bubbles.common.screen;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.gui.Widget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    public Screen() {

    }

    private boolean eventsActive;
    private final List<Widget> children = new ArrayList<>();
    protected final QBubbles game = QBubbles.getInstance();

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

    public abstract void render(QBubbles game, Graphics2D gg);

    public final <T extends Widget> T addWidget(T widget) {
        this.children.add(widget);
        return widget;
    }

    public void renderGUI(QBubbles game, Graphics2D gg) {
        for (Widget widget : this.children) {
            widget.paint(gg);
        }
    }

    public Cursor getDefaultCursor() {
        return QBubbles.getInstance().getDefaultCursor();
    }

    public boolean doesPauseGame() {
        return true;
    }
}
