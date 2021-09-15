package com.ultreon.hydro.render;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.GameWindow;

public class RenderSettings {
    private final boolean antialiasingBackup = Game.getInstance().isAntialiasEnabled();
    private final GameWindow gameWindow = Game.getInstance().getGameWindow();
    private boolean antialiasingOverride = antialiasingBackup;

    public boolean isAntialiasingEnabled() {
        return antialiasingOverride;
    }

    public void setAntialiasing(boolean antialiasing) {
        antialiasingOverride = antialiasing;
    }

    public void resetAntialiasing() {
        antialiasingOverride = antialiasingBackup;
    }

    public void enableAntialiasing() {
        antialiasingOverride = true;
    }

    public void disableAntialiasing() {
        antialiasingOverride = false;
    }

    public float getScale() {
        if (Game.getInstance() == null) {
            return 1.f;
        }
        int width = gameWindow.getWidth();
        int height = gameWindow.getHeight();

        return width > height ? width / 600f : height / 600f;
    }

    @SuppressWarnings("EmptyMethod")
    public void drawBubble() {

    }
}
