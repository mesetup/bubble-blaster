package com.qtech.bubbles.rendering;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.settings.GameSettings;

public class RenderSettings {
    private final boolean antialiasingBackup = GameSettings.instance().isAntialiasEnabled();
    private BubbleBlaster gameWindow = BubbleBlaster.getInstance();
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
        if (gameWindow == null) {
            gameWindow = BubbleBlaster.getInstance();
            if (gameWindow == null) {
                return 1.f;
            }
        }
        int width = gameWindow.getWidth();
        int height = gameWindow.getHeight();

        return width > height ? width / 600f : height / 600f;
    }

    @SuppressWarnings("EmptyMethod")
    public void drawBubble() {

    }
}
