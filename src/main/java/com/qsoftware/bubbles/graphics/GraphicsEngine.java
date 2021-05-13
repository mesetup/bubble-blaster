package com.qsoftware.bubbles.graphics;

import com.qsoftware.bubbles.common.bubble.BubbleSpec;
import com.qsoftware.bubbles.settings.GameSettings;

import java.awt.*;

public class GraphicsEngine {
    private final boolean antialiasingBackup = GameSettings.instance().isAntialiasEnabled();
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

    public void drawBubble(Graphics g, int width, BubbleSpec spec) {

    }
}
