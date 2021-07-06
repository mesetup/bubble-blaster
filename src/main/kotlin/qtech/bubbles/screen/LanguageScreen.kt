package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.common.text.translation.I18n.translateToLocal
import qtech.bubbles.event.RenderEventPriority
import qtech.bubbles.event.SubscribeEvent
import qtech.hydro.Animation
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.gui.OptionsButton
import qtech.bubbles.registry.LocaleManager
import qtech.bubbles.settings.GameSettings
import qtech.bubbles.util.Util
import qtech.hydro.Game
import java.awt.Color
import java.util.*

class LanguageScreen(backScene: Screen?) : Screen() {
    private val loader: LanguageLoader
    private var languageIndex = 1
    private val button1: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val button2: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val button3: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val button4: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val button5: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val button6: OptionsButton = OptionsButton.Builder().bounds(0, 0, 512, 48).renderPriority(RenderEventPriority.GUI_SCREEN).build()
    private val prev: OptionsButton = OptionsButton.Builder().bounds(0, 0, 64, 298).renderPriority(RenderEventPriority.GUI_SCREEN).text("Prev").build()
    private val next: OptionsButton = OptionsButton.Builder().bounds(0, 0, 64, 298).renderPriority(RenderEventPriority.GUI_SCREEN).text("Next").build()
    private val cancelButton: OptionsButton = OptionsButton.Builder().bounds(0, 0, 644, 48).renderPriority(RenderEventPriority.GUI_SCREEN).text("Cancel").build()
    private val nameLocaleMap: TreeMap<String, Locale> = TreeMap<String, Locale>()
    private var backScene: Screen?
    private var deltaIndex = 0
    private var animation1: Animation? = null
    private var animation2: Animation? = null
    private var deltaPage = 0

    private inner class LanguageLoader {
        @SubscribeEvent
        fun onLoadComplete() {
            onPostInitialize()
        }
    }

    private fun back() {
        Objects.requireNonNull(Util.sceneManager).displayScreen(backScene)
    }

    private fun onPostInitialize() {
        for (locale in LocaleManager.manager.keys()) {
            val name: String = if (locale.displayCountry != "") locale.displayLanguage + " (" + locale.displayCountry + ")" else {
                locale.displayLanguage
            }
            nameLocaleMap[name] = locale
        }
        BubbleBlaster.eventBus.unregister(loader)
    }

    private fun cmdButton6() {
        setLanguage(languageIndex + 5)
    }

    private fun cmdButton5() {
        setLanguage(languageIndex + 4)
    }

    private fun cmdButton4() {
        setLanguage(languageIndex + 3)
    }

    private fun cmdButton3() {
        setLanguage(languageIndex + 2)
    }

    private fun cmdButton2() {
        setLanguage(languageIndex + 1)
    }

    private fun cmdButton1() {
        setLanguage(languageIndex)
    }

    private fun setLanguage(languageIndex: Int) {
        val locale: Locale = ArrayList(nameLocaleMap.values)[languageIndex]
        val settings: GameSettings = GameSettings.instance()
        settings.language = locale.toString()
        settings.save()
        back()
    }

    private fun nextPage() {
        if (animation1 == null && animation2 == null) {
//            languageIndex += 6;
            deltaIndex = 6
            deltaPage = 1
            animation1 = Animation(0.0, (512 * -deltaPage).toDouble(), 0.05)
            animation1!!.start()
            println("next")
        }
    }

    private fun prevPage() {
        if (animation1 == null && animation2 == null) {
//            languageIndex -= 6;
            deltaIndex = -6
            deltaPage = -1
            animation1 = Animation(0.0, (512 * -deltaPage).toDouble(), 0.05)
            animation1!!.start()
            println("prev")
        }
    }

    override fun init() {
        BubbleBlaster.eventBus.register(this)
        button1.bindEvents()
        button2.bindEvents()
        button3.bindEvents()
        button4.bindEvents()
        button5.bindEvents()
        button6.bindEvents()
        next.bindEvents()
        prev.bindEvents()
        cancelButton.bindEvents()
    }

    override fun onClose(to: Screen?): Boolean {
        BubbleBlaster.eventBus.unregister(this)
        button1.unbindEvents()
        button2.unbindEvents()
        button3.unbindEvents()
        button4.unbindEvents()
        button5.unbindEvents()
        button6.unbindEvents()
        next.unbindEvents()
        prev.unbindEvents()
        cancelButton.unbindEvents()
        if (to === backScene) {
            backScene = null
        }
        return super.onClose(to)
    }

