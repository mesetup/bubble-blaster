package com.qtech.bubbleblaster.common.gamestate;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.LoadedGame;
import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.util.Util;
import com.qtech.utilities.datetime.DateTime;
import org.jetbrains.annotations.Nullable;


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

    public synchronized void renderBackground(BubbleBlaster game, GraphicsProcessor gg) {
        if (backgroundColor == null) return;
        if (!isActive(DateTime.current())) return;

        gg.setColor(getBackgroundColor());
        gg.fill(BubbleBlaster.getInstance().getBounds());
    }

    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}