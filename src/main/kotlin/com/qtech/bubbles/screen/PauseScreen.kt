package com.qtech.bubbles.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.bubble.BubbleSystem.defaultTotalPriority
import com.qtech.bubbles.common.bubble.BubbleSystem.getDefaultPercentageChance
import com.qtech.bubbles.common.bubble.BubbleSystem.getDefaultPriority
import com.qtech.bubbles.common.bubble.BubbleSystem.getPercentageChance
import com.qtech.bubbles.common.bubble.BubbleSystem.getPriority
import com.qtech.bubbles.common.bubble.BubbleSystem.totalPriority
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.common.text.translation.I18n.translateToLocal
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.environment.EnvironmentRenderer
import com.qtech.bubbles.event.KeyboardEvent
import com.qtech.bubbles.event.LanguageChangeEvent
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.type.KeyEventType
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.gui.PauseButton
import com.qtech.bubbles.media.AudioSlot
import com.qtech.bubbles.registry.Registry
import com.qtech.bubbles.util.Util
import com.qtech.bubbles.util.helpers.MathHelper
import org.jdesktop.swingx.graphics.FilterComposite
import org.jdesktop.swingx.image.GaussianBlurFilter
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.geom.Rectangle2D
import java.math.BigDecimal
import java.net.URISyntaxException
import java.util.*

class PauseScreen : Screen() {
    private val exitButton: PauseButton = PauseButton.Builder().bounds((BubbleBlaster.middleX - 128).toInt(), 200, 256, 48).text("Exit and Quit Game").command { BubbleBlaster.instance.shutdown() }.build()
    private val prevButton: PauseButton
    private val nextButton: PauseButton
    private var minRadius = translateToLocal("screen.bubbleblaster.pause.min_radius")
    private var maxRadius = translateToLocal("screen.bubbleblaster.pause.max_radius")
    private var minSpeed = translateToLocal("screen.bubbleblaster.pause.min_speed")
    private var maxSpeed = translateToLocal("screen.bubbleblaster.pause.max_speed")
    private var defChance = translateToLocal("screen.bubbleblaster.pause.default_chance")
    private var curChance = translateToLocal("screen.bubbleblaster.pause.current_chance")
    private var defPriority = translateToLocal("screen.bubbleblaster.pause.default_priority")
    private var curPriority = translateToLocal("screen.bubbleblaster.pause.current_priority")
    private var defTotPriority = translateToLocal("screen.bubbleblaster.pause.default_total_priority")
    private var curTotPriority = translateToLocal("screen.bubbleblaster.pause.current_total_priority")
    private var scoreMod = translateToLocal("screen.bubbleblaster.pause.score_modifier")
    private var attackMod = translateToLocal("screen.bubbleblaster.pause.attack_modifier")
    private var defenseMod = translateToLocal("screen.bubbleblaster.pause.defense_modifier")
    private var canSpawn = translateToLocal("screen.bubbleblaster.pause.can_spawn")
    private var description = translateToLocal("screen.bubbleblaster.pause.description")
    private var random = translateToLocal("other.random")

