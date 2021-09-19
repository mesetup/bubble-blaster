package com.ultreon.hydro.screen.gui;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.vector.Vector2i;

public abstract class AbstractButton extends Widget {
    private Runnable command;
    private boolean hovered;
    private boolean pressed;
    private boolean valid;

    public AbstractButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void onMousePress(int x, int y, int button) {
        if (isWithinBounds(x, y) && button == 1) {
            this.pressed = true;
        }
    }

    @Override
    public void onMouseRelease(int x, int y, int button) {
        if (isWithinBounds(x, y) && button == 1) {
            this.command.run();
        }
        this.pressed = false;
    }

//    @Override
//    public void onMouseMove(int x, int y) {
//        Vector2i mousePos = new Vector2i(x, y);
//        if (contains(mousePos)) {
//            Game.getInstance().getGameWindow().setCursor(Game.getInstance().getPointerCursor());
//            hovered = true;
//        } else {
//            if (hovered) {
//                Game.getInstance().getGameWindow().setCursor(Game.getInstance().getDefaultCursor());
//                hovered = false;
//            }
//        }
//
//    }

    @Override
    public void make() {
        this.valid = true;

        Vector2i mousePos = MouseInput.getPos();
        if (isWithinBounds(mousePos)) {
            Game.getInstance().getGameWindow().setCursor(Game.getInstance().getPointerCursor());
            this.hovered = true;
        }
    }

    @Override
    public void destroy() {
        this.valid = false;

        if (this.hovered) {
            Game.getInstance().getGameWindow().setCursor(Game.getInstance().getDefaultCursor());
            this.hovered = false;
        }
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void onMouseEnter(int x, int y) {
        this.hovered = true;
    }

    @Override
    public void onMouseLeave() {
        this.hovered = false;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public Runnable getCommand() {
        return this.command;
    }

    public void setCommand(Runnable command) {
        this.command = command;
    }
}
