package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.scene.ScreenManager
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.common.text.translation.I18n.translateToLocal
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.bubbles.event.RenderEventPriority
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.gui.TitleButton
import qtech.bubbles.util.Util
import qtech.hydro.Game
import java.awt.Color
import java.awt.Font
import java.awt.GradientPaint
import java.awt.geom.Rectangle2D

class TitleScreen : Screen() {
    private val startButton: TitleButton
    private val languageButton: TitleButton
    private val savesButton: TitleButton
    private val optionsButton: TitleButton
    private var message = ""
    private var ticks = 0

    private fun openSavesSelection() {
        val screenManager: ScreenManager = Util.sceneManager
        screenManager.displayScreen(SavesScreen(this))
    }

    private fun openLanguageSettings() {
        val screenManager: ScreenManager = Util.sceneManager
        screenManager.displayScreen(LanguageScreen(this))
    }

    private fun openOptions() {
        val screenManager: ScreenManager = Util.sceneManager
        screenManager.displayScreen(OptionsScreen(this))
    }

    private fun startGame() {
//        ScreenManager screenManager = Util.getSceneManager();
//        screenManager.displayScreen(new EnvLoadScreen(SavedGame.fromFile(new File(References.SAVES_DIR, "save")), GameTypes.CLASSIC_TYPE::get));
        BubbleBlaster.instance.loadGame()
    }

    override fun init() {
        message = ""
        BubbleBlaster.eventBus.register(this)
        startButton.bindEvents()
        savesButton.bindEvents()
        optionsButton.bindEvents()
        languageButton.bindEvents()
    }

    override fun onClose(to: Screen?): Boolean {
        BubbleBlaster.eventBus.unregister(this)
        startButton.unbindEvents()
        savesButton.unbindEvents()
        optionsButton.unbindEvents()
        languageButton.unbindEvents()
        return super.onClose(to)
    }

    override fun renderGUI(game: Game, gg: GraphicsProcessor) {
        gg.color(Color(128, 128, 128))
        gg.shapeF(BubbleBlaster.instance.bounds)
        startButton.text = translateToLocal("scene.bubbleblaster.title.start")
        optionsButton.text = translateToLocal("scene.bubbleblaster.title.options")
        languageButton.text = translateToLocal("scene.bubbleblaster.title.language")
        startButton.x = 0
        optionsButton.x = BubbleBlaster.instance.width - 225
        languageButton.x = BubbleBlaster.instance.width - 200
        savesButton.x = 0
        gg.color(Color(64, 64, 64))
        gg.rectF(0, 0, BubbleBlaster.instance.width, 175)
        val shiftX = BubbleBlaster.instance.width.toDouble() * 2 * BubbleBlaster.ticks / (BubbleBlaster.TPS * 10)
        val p = GradientPaint(shiftX.toFloat() - BubbleBlaster.instance.width, 0f, Color(0, 192, 255), shiftX.toFloat(), 0f, Color(0, 255, 192), true)
        gg.paint(p)
        gg.rectF(0, 175, BubbleBlaster.instance.width, 3)
        gg.color(Color(255, 255, 255))
        drawCenteredString(gg, "Bubble Blaster 2", Rectangle2D.Double(0.0, 0.0, BubbleBlaster.instance.width.toDouble(), 145.0), Font(BubbleBlaster.instance.gameFontName, Font.PLAIN, 87))
        gg.color(Color(255, 255, 255))
        drawCenteredString(gg, message, Rectangle2D.Double(0.0, 145.0, BubbleBlaster.instance.width.toDouble(), 30.0), Font(BubbleBlaster.instance.sansFontName, Font.PLAIN, 24))
        startButton.paint(gg)
        languageButton.paint(gg)
        savesButton.paint(gg)
        optionsButton.paint(gg)
    }

    override fun render(game: Game, gp: GraphicsProcessor) {}
    override fun tick() {
        ticks = (ticks + 1) % (BubbleBlaster.TPS * 10)
    }

    companion object {
        private lateinit var INSTANCE: TitleScreen
        fun instance(): TitleScreen {
            return INSTANCE
        }
    }

    init {
        INSTANCE = this
        startButton = TitleButton.Builder().bounds(0, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Start Game").command { startGame() }.build()
        optionsButton = TitleButton.Builder().bounds(BubbleBlaster.instance.width - 225, 220, 225, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Options").command { openOptions() }.build()
        savesButton = TitleButton.Builder().bounds(0, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Select Save (WIP)").command { openSavesSelection() }.build()
        languageButton = TitleButton.Builder().bounds(BubbleBlaster.instance.width - 200, 280, 200, 48).renderPriority(RenderEventPriority.AFTER_FILTER).text("Language").command { openLanguageSettings() }.build()
    }
}