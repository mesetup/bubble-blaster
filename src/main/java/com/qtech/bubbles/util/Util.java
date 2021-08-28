package com.qtech.bubbles.util;

import com.google.common.annotations.Beta;
import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.gui.Window;
import com.qtech.bubbles.screen.LoadScreen;
import com.qtech.bubbles.screen.ScreenManager;
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

    public static java.awt.Window getWindow() {
        return BubbleBlaster.getInstance().getFrame();
    }

    public static Window getWindowCanvas() {
        return BubbleBlaster.getInstance().getWindow();
    }

    public static void setCursor(Cursor cursor) {
        // Set the cursor to the JFrame.
        BubbleBlaster.getInstance().getFrame().setCursor(cursor);
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

    @Nullable
    public static AddonLoader getAddonLoader() {
        return LoadScreen.getAddonLoader();
    }
}
