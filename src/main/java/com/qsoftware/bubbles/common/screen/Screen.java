package com.qsoftware.bubbles.common.screen;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.GraphicsProcessor;
import com.qsoftware.bubbles.common.interfaces.PauseTickable;
import com.qsoftware.bubbles.common.scene.Scene;

public abstract class Screen<T extends Screen<T>> implements PauseTickable {
    private final ScreenType<T> type;
    private final Scene scene;

    public Screen(ScreenType<T> type, Scene scene) {
        this.type = type;
        this.scene = scene;
    }

    //    public abstract void loadUI(GameType gameType);
    public abstract void openScreen();

    public abstract void closeScreen();

    public abstract void renderGUI(QBubbles game, GraphicsProcessor ngg);

    public Scene getScene() {
        return scene;
    }

    public ScreenType<T> getType() {
        return type;
    }
}
