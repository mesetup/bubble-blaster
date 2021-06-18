package com.qtech.bubbles.util

import com.google.common.annotations.Beta
import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.addon.loader.AddonLoader
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.scene.ScreenManager
import com.qtech.bubbles.core.common.SavedGame
import com.qtech.bubbles.screen.LoadScreen.Companion.getAddonLoader
import java.awt.Cursor
import java.awt.Font
import java.awt.Window
import javax.annotation.ParametersAreNonnullByDefault

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
object Util {
    @JvmStatic
    val sceneManager: ScreenManager
        get() = BubbleBlaster.instance.screenManager
    @JvmStatic
    val game: BubbleBlaster
        get() = BubbleBlaster.instance
    val gameFont: Font
        get() = BubbleBlaster.instance.gameFont
    val window: Window
        get() = BubbleBlaster.instance.frame
    val windowCanvas: com.qtech.bubbles.gui.Window
        get() = BubbleBlaster.instance.window

    @JvmStatic
    fun setCursor(cursor: Cursor?) {
        // Set the cursor to the JFrame.
        if (!BubbleBlaster.isHeadless()) {
            BubbleBlaster.instance.frame.cursor = cursor
        }
    }

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
    @get:Beta
    val saves: ArrayList<SavedGame>
        get() =//        ArrayList<SavedGame> saves = new ArrayList<>();
//        File savesDir = new File(References.SAVES_DIR);
//
//        for (File save : savesDir.listFiles(new DirectoryFilter())) {
//            if (save.isDirectory()) {
//                saves.add(SavedGame.fromFile(save));
//            }
//        }
//
//        return saves;
            ArrayList()
    val addonLoader: AddonLoader?
        get() = getAddonLoader()
}