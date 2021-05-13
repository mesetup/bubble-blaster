package com.qsoftware.bubbles.common.gamestate;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
import com.qsoftware.utilities.datetime.DateTime;

import java.awt.*;

@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class GameEvent extends RegistryEntry {
    private Color backgroundColor;

    public GameEvent() {
        if (QBubbles.getEventBus() == null) {
            throw new NullPointerException();
        }
        QBubbles.getEventBus().register(this);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isActive(DateTime dateTime) {
        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (!(currentScene instanceof GameScene)) return false;
        GameScene gameScene = (GameScene) currentScene;

        return gameScene.getGameType().isGameStateActive(this);
    }

    public final Color getBackgroundColor() {
        return backgroundColor;
    }

    public synchronized void renderBackground(QBubbles game, Graphics2D gg) {
        if (backgroundColor == null) return;
        if (!isActive(DateTime.current())) return;

        gg.setColor(getBackgroundColor());
        gg.fill(QBubbles.getInstance().getBounds());
    }

    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
