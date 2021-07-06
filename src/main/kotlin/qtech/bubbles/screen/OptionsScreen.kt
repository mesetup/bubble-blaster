package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.common.text.translation.I18n.translateToLocal
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.TickEvent
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.gui.OptionsButton
import qtech.bubbles.gui.OptionsNumberInput
import qtech.bubbles.settings.GameSettings
import qtech.bubbles.util.Util
import qtech.hydro.Game
import java.awt.Color
import java.util.*

@Suppress("UNUSED_PARAMETER")
class OptionsScreen(backScene: Screen?) : Screen() {
    private val maxBubblesOption: OptionsNumberInput
    private val languageButton: OptionsButton
    private val cancelButton: OptionsButton
    private val saveButton: OptionsButton
    private var backScene: Screen?
    private fun save() {
        val maxBubbles: Int = maxBubblesOption.value
        val settings: GameSettings = GameSettings.instance()
        settings.maxBubbles = maxBubbles
        settings.save()
    }

    private fun showLanguages() {
        Objects.requireNonNull(Util.sceneManager).displayScreen(LanguageScreen(this))
    }

    private fun back() {
        Objects.requireNonNull(Util.sceneManager).displayScreen(backScene)
    }

    override fun init() {
        BubbleBlaster.eventBus.register(this)
        maxBubblesOption.bindEvents()
        languageButton.bindEvents()
        cancelButton.bindEvents()
        saveButton.bindEvents()
    }

    override fun onClose(to: Screen?): Boolean {
        BubbleBlaster.eventBus.unregister(this)
        maxBubblesOption.unbindEvents()
        languageButton.unbindEvents()
        cancelButton.unbindEvents()
        saveButton.unbindEvents()
        if (to === backScene) {
            backScene = null
        }
        return super.onClose(to)
    }

    override fun render(game: Game, gp: GraphicsProcessor) {
        maxBubblesOption.x = BubbleBlaster.middleX.toInt() - 322
        maxBubblesOption.y = BubbleBlaster.middleY.toInt() + 101
        maxBubblesOption.width = 321
        languageButton.x = BubbleBlaster.middleX.toInt() + 1
        languageButton.y = BubbleBlaster.middleY.toInt() + 101
        languageButton.width = 321
        cancelButton.x = BubbleBlaster.middleX.toInt() - 322
        cancelButton.y = BubbleBlaster.middleY.toInt() + 151
        cancelButton.width = 321
        saveButton.x = BubbleBlaster.middleX.toInt() + 1
        saveButton.y = BubbleBlaster.middleY.toInt() + 151
        saveButton.width = 321
        renderBackground(game, gp)
    }

    override fun renderGUI(game: Game, gg: GraphicsProcessor) {
        cancelButton.text = translateToLocal("other.cancel")
        cancelButton.paint(gg)
        languageButton.text = translateToLocal("scene.bubbleblaster.options.language")
        languageButton.paint(gg)
        maxBubblesOption.render(gg)
        saveButton.paint(gg)
        saveButton.text = translateToLocal("other.save")
    }

    @Synchronized
    fun renderBackground(game: Game, gg: GraphicsProcessor) {
        gg.color(Color(96, 96, 96))
        gg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
    }

    @SubscribeEvent
    fun onUpdate(evt: TickEvent) {
    }

    companion object {
        private var INSTANCE: OptionsScreen? = null
        fun instance(): OptionsScreen? {
            return INSTANCE
        }
    }

    init {
        INSTANCE = this
        this.backScene = backScene
        maxBubblesOption = OptionsNumberInput(0, 0, 321, 48, GameSettings.instance().maxBubbles, 400, 2000)
        languageButton = OptionsButton.Builder().bounds(0, 0, 321, 48).command { showLanguages() }.build()
        cancelButton = OptionsButton.Builder().bounds(0, 0, 321, 48).command { back() }.build()
        saveButton = OptionsButton.Builder().bounds(0, 0, 321, 48).command { save() }.build()
    }
}