package com.qtech.bubbles.common.gamestate;

import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.RegistryEntry;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.util.Util;
import com.qtech.utilities.datetime.DateTime;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@SuppressWarnings({"unused"})
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
        @Nullable Screen currentScene = Util.getSceneManager().getCurrentScreen();

        LoadedGame loadedGame = QBubbles.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return false;
        }

        return loadedGame.getGameType().isGameStateActive(this);
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
