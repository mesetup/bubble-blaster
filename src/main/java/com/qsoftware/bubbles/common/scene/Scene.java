package com.qsoftware.bubbles.common.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.interfaces.Listener;
import com.qsoftware.bubbles.gui.Widget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Scene baseclass</h1>
 *
 * @author Quinten Jungblut
 * @see SceneManager
 */
public abstract class Scene implements Listener {
    private boolean eventsActive;
    private final List<Widget> children = new ArrayList<>();
    protected QBubbles game = QBubbles.getInstance();

    /**
     * <h1>Show Scene</h1>
     *
     * @author Quinten Jungblut
     */
    public abstract void showScene();

    /**
     * <h1>Hide Scene</h1>
     * Hide scene, unbind events.
     *
     * @param to the next scene to go.
     * @return
     * @author Quinten Jungblut
     */
    public boolean hideScene(Scene to) {
        return true;
    }

    @Override
    public void bindEvents() {
        eventsActive = true;
    }

    @Override
    public void unbindEvents() {
        eventsActive = false;
    }

    @Override
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
}
