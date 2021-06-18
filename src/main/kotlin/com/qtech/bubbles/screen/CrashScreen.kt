package com.qtech.bubbles.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.gui.CrashButton
import java.awt.Color
import java.awt.Font
import java.awt.Rectangle
import java.io.IOException

class CrashScreen(crashReport: CrashReport) : Screen() {
    private val report: CrashReport = crashReport
    private val crashButton: CrashButton = addWidget(CrashButton(game.width / 2 - 64, 60, 128, 24))
    override fun init() {
        try {
            report.writeToFile()
        } catch (e: IOException) {
            try {
                report.writeToFile()
            } catch (ioException: IOException) {
                try {
                    report.writeToFile()
                } catch (exception: IOException) {
                    BubbleBlaster.logger.fatal("Couldn't create crash report!")
                }
            }
        }
    }

    override fun onClose(to: Screen?): Boolean {
        return false
    }

    override fun render(game: BubbleBlaster, gp: GraphicsProcessor) {
        gp.color(Color(192, 0, 0))
        gp.rect(0, 0, game.width, game.height)
        drawCenteredString(gp, "The game crashed!", Rectangle(20, 20, game.width - 40, 30), Font(game.sansFontName, Font.BOLD, 24))
        crashButton.setX(game.width / 2 - crashButton.bounds.width / 2)
    }

    override fun renderGUI(game: BubbleBlaster, gg: GraphicsProcessor) {}

    init {
        crashButton.text = "Open crash report \uD83D\uDCCE"
    }
}