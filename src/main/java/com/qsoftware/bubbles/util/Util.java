package com.qsoftware.bubbles.util;

import com.google.common.annotations.Beta;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.addon.loader.AddonLoader;
import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.scene.SceneManager;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.gui.Window;
import com.qsoftware.bubbles.scene.LoadScene;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class Util {

    public static SceneManager getSceneManager() {
        return QBubbles.getInstance().getSceneManager();
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
        return LoadScene.getAddonLoader();
    }
}
