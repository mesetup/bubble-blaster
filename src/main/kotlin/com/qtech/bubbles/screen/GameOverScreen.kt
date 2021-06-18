package com.qtech.bubbles.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.data.GlobalSaveData
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Color
import java.awt.Font
import java.awt.LinearGradientPaint
import java.awt.Rectangle

class GameOverScreen(private var score: Long) : Screen() {
    val isHighScore: Boolean
    private val titleFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD, 64)
    private val descriptionFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD, 14)
    private val scoreFont = Font(BubbleBlaster.instance.sansFontName, Font.BOLD, 32)
    override fun init() {
        bindEvents()
    }

    override fun onClose(to: Screen?): Boolean {
        unbindEvents()
        return super.onClose(to)
    }

    override fun render(game: BubbleBlaster, gp: GraphicsProcessor) {
        val gradient = LinearGradientPaint(0F, 0F, 0F, game.height.toFloat(), floatArrayOf(0.0f, 0.4f), arrayOf(Color(0, 0, 224), Color(0, 0, 128)))
        val old = gp.paint()
        gp.paint(gradient)
            .rectF(0, 0, game.width, game.height)
            .paint(old)
    }

    override fun renderGUI(game: BubbleBlaster, gg: GraphicsProcessor) {
        gg.color(Color.white)
        if (isHighScore) {
            drawCenteredString(gg, "Congratulations!", Rectangle(0, 120, game.width, 64), titleFont)
            drawCenteredString(gg, "You beat your high-score!", Rectangle(0, 184, game.width, 64), descriptionFont)
        }
        drawCenteredString(gg, score.toString(), Rectangle(0, 248, game.width, 64), scoreFont)
    }

    init {
        val globalSaveData: GlobalSaveData = GlobalSaveData.instance()
        if (!globalSaveData.isLoaded) {
            if (!globalSaveData.isCreated) {
                globalSaveData.create()
            }
            globalSaveData.load()
        }
        isHighScore = globalSaveData.highScore < score
        if (isHighScore) {
            globalSaveData.setHighScore(score.toDouble(), System.currentTimeMillis())
            globalSaveData.dump()
        }
    }
}