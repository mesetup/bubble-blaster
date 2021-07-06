@file:Suppress("unused")

package qtech.bubbles.screen

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.LoadedGame
import qtech.bubbles.common.InfoTransporter
import qtech.hydro.crash.CrashReport
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.gametype.AbstractGameMode.Companion.loadState
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.bubbles.environment.Environment
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.settings.GameSettings
import org.apache.commons.io.FileUtils
import qtech.hydro.Game
import qtech.hydro.crash.CrashCategory
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier

/**
 * Environment loading scene.
 * When showing this scene, a new thread will be created and in that thread the loading will be done.
 * The thread is located in the method [.init].
 *
 *
 * Todo: update docstring.
 *
 * @author Quinten Jungblut
 * @since 1.0.615
 */
class EnvLoadScreen private constructor(private val create: Boolean, private val savedGame: SavedGame, gameMode: Supplier<AbstractGameMode>?, loadedGameReference: AtomicReference<LoadedGame?>) : Screen() {
    private val gameMode: Supplier<AbstractGameMode>?
    private val loadedGameReference: AtomicReference<LoadedGame?>
    private val infoTransporter = InfoTransporter { description: String -> setDescription(description) }
    private var description = ""

    constructor(savedGame: SavedGame, loadedGameReference: AtomicReference<LoadedGame?>) : this(false, savedGame, null, loadedGameReference)
    constructor(savedGame: SavedGame, gameMode: Supplier<AbstractGameMode>, loadedGameReference: AtomicReference<LoadedGame?>) : this(true, savedGame, gameMode, loadedGameReference)

    private fun setDescription(description: String) {
        this.description = description
    }

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
            val loadedGame = LoadedGame(savedGame, game.environment!!)
            loadedGame.run()
            loadedGameReference.set(loadedGame)
        } catch (t: Throwable) {
            val crashReport = CrashReport(t)
            crashReport.addCategory(CrashCategory("Environment being loaded").also {
                it.add("Save Directory") { savedGame.directory }
            })
            throw crashReport.reportedException
        }
        game.displayScene(null)
    }

    /**
     * Renders the environment loading scene.<br></br>
     * Shows the title in the blue accent color (#00b0ff), and the description in a 50% black color (#7f7f7f).
     * @param game the QBubbles game.
     * @param gp   the graphics 2D processor.
     */
    override fun render(game: Game, gp: GraphicsProcessor) {
        gp.color(Color(64, 64, 64))
        gp.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        if (GameSettings.instance().isTextAntialiasEnabled) gp.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        gp.color(Color(0, 192, 255))
        drawCenteredString(gp, "Loading Environment...", Rectangle2D.Double(0.0, BubbleBlaster.instance.height.toDouble() / 2 - 24, BubbleBlaster.instance.width.toDouble(), 64.0), Font("Helvetica", Font.PLAIN, 48))
        gp.color(Color(127, 127, 127))
        drawCenteredString(gp, description, Rectangle2D.Double(0.0, BubbleBlaster.instance.height.toDouble() / 2 + 40, BubbleBlaster.instance.width.toDouble(), 50.0), Font("Helvetica", Font.PLAIN, 20))
        if (GameSettings.instance().isTextAntialiasEnabled) gp.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    }

    init {
        this.gameMode = gameMode
        this.loadedGameReference = loadedGameReference
    }
}