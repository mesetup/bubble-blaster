@file:Suppress("unused", "UNUSED_PARAMETER")

package com.qtech.bubbles.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.LoadedGame
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.core.common.SavedGame
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.TickEvent
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.save.SaveLoader
import com.qtech.bubbles.util.Util
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier
import javax.annotation.ParametersAreNonnullByDefault

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
class SavesScreen(backScreen: Screen?) : Screen() {
    private val saves: Collection<Supplier<SavedGame>>
    private val selectedSave: SavedGame? = null
    private var backScreen: Screen?

    // Mouse listener.
    private class MyMouseListener : MouseAdapter() {
        override fun mouseClicked(me: MouseEvent) {
            println(me)
        }
    }

    private fun newSave() {
//        Objects.requireNonNull(Util.getSceneManager()).displayScene(new CreateSaveScene(this));
    }

    private fun openSave() {
        if (selectedSave != null) {
            game.loadSave(selectedSave.directory.name)
        }
    }

    private fun showLanguages() {
        Objects.requireNonNull(Util.sceneManager).displayScreen(LanguageScreen(this))
    }

    private fun back() {
        Objects.requireNonNull(Util.sceneManager).displayScreen(backScreen)
    }

    override fun init() {
        BubbleBlaster.eventBus.register(this)

//        panel.setVisible(true);

//        languageButton.bindEvents();
//        cancelButton.bindEvents();
//        scrollPane.validate();
    }

    override fun onClose(to: Screen?): Boolean {
        BubbleBlaster.eventBus.unregister(this)

//        panel.setVisible(false);

//        languageButton.unbindEvents();
//        cancelButton.unbindEvents();
        if (to === backScreen) {
            backScreen = null
        }

//        scrollPane.invalidate();
        return super.onClose(to)
    }

    override fun render(game: BubbleBlaster, gp: GraphicsProcessor) {
//        languageButton.setX((int) Game.getMiddleX() - 322);
//        languageButton.setY((int) Game.getMiddleY() + 101);

//        cancelButton.setX((int) Game.getMiddleX() - 322);
//        cancelButton.setY((int) Game.getMiddleY() + 151);

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }
        renderBackground(game, gp)
    }

    @Synchronized
    override fun renderGUI(game: BubbleBlaster, gg: GraphicsProcessor) {
//        cancelButton.setText(I18n.translateToLocal("other.cancel"));
//        cancelButton.render(game, gg);

//        languageButton.setText(I18n.translateToLocal("scene.qbubbles.options.language"));
//        languageButton.render(game, gg);

//        scrollPane.setPreferredSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        scrollPane.setSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        panel.setPreferredSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        panel.setSize(new Dimension(800, QBubbles.getInstance().getHeight()));
//        scrollPane.setLocation(Game.instance().getWidth() / 2 - 300, 0);

//        scrollPane.paintAll(gg.create(Game.instance().getWidth() / 2 - 400, 0, 800, Game.instance().getHeight()));
//        scrollPane.repaint(0);
//        scrollPane.addNotify();
//        scrollPane.invalidate();

//        Game.instance().getWindow().repaint();
//        Game.instance().getWindow().revalidate();

//        scrollPane.setVisible(true);

//        panel.revalidate();
//        scrollPane.repaint();
//        panel.repaint(0);
//        scrollPane.repaint(0);
//        panel.revalidate();
//        scrollPane.revalidate();

////        savesDisplay.paint(gg);
////        savesDisplay.paintComponents(gg);
////        savesDisplay.paintAll(gg);
//        savesDisplay.paint(gg.create(Game.instance().getWidth() / 2 - 400, 0, 800, Game.instance().getHeight()));
//        savesDisplay.repaint(0);
    }

    @Synchronized
    fun renderBackground(game: BubbleBlaster?, gg: GraphicsProcessor?) {
        gg!!.color(Color(96, 96, 96))
        gg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
    }

    @SubscribeEvent
    fun onUpdate(evt: TickEvent?) {
    }

    companion object {
        var instance: SavesScreen? = null
            private set
    }

    init {
        instance = this

        // Configure back scene.
        this.backScreen = backScreen

        // Logging.
        println("Initializing SavesScene")
        saves = SaveLoader.instance.getSaves()
    }
}