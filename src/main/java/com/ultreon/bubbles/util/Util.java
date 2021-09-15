package com.ultreon.bubbles.util;

import com.google.common.annotations.Beta;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.commons.annotation.FieldsAreNonnullByDefault;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.hydro.gui.Window;
import com.ultreon.hydro.screen.ScreenManager;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class Util {
    public static ScreenManager getSceneManager() {
        return BubbleBlaster.getInstance().getScreenManager();
    }

    public static BubbleBlaster getGame() {
        return BubbleBlaster.getInstance();
    }

    public static Font getGameFont() {
        return BubbleBlaster.getInstance().getGameFont();
    }

    @Deprecated
    @Nullable
    public static java.awt.Window getWindow() {
        return BubbleBlaster.getInstance().getFrame();
    }

    @Deprecated
    @Nullable
    public static Window getWindowCanvas() {
        return null;
    }

    public static void setCursor(Cursor cursor) {
        // Set the cursor to the Game Window.
        BubbleBlaster.getInstance().getGameWindow().setCursor(cursor);
    }

    @Beta
    public static ArrayList<SavedGame> getSaves() {
//        ArrayList<SavedGame> saves = new ArrayList<>();
//        File savesDir = new File(References.SAVES_DIR);
//
//        for (File save : savesDir.listFiles(new DirectoryFilter())) {
//            if (save.isDirectory()) {
//                saves.add(SavedGame.fromFile(save));
//            }
//        }
//
//        return saves;
        return new ArrayList<>();
    }
}
