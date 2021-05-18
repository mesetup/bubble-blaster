package com.qtech.bubbles.util;

import com.google.common.annotations.Beta;
import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.common.scene.ScreenManager;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.gui.Window;
import com.qtech.bubbles.scene.LoadScreen;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class Util {

    public static ScreenManager getSceneManager() {
        return QBubbles.getInstance().getScreenManager();
    }

    public static QBubbles getGame() {
        return QBubbles.getInstance();
    }

    public static Font getGameFont() {
        return QBubbles.getInstance().getGameFont();
    }

    public static java.awt.Window getWindow() {
        return QBubbles.getInstance().getFrame();
    }

    public static Window getWindowCanvas() {
        return QBubbles.getInstance().getWindow();
    }

    public static void setCursor(Cursor cursor) {
        // Set the cursor to the JFrame.
        QBubbles.getInstance().getFrame().setCursor(cursor);
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
