package com.ultreon.hydro.screen;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.input.KeyInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.Container;
import com.ultreon.hydro.screen.gui.Widget;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public abstract class Screen extends Container {
    private Widget focusedWidget;
    private int focusIndex = 0;

    public Screen() {
        super(0, 0, Game.getInstance().getWidth(), Game.getInstance().getHeight());
    }

    private boolean eventsActive;
    protected final Game game = Game.getInstance();

    public final void resize(int width, int height) {
        this.onResize(width, height);
        this.width = width;
        this.height = height;
    }

    protected void onResize(int width, int height) {

    }

    public void init(int width, int height) {
        this.init();
    }

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

    @Override
    public void make() {
        eventsActive = true;
    }

    @Override
    public void destroy() {
        eventsActive = false;
    }

    @Override
    public boolean isValid() {
        return eventsActive;
    }

    public void onMouseExit() {
        if (this.hoveredWidget != null) {
            this.hoveredWidget.onMouseLeave();
            this.hoveredWidget = null;
        }
    }

    @Override
    public void onKeyPress(int keyCode, char character) {
        if (keyCode == KeyInput.Map.KEY_TAB) {
            this.focusIndex++;
            onChildFocusChanged();
            return;
        }

        if (this.focusedWidget != null) this.focusedWidget.onKeyPress(keyCode, character);
    }

    @Override
    public void onKeyRelease(int keyCode, char character) {
        if (keyCode == KeyInput.Map.KEY_TAB) return;

        if (this.focusedWidget != null) this.focusedWidget.onKeyRelease(keyCode, character);
    }

    @Override
    public void onKeyType(int keyCode, char character) {
        if (keyCode == KeyInput.Map.KEY_TAB) return;

        if (this.focusedWidget != null) this.focusedWidget.onKeyType(keyCode, character);
    }

    public void onChildFocusChanged() {
        CopyOnWriteArrayList<Widget> clone = new CopyOnWriteArrayList<>(children);
        if (this.focusIndex >= clone.size()) {
            this.focusIndex = 0;
        }

        this.focusedWidget = clone.get(this.focusIndex);
    }

    public Widget getFocusedWidget() {
        return focusedWidget;
    }

    @Deprecated
    public void tick() {

    }

    public abstract void render(Game game, Renderer gg);

    @Override
    public final <T extends Widget> T add(T widget) {
        this.children.add(widget);
        return widget;
    }

    public void renderGUI(Game game, Renderer gg) {
        for (Widget widget : this.children) {
            widget.render(gg);
        }
    }

    public Cursor getDefaultCursor() {
        return Game.getInstance().getDefaultCursor();
    }

    public boolean doesPauseGame() {
        return true;
    }
}