    //    private final Font bubbleTitleFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 32);
    //    private final Font bubbleValueFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD + Font.ITALIC, 16);
    //    private final Font bubbleInfoFont = new Font(QBubbles.getInstance().getSansFontName(), Font.BOLD, 16);
    private val bubbleTitleFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD, 32)
    private val bubbleValueFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD + Font.ITALIC, 16)
    private val bubbleInfoFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD, 16)
    private val fallbackTitleFont = Font(BubbleBlaster.instance.font.fontName, Font.BOLD, 32)
    private val fallbackValueFont = Font(BubbleBlaster.instance.font.fontName, Font.BOLD + Font.ITALIC, 16)
    private val fallbackInfoFont = Font(BubbleBlaster.instance.font.fontName, Font.BOLD, 16)
    private val pauseFont = Font(BubbleBlaster.instance.gameFontName, Font.PLAIN, 75)
    private val differentBubbles: Int
    private var bubble: AbstractBubble? = null
    private fun previousPage() {
        if (helpIndex > 0) {
            try {
                val focusChangeSFX = AudioSlot(Objects.requireNonNull(javaClass.getResource("/assets/bubbleblaster/audio/sfx/ui/button/focus_change.wav")), "focusChange")
                focusChangeSFX.volume = 0.1
                focusChangeSFX.play()
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
        helpIndex = MathHelper.clamp(helpIndex - 1, 0, differentBubbles - 1)
        tickPage()
    }

    private fun nextPage() {
        if (helpIndex < differentBubbles - 1) {
            try {
                val focusChangeSFX = AudioSlot(javaClass.getResource("/assets/bubbleblaster/audio/sfx/ui/button/focus_change.wav")!!, "focusChange")
                focusChangeSFX.volume = 0.1
                focusChangeSFX.play()
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
        helpIndex = MathHelper.clamp(helpIndex + 1, 0, differentBubbles - 1)
        tickPage()
    }

    private fun tickPage() {
        bubble = ArrayList<Any?>(Registry.getRegistry(AbstractBubble::class.java).values())[helpIndex] as AbstractBubble?
        if (helpIndex >= differentBubbles - 1 && nextButton.eventsAreBound()) {
            nextButton.unbindEvents()
        } else if (!nextButton.eventsAreBound()) {
            nextButton.bindEvents()
        }
        if (helpIndex <= 0 && prevButton.eventsAreBound()) {
            prevButton.unbindEvents()
        } else if (!prevButton.eventsAreBound()) {
            prevButton.bindEvents()
        }
    }

    @SubscribeEvent
    private fun onLanguageChange(evt: LanguageChangeEvent) {
        minRadius = translateToLocal("screen.bubbleblaster.pause.min_radius")
        maxRadius = translateToLocal("screen.bubbleblaster.pause.max_radius")
        minSpeed = translateToLocal("screen.bubbleblaster.pause.min_speed")
        maxSpeed = translateToLocal("screen.bubbleblaster.pause.max_speed")
        defChance = translateToLocal("screen.bubbleblaster.pause.default_chance")
        curChance = translateToLocal("screen.bubbleblaster.pause.current_chance")
        defPriority = translateToLocal("screen.bubbleblaster.pause.default_priority")
        curPriority = translateToLocal("screen.bubbleblaster.pause.current_priority")
        defTotPriority = translateToLocal("screen.bubbleblaster.pause.default_total_priority")
        curTotPriority = translateToLocal("screen.bubbleblaster.pause.current_total_priority")
        scoreMod = translateToLocal("screen.bubbleblaster.pause.score_modifier")
        attackMod = translateToLocal("screen.bubbleblaster.pause.attack_modifier")
        defenseMod = translateToLocal("screen.bubbleblaster.pause.defense_modifier")
        canSpawn = translateToLocal("screen.bubbleblaster.pause.can_spawn")
        description = translateToLocal("screen.bubbleblaster.pause.description")
        random = translateToLocal("other.random")
    }

    override fun init() {
        exitButton.bindEvents()
        prevButton.bindEvents()
        nextButton.bindEvents()
        BubbleBlaster.eventBus.register(this)
        if (!BubbleBlaster.instance.isGameLoaded) {
            return
        }
        Util.setCursor(BubbleBlaster.instance.defaultCursor)
    }

    override fun onClose(to: Screen?): Boolean {
        exitButton.unbindEvents()
        prevButton.unbindEvents()
        nextButton.unbindEvents()
        BubbleBlaster.eventBus.unregister(this)
        Util.setCursor(BubbleBlaster.instance.blankCursor)
        return super.onClose(to)
    }

    @SubscribeEvent
    @Synchronized
    fun onKeyboard(evt: KeyboardEvent) {
        if (evt.type === KeyEventType.PRESS && evt.parentEvent.keyCode == KeyEvent.VK_ESCAPE) {
            if (!BubbleBlaster.instance.isGameLoaded) {
                return
            }
            BubbleBlaster.instance.displayScene(null)
        }
    }

    override fun render(game: BubbleBlaster, gp: GraphicsProcessor) {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
//        val filterComposite = FilterComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f), GaussianBlurFilter(10))
//        gp.composite(filterComposite)
        val ngg = GraphicsProcessor(gp)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Darkened background     //
        /////////////////////////////////
        ngg.color(Color(0, 0, 0, 192))
        ngg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        val oldFont = ngg.font()

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Pause text     //
        ////////////////////////
        ngg.color(Color(255, 255, 255, 128))
        ngg.font(pauseFont)
        drawCenteredString(ngg, translateToLocal("screen.bubbleblaster.pause.text")!!, Rectangle2D.Double(0.0, 90.0, BubbleBlaster.instance.width.toDouble(), ngg.fontMetrics(pauseFont).height
            .toDouble()), pauseFont)
        ngg.font(oldFont)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Exit button     //
        /////////////////////////
        exitButton.text = translateToLocal("screen.bubbleblaster.pause.exit")
        exitButton.paint(ngg)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Navigation Buttons & border     //
        /////////////////////////////////////////

        // Navigation buttons.
        nextButton.text = translateToLocal("other.next")
        prevButton.text = translateToLocal("other.prev")
        if (helpIndex > 0) prevButton.paint(ngg)
        if (helpIndex < differentBubbles - 1) nextButton.paint(ngg)

        // Border
        ngg.color(Color(255, 255, 255, 128))
        ngg.rect((BubbleBlaster.middleX - 480).toInt(), 300, 960, 300)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Bubble     //
        ////////////////////

        // Bubble name.
        ngg.color(Color(255, 255, 255, 192))
        ngg.font(bubbleTitleFont)
        ngg.fallbackFont(fallbackTitleFont)
        ngg.textI(translateToLocal("bubble." + bubble!!.registryName!!.namespace + "." + bubble!!.registryName!!.path.replace("/".toRegex(), ".") + ".name"), BubbleBlaster.middleX.toInt() - 470, 332)

        // Bubble icon.
        EnvironmentRenderer.drawBubble(ngg, (BubbleBlaster.middleX - 470).toInt(), 350, 122, *bubble!!.colors!!)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Info Names     //
        ////////////////////////

        // Set color & font.
        ngg.font(bubbleInfoFont)
        ngg.fallbackFont(fallbackInfoFont)
        ngg.color(Color(255, 255, 255, 192))

        // Left data.
        ngg.textI(minRadius, (BubbleBlaster.middleX - 326).toInt() + 10, 362)
        ngg.textI(maxRadius, (BubbleBlaster.middleX - 326).toInt() + 10, 382)
        ngg.textI(minSpeed, (BubbleBlaster.middleX - 326).toInt() + 10, 402)
        ngg.textI(maxSpeed, (BubbleBlaster.middleX - 326).toInt() + 10, 422)
        ngg.textI(defChance, (BubbleBlaster.middleX - 326).toInt() + 10, 442)
        ngg.textI(curChance, (BubbleBlaster.middleX - 326).toInt() + 10, 462)

        // Right data.
        ngg.textI(defTotPriority, (BubbleBlaster.middleX + 72).toInt() + 10, 322)
        ngg.textI(curTotPriority, (BubbleBlaster.middleX + 72).toInt() + 10, 342)
        ngg.textI(defPriority, (BubbleBlaster.middleX + 72).toInt() + 10, 362)
        ngg.textI(curPriority, (BubbleBlaster.middleX + 72).toInt() + 10, 382)
        ngg.textI(scoreMod, (BubbleBlaster.middleX + 72).toInt() + 10, 402)
        ngg.textI(attackMod, (BubbleBlaster.middleX + 72).toInt() + 10, 422)
        ngg.textI(defenseMod, (BubbleBlaster.middleX + 72).toInt() + 10, 442)
        ngg.textI(canSpawn, (BubbleBlaster.middleX + 72).toInt() + 10, 462)

        // Description
        ngg.textI(description, BubbleBlaster.middleX.toInt() - 470, 502)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //     Values     //
        ////////////////////

        // Set color & font.
        ngg.font(bubbleValueFont)
        ngg.fallbackFont(fallbackValueFont)
        ngg.color(Color(255, 255, 255, 128))

        // Left data.
        ngg.textI(bubble!!.minRadius.toString(), (BubbleBlaster.middleX - 326).toInt() + 200, 362)
        ngg.textI(bubble!!.maxRadius.toString(), (BubbleBlaster.middleX - 326).toInt() + 200, 382)
        ngg.textI(bubble!!.minSpeed.toString(), (BubbleBlaster.middleX - 326).toInt() + 200, 402)
        ngg.textI(bubble!!.maxSpeed.toString(), (BubbleBlaster.middleX - 326).toInt() + 200, 422)
        ngg.textI(MathHelper.round(100.toDouble() * getDefaultPercentageChance(bubble), 5).toString() + "%", (BubbleBlaster.middleX - 326).toInt() + 200, 442)
        ngg.textI(MathHelper.round(100.toDouble() * getPercentageChance(bubble), 5).toString() + "%", (BubbleBlaster.middleX - 326).toInt() + 200, 462)
        ngg.textI(BigDecimal.valueOf(defaultTotalPriority).toBigInteger().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 322)
        ngg.textI(BigDecimal.valueOf(totalPriority).toBigInteger().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 342)
        ngg.textI(BigDecimal.valueOf(getDefaultPriority(bubble)).toBigInteger().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 362)
        ngg.textI(BigDecimal.valueOf(getPriority(bubble)).toBigInteger().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 382)

        // Right data
        if (bubble!!.isScoreRandom) {
            ngg.textI(translateToLocal("other.random"), (BubbleBlaster.middleX + 72).toInt() + 200, 402)
        } else {
            ngg.textI(bubble!!.score.toDouble().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 402)
        }
        if (bubble!!.isAttackRandom) {
            ngg.textI(translateToLocal("other.random"), (BubbleBlaster.middleX + 72).toInt() + 200, 422)
        } else {
            ngg.textI(bubble!!.attack.toDouble().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 422)
        }
        if (bubble!!.isDefenseRandom) {
            ngg.textI(translateToLocal("other.random"), (BubbleBlaster.middleX + 72).toInt() + 200, 442)
        } else {
            ngg.textI(bubble!!.defense.toDouble().toString(), (BubbleBlaster.middleX + 72).toInt() + 200, 442)
        }
        ngg.textI(if (bubble!!.canSpawn(loadedGame.gameMode)) translateToLocal("other.true") else translateToLocal("other.false"), (BubbleBlaster.middleX + 72).toInt() + 200, 462)

        // Description
        ngg.wrappedText(translateToLocal("bubble." + bubble!!.registryName!!.namespace + "." + bubble!!.registryName!!.path.replace("/".toRegex(), ".") + ".description")!!
            .replace("\\\\n".toRegex(), "\n"), BubbleBlaster.middleX.toInt() - 470, 522, 940)
        ngg.font(oldFont)
    }

    @Suppress("unused")
    private fun compress(totalPriority: Double): String {
        if (totalPriority >= 0.0 && totalPriority < 1000.0) {
            return totalPriority.toString()
        }
        if (totalPriority >= 1000.0 && totalPriority < 1000000.0) {
            return MathHelper.round(totalPriority / 1000.0, 1).toString() + "K"
        }
        if (totalPriority >= 1000000.0 && totalPriority < 1000000000.0) {
            return MathHelper.round(totalPriority / 1000000.0, 1).toString() + "M"
        }
        if (totalPriority >= 1000000000.0 && totalPriority < 1000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000.0, 1).toString() + "B"
        }
        if (totalPriority >= 1000000000000.0 && totalPriority < 1000000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000000.0, 1).toString() + "T"
        }
        if (totalPriority >= 1000000000000000.0 && totalPriority < 1000000000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000000000.0, 1).toString() + "QD"
        }
        if (totalPriority >= 1000000000000000000.0 && totalPriority < 1000000000000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000000000000.0, 1).toString() + "QT"
        }
        if (totalPriority >= 1000000000000000000000.0 && totalPriority < 1000000000000000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000000000000000.0, 1).toString() + "S"
        }
        if (totalPriority >= 1000000000000000000000000.0 && totalPriority < 1000000000000000000000000000.0) {
            return MathHelper.round(totalPriority / 1000000000000000000000000.0, 1).toString() + "SX"
        }
        return if (totalPriority >= 1000000000000000000000000000.0 && totalPriority < 1000000000000000000000000000000.0) {
            MathHelper.round(totalPriority / 1000000000000000000000000000.0, 1).toString() + "C"
        } else totalPriority.toString()
    }

    override fun doesPauseGame(): Boolean {
        return true
    }

    companion object {
        private var helpIndex = 0
    }

    init {
        prevButton = PauseButton.Builder().bounds((BubbleBlaster.middleX - 480).toInt(), 250, 96, 48).text("Prev").command { previousPage() }.build()
        nextButton = PauseButton.Builder().bounds((BubbleBlaster.middleX + 480 - 95).toInt(), 250, 96, 48).text("Next").command { nextPage() }.build()
        differentBubbles = Registry.getRegistry(AbstractBubble::class.java).values().size
        tickPage()
    }
}