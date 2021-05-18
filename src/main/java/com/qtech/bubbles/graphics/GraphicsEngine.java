package com.qtech.bubbles.graphics;

import com.qtech.bubbles.settings.GameSettings;

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

    @SuppressWarnings("EmptyMethod")
    public void drawBubble() {

    }
}
