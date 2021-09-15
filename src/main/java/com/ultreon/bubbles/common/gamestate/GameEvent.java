package com.ultreon.bubbles.common.gamestate;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.bubbles.util.Util;
import com.ultreon.commons.utilities.datetime.DateTime;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;

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

    public void renderBackground(BubbleBlaster game, Renderer gg) {
        if (backgroundColor == null) return;
        if (!isActive(DateTime.current())) return;

        gg.color(getBackgroundColor());
        gg.fill(BubbleBlaster.getInstance().getGameBounds());
    }

    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    @Override
    public String toString() {
        return "GameEvent[" + getRegistryName() + "]";
    }
}
