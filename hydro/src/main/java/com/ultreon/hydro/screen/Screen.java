package com.ultreon.hydro.screen;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.gui.Widget;
import com.ultreon.hydro.render.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    public Screen() {

    }

    private boolean eventsActive;
    private final List<Widget> children = new ArrayList<>();
    protected final Game game = Game.getInstance();

    /**
     * <h1>Show Scene</h1>
     *
     * @author Qboi
     */
    public abstract void init();

    /**
     * <h1>Hide Scene</h1>
     * Hide scene, unbind events.
     *
     * @param to the next scene to go.
     * @return true to cancel change screen.
     * @author Qboi
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

    @Deprecated
    public void tick() {

    }

    public abstract void render(Game game, Renderer gg);

    public final <T extends Widget> T addWidget(T widget) {
        this.children.add(widget);
        return widget;
    }

    public void renderGUI(Game game, Renderer gg) {
        for (Widget widget : this.children) {
            widget.paint(gg);
        }
    }

    public Cursor getDefaultCursor() {
        return Game.getInstance().getDefaultCursor();
    }

    public boolean doesPauseGame() {
        return true;
    }
}