    override fun render(game: Game, gp: GraphicsProcessor) {
        prev.x = BubbleBlaster.middleX.toInt() - 322
        prev.y = BubbleBlaster.middleY.toInt() - 149
        button1.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button2.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button3.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button4.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button5.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button6.visualX = 0 /*(int) Game.getMiddleX() - 256*/
        button1.visualY =  /*(int) Game.getMiddleY() + */0 /* - 149*/
        button2.visualY =  /*(int) Game.getMiddleY() + */50 /* - 99*/
        button3.visualY =  /*(int) Game.getMiddleY() + */100 /* - 49*/
        button4.visualY =  /*(int) Game.getMiddleY() + */150 /* + 1*/
        button5.visualY =  /*(int) Game.getMiddleY() + */200 /* + 51*/
        button6.visualY =  /*(int) Game.getMiddleY() + */250 /* + 101*/
        button1.x = BubbleBlaster.middleX.toInt() - 256
        button2.x = BubbleBlaster.middleX.toInt() - 256
        button3.x = BubbleBlaster.middleX.toInt() - 256
        button4.x = BubbleBlaster.middleX.toInt() - 256
        button5.x = BubbleBlaster.middleX.toInt() - 256
        button6.x = BubbleBlaster.middleX.toInt() - 256
        button1.y = BubbleBlaster.middleY.toInt() - 149
        button2.y = BubbleBlaster.middleY.toInt() - 99
        button3.y = BubbleBlaster.middleY.toInt() - 49
        button4.y = BubbleBlaster.middleY.toInt() + 1
        button5.y = BubbleBlaster.middleY.toInt() + 51
        button6.y = BubbleBlaster.middleY.toInt() + 101
        next.x = BubbleBlaster.middleX.toInt() + 258
        next.y = BubbleBlaster.middleY.toInt() - 149
        cancelButton.x = BubbleBlaster.middleX.toInt() - 322
        cancelButton.y = BubbleBlaster.middleY.toInt() + 151

//        if (evt.getPriority() == RenderEventPriority.BACKGROUND) {
//        }

//        if (evt.getPriority() == RenderEventPriority.FOREGROUND) {
//        }
        renderBackground(gp)
    }

    override fun renderGUI(game: Game, gg: GraphicsProcessor) {
        var loc1: Locale? = null
        var loc2: Locale? = null
        var loc3: Locale? = null
        var loc4: Locale? = null
        var loc5: Locale? = null
        var loc6: Locale? = null
        try {
            loc1 = ArrayList(nameLocaleMap.values)[languageIndex]
            button1.text = loc1.getDisplayLanguage(loc1) + " (" + loc1.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        try {
            loc2 = ArrayList(nameLocaleMap.values)[languageIndex + 1]
            button2.text = loc2.getDisplayLanguage(loc2) + " (" + loc2.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        try {
            loc3 = ArrayList(nameLocaleMap.values)[languageIndex + 2]
            button3.text = loc3.getDisplayLanguage(loc3) + " (" + loc3.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        try {
            loc4 = ArrayList(nameLocaleMap.values)[languageIndex + 3]
            button4.text = loc4.getDisplayLanguage(loc4) + " (" + loc4.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        try {
            loc5 = ArrayList(nameLocaleMap.values)[languageIndex + 4]
            button5.text = loc5.getDisplayLanguage(loc5) + " (" + loc5.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        try {
            loc6 = ArrayList(nameLocaleMap.values)[languageIndex + 5]
            button6.text = loc6.getDisplayLanguage(loc6) + " (" + loc6.displayLanguage + ")"
        } catch (ignored: IndexOutOfBoundsException) {
        }
        next.text = translateToLocal("other.next")
        prev.text = translateToLocal("other.prev")
        cancelButton.text = translateToLocal("other.cancel")
        val gg1 = gg.create(BubbleBlaster.middleX.toInt() - 256, BubbleBlaster.middleY.toInt() - 149, 512, 300)
        val gg2: GraphicsProcessor
        if (animation1 != null) {
            if (animation1!!.isEnded) {
                languageIndex += deltaIndex
                animation1 = null
                animation2 = Animation((512 * deltaPage).toDouble(), 0.0, 0.05)
                animation2!!.start()
                val x = animation2!!.animate().toInt()
                gg2 = gg1.create(x, 0, 512, 300)
            } else {
                val x = animation1!!.animate().toInt()
                gg2 = gg1.create(x, 0, 512, 300)
            }
        } else {
            if (animation2 != null) {
                if (animation2!!.isEnded) {
                    gg2 = gg1.create(0, 0, 512, 300)
                    animation2 = null
                } else {
                    val x = animation2!!.animate().toInt()
                    gg2 = gg1.create(x, 0, 512, 300)
                }
            } else {
                gg2 = gg1.create(0, 0, 512, 300)
            }
        }
        if (loc1 != null) button1.paint(gg2)
        if (loc2 != null) button2.paint(gg2)
        if (loc3 != null) button3.paint(gg2)
        if (loc4 != null) button4.paint(gg2)
        if (loc5 != null) button5.paint(gg2)
        if (loc6 != null) button6.paint(gg2)
        gg2.dispose()
        gg1.dispose()
        next.paint(gg)
        prev.paint(gg)
        cancelButton.paint(gg)
    }

    @Synchronized
    fun renderBackground(gg: GraphicsProcessor?) {
        gg!!.color(Color(96, 96, 96))
        gg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
    }

    @SubscribeEvent
    fun onUpdate() {
    }

    companion object {
        private var INSTANCE: LanguageScreen? = null
        fun instance(): LanguageScreen? {
            return INSTANCE
        }
    }

    init {
        INSTANCE = this
        this.backScene = backScene
        button1.command = { cmdButton1() }
        button2.command = { cmdButton2() }
        button3.command = { cmdButton3() }
        button4.command = { cmdButton4() }
        button5.command = { cmdButton5() }
        button6.command = { cmdButton6() }
        prev.command = { prevPage() }
        next.command = { nextPage() }
        cancelButton.command = { back() }
        loader = LanguageLoader()
        BubbleBlaster.eventBus.register(loader)
    }
}