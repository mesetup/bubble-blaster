@file:Suppress("unused")

package com.qtech.bubbles.screen

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.InfoTransporter
import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.gametype.AbstractGameMode.Companion.loadState
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.core.common.SavedGame
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.settings.GameSettings
import org.apache.commons.io.FileUtils
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Supplier
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Deprecated("Use EnvLoadScreen")
class SaveLoadingScreen private constructor(
    private val create: Boolean,
    private val savedGame: SavedGame, gameMode: Supplier<AbstractGameMode>?
) : Screen() {
    private val infoTransporter = InfoTransporter { s: String -> setDescription(s) }
    private val gameMode: Supplier<AbstractGameMode>?
    private var description = ""
    private fun setDescription(s: String) {
        description = s
    }

    constructor(savedGame: SavedGame) : this(false, savedGame, null)
    constructor(savedGame: SavedGame, gameMode: Supplier<AbstractGameMode>) : this(true, savedGame, gameMode)

    override fun init() {
        val gameMode: AbstractGameMode
        try {
            val directory: File = savedGame.directory
            if (create && directory.exists()) {
                FileUtils.deleteDirectory(directory)
            }
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw IOException("Creating save folder failed.")
                }
            }
            if (create) {
                setDescription("Creating save data...")
                gameMode = Objects.requireNonNull(this.gameMode)!!.get()
                gameMode.dumpDefaultState(savedGame, infoTransporter)
                gameMode.createSaveData(savedGame, infoTransporter)
                setDescription("Loading data...")
            } else {
                setDescription("Loading data...")
                gameMode = loadState(savedGame, infoTransporter)
            }
            game.environment = Environment(gameMode)
            val environment = game.environment!!
            if (create) {
                gameMode.init(environment, infoTransporter)
                gameMode.dumpSaveData(savedGame)
            } else {
                gameMode.load(environment, infoTransporter)
            }
        } catch (t: Throwable) {
            val crashReport = CrashReport("Save being loaded", t)
            crashReport.add("Save Directory", savedGame.directory)
            throw crashReport.reportedException
        }
        game.displayScene(null)
    }

    override fun render(game: BubbleBlaster, gp: GraphicsProcessor) {
        gp.color(Color(64, 64, 64))
        gp.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        if (GameSettings.instance().isTextAntialiasEnabled) gp.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        gp.color(Color(127, 127, 127))
        drawCenteredString(gp, description, Rectangle2D.Double(0.0, BubbleBlaster.instance.height.toDouble() / 2 + 40, BubbleBlaster.instance.width.toDouble(), 50.0), Font("Helvetica", Font.PLAIN, 20))
        if (GameSettings.instance().isTextAntialiasEnabled) gp.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    }

    override fun renderGUI(game: BubbleBlaster, gg: GraphicsProcessor) {}

    init {
        this.gameMode = gameMode
    }
}