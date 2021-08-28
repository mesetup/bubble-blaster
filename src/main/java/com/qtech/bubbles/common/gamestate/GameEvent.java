package com.qtech.bubbles.common.gamestate;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.registry.RegistryEntry;
import com.qtech.bubbles.screen.Screen;
import com.qtech.bubbles.util.Util;
import com.qtech.utilities.datetime.DateTime;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@SuppressWarnings({"unused"})
public abstract class GameEvent extends RegistryEntry {
    private Color backgroundColor;

    public GameEvent() {
        if (BubbleBlaster.getEventBus() == null) {
            throw new NullPointerException();
        }
        BubbleBlaster.getEventBus().register(this);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isActive(DateTime dateTime) {
        @Nullable Screen currentScene = Util.getSceneManager().getCurrentScreen();

        LoadedGame loadedGame = BubbleBlaster.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return false;
        }

        return loadedGame.getGameType().isGameStateActive(this);
    }

    public final Color getBackgroundColor() {
        return backgroundColor;
    }

    public void renderBackground(BubbleBlaster game, Graphics2D gg) {
        if (backgroundColor == null) return;
        if (!isActive(DateTime.current())) return;

        gg.setColor(getBackgroundColor());
        gg.fill(BubbleBlaster.getInstance().getBounds());
    }

    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
