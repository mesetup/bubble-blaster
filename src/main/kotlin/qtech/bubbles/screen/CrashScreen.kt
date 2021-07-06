package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.hydro.crash.CrashReport
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.gui.CrashButton
import qtech.hydro.Game
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

    override fun render(game: Game, gp: GraphicsProcessor) {
        if (game is BubbleBlaster) {
            gp.color(Color(192, 0, 0))
            gp.rect(0, 0, game.width, game.height)
            drawCenteredString(
                gp,
                "The game crashed!",
                Rectangle(20, 20, game.width - 40, 30),
                Font(game.sansFontName, Font.BOLD, 24)
            )
            crashButton.setX(game.width / 2 - crashButton.bounds.width / 2)
        }
    }

    override fun renderGUI(game: Game, gg: GraphicsProcessor) {}

    init {
        crashButton.text = "Open crash report \uD83D\uDCCE"
    }
}