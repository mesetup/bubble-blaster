package qtech.bubbles.util

import com.google.common.annotations.Beta
import qtech.bubbles.BubbleBlaster
import qtech.bubbles.mods.loader.ModLoader
import qtech.bubbles.annotation.FieldsAreNonnullByDefault
import qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import qtech.bubbles.common.scene.ScreenManager
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.screen.LoadScreen.Companion.getAddonLoader
import qtech.hydro.Game
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

    @JvmStatic
    fun setCursor(cursor: Cursor) {
        // Set the cursor to the JFrame.
        if (!BubbleBlaster.isHeadless()) {
            Game.instance.cursor = cursor
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
    val modLoader: ModLoader?
        get() = getAddonLoader()
}