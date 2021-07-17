package qtech.bubbles

import com.qtech.preloader.PreClassLoader
import lombok.Getter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import qtech.bubbles.common.References
import qtech.bubbles.common.cursor.BlankCursorRenderer
import qtech.bubbles.common.cursor.DefaultCursorRenderer
import qtech.bubbles.common.cursor.HoverCursorRenderer
import qtech.bubbles.common.cursor.TextCursorRenderer
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.core.common.SavedGame.Companion.fromFile
import qtech.bubbles.entity.player.PlayerController
import qtech.bubbles.environment.Environment
import qtech.bubbles.environment.EnvironmentRenderer
import qtech.bubbles.event.GuiTickEvent
import qtech.bubbles.event.TickEvent
import qtech.bubbles.event.bus.BBEventBus
import qtech.bubbles.gui.Window
import qtech.bubbles.init.GameTypes
import qtech.bubbles.media.AudioPlayer
import qtech.bubbles.mods.loader.ModManager
import qtech.bubbles.screen.EnvLoadScreen
import qtech.bubbles.util.Util.sceneManager
import qtech.hydro.Game
import qtech.hydro.GraphicsProcessor
import qtech.hydro.crash.CrashCategory
import qtech.hydro.crash.CrashReport
import qtech.hydro.ui.CursorManager
import qtech.hydro.ui.FontManager
import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.lang.instrument.Instrumentation
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.annotation.ParametersAreNonnullByDefault
import javax.imageio.ImageIO

/**
 * The QBubbles game main class.
 *
 * @since 0.0.1-indev1
 */
@Suppress("unused", "UNUSED_PARAMETER")
@ParametersAreNonnullByDefault
open class BubbleBlaster(args: Array<String>) : Game(gameDir, args) {
    val isFocused: Boolean
        get() = window.isFocused
//    private val pixelOverlay: BufferedImage? = ImageIO.read(javaClass.getResourceAsStream("/assets/bubbleblaster/textures/pixel_overlay.png"))

    // Event-bus.
    var background: BufferedImage? = null
        private set
    var environment: Environment? = null
    val playerController = PlayerController()

    lateinit var instrumentation: Instrumentation
        private set

    // Fonts.
    lateinit var sansFont: Font
        private set
    lateinit var monospaceFont: Font
        private set
    lateinit var pixelFont: Font
        private set
    lateinit var gameFont: Font
        private set

    // Cursors
    lateinit var blankCursor: Cursor
        private set
    final override lateinit var defaultCursor: Cursor
        private set
    lateinit var pointerCursor: Cursor
        private set
    lateinit var textCursor: Cursor
        private set

    // GraphicsProcessor / GUI.
    override val fullscreen: Boolean = !devMode

    // Managers.
    lateinit var modManager: ModManager
        private set

    // Randomizers.
    val random = Random()
    val environmentRenderer = EnvironmentRenderer()
    var loadedGame: LoadedGame? = null
        private set

    // Font-getters.
    val gameFontName: String
        get() = gameFont.fontName
    val pixelFontName: String
        get() = pixelFont.fontName
    val monospaceFontName: String
        get() = monospaceFont.fontName
    val sansFontName: String
        get() = sansFont.fontName

    // Threads
    lateinit var renderThread: Thread
        private set
    lateinit var gcThread: Thread
        private set


    // Constructor.
    init {
//        // Assign instance.
//        instance = this
//
//        val exceptionHandler = BBExceptionHandler()
//        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
//
//        try {
//            screenManager = ScreenManager.instance
//            modManager = ModManager.instance
//            for (thread in Thread.getAllStackTraces().keys) {
//                thread.uncaughtExceptionHandler = exceptionHandler
//                if (thread.name == "JavaFX Application Thread") {
//                    thread.name = "BubbleBlasterApp"
//                }
//                if (thread.name == "AWT-EventQueue-0") {
//                    thread.name = "BB-EventBus"
//                }
//            }
//
//            // Add ansi color compatibility in console.
//            AnsiConsole.systemInstall()
//            setCwd(References.QBUBBLES_DIR)
//
//            // Transparent 16 x 16 pixel cursor image.
//            val cursorImg = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)
//
//            // Create a new blank cursor.
//            blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg, Point(0, 0), "blank cursor")
//
//            // Transparent 16 x 16 pixel cursor image.
//            val cursorImg1 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//            val polygon = Polygon(intArrayOf(0, 10, 5, 0), intArrayOf(0, 12, 12, 16), 4)
//            val gg = GraphicsProcessor(cursorImg1.createGraphics())
//            gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//            gg.color(Color.black).polyF(polygon)
//            gg.color(Color.white).poly(polygon)
//            gg.dispose()
//
//            // Create the default cursor.
//            defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg1, Point(1, 1), "default cursor")
//
//            // Transparent 16 x 16 pixel cursor image.
//            val cursorImg2 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//            val polygon2 = Polygon(intArrayOf(10, 20, 15, 10), intArrayOf(10, 22, 22, 26), 4)
//            val gg2 = GraphicsProcessor(cursorImg2.createGraphics())
//            gg2.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//            gg2.color(Color.white).oval(0, 0, 20, 20)
//            gg2.color(Color.white).oval(2, 2, 16, 16)
//            gg2.color(Color.black).polyF(polygon2)
//            gg2.color(Color.white).poly(polygon2)
//            gg2.color(Color.black).oval(1, 1, 18, 18)
//            gg2.dispose()
//
//            // Create the pointer cursor.
//            pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg2, Point(11, 11), "pointer cursor")
//
//            // Transparent 16 x 16 pixel cursor image.
//            val cursorImg3 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//            val gg3 = GraphicsProcessor(cursorImg3.createGraphics())
//            gg3.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//            gg3.color(Color.white).line(0, 1, 0, 24)
//            gg3.color(Color.white).line(1, 0, 1, 25)
//            gg3.color(Color.white).line(2, 1, 2, 24)
//            gg3.color(Color.black).line(1, 1, 1, 24)
//            gg3.dispose()
//
//            // Create the text cursor.
//            textCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg3, Point(1, 12), "text cursor")
//            Thread.setDefaultUncaughtExceptionHandler(BBExceptionHandler())
//
//            // Hook output for logger.
//            System.setErr(PrintStream(CustomOutputStream("STDERR", Level.ERROR)))
//            System.setOut(PrintStream(CustomOutputStream("STDOUT", Level.INFO)))
//
//            // Logs directory creation.
//            References.LOGS_DIR.mkdirs()
//
//            // Register Game Font.
//            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
//            try {
//                // Create and load game font.
//                gameFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/ChicleRegular-xpv5.ttf")))
//                ge.registerFont(gameFont)
//
//                // Create and load pixel font.
//                pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/pixel/Pixel-UniCode.ttf")))
//                ge.registerFont(pixelFont)
//
//                // Create and load monospace font.
//                monospaceFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/dejavu/DejaVuSansMono.ttf")))
//                ge.registerFont(monospaceFont)
//
//                // Create and load sans font.
//                sansFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/arial-unicode-ms.ttf")))
//                ge.registerFont(sansFont)
//            } catch (e: FontFormatException) {
//                // Check for null pointer exception.
//                if (e is NullPointerException) {
//                    System.err.println("Couldn't load fonts.")
//                }
//                e.printStackTrace()
//            } catch (e: NullPointerException) {
//                System.err.println("Couldn't load fonts.")
//                e.printStackTrace()
//            }
//
//            // Create the graphics configurator.
//            graphicsConfigurator = GraphicsConfigurator()
//            if (!isHeadless()) {
//                // Internal canvas.
//                window = Window(this, !devMode)
//                window.cursor = defaultCursor
//
//                // Request focus
//                requestFocus()
//            }
//
//            // Set font.
//            font = sansFont
//
//            // Register event bus.
//            eventBus.register(this)
//
//            // Start scene-manager.
//            Objects.requireNonNull(this.screenManager).start()
//        } catch (t: Throwable) {
//            // Create crash report instance, and convert to string.
//            val crashReport = CrashReport(t)
//            val crashString = crashReport.toString()
//
//            // Split the crash report into lines.
//            val strings = StringUtils.splitIntoLines(crashString)
//
//            // Print each lien in the stderr output.
//            for (string in strings) {
//                System.err.println(string)
//            }
//
//            // Exit the process with status 1.
//            exitProcess(1)
//        }
    }

    override fun initManagers() {
        modManager = ModManager.instance
    }

    override fun loadCursors(manager: CursorManager) {
        blankCursor = BlankCursorRenderer().create(manager)
        defaultCursor = DefaultCursorRenderer().create(manager)
        pointerCursor = HoverCursorRenderer().create(manager)
        textCursor = TextCursorRenderer().create(manager)

//        // Create a new blank cursor.
//        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//            cursorImg, Point(0, 0), "blank cursor")
//
//        // Transparent 16 x 16 pixel cursor image.
//        val cursorImg1 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//        val polygon = Polygon(intArrayOf(0, 10, 5, 0), intArrayOf(0, 12, 12, 16), 4)
//        val gg = GraphicsProcessor(cursorImg1.createGraphics())
//        gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//        gg.color(Color.black).polyF(polygon)
//        gg.color(Color.white).poly(polygon)
//        gg.dispose()
//
//        // Create the default cursor.
//        manager.create()
//        defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//            cursorImg1, Point(1, 1), "default cursor")
//
//        // Transparent 16 x 16 pixel cursor image.
//        val cursorImg2 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//        val polygon2 = Polygon(intArrayOf(10, 20, 15, 10), intArrayOf(10, 22, 22, 26), 4)
//        val gg2 = GraphicsProcessor(cursorImg2.createGraphics())
//        gg2.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//        gg2.color(Color.white).oval(0, 0, 20, 20)
//        gg2.color(Color.white).oval(2, 2, 16, 16)
//        gg2.color(Color.black).polyF(polygon2)
//        gg2.color(Color.white).poly(polygon2)
//        gg2.color(Color.black).oval(1, 1, 18, 18)
//        gg2.dispose()
//
//        // Create the pointer cursor.
//        pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//            cursorImg2, Point(11, 11), "pointer cursor")
//
//        // Transparent 16 x 16 pixel cursor image.
//        val cursorImg3 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
//        val gg3 = GraphicsProcessor(cursorImg3.createGraphics())
//        gg3.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//        gg3.color(Color.white).line(0, 1, 0, 24)
//        gg3.color(Color.white).line(1, 0, 1, 25)
//        gg3.color(Color.white).line(2, 1, 2, 24)
//        gg3.color(Color.black).line(1, 1, 1, 24)
//        gg3.dispose()
//
//        // Create the text cursor.
//        textCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//            cursorImg3, Point(1, 12), "text cursor")
    }

    override fun loadFonts(manager: FontManager) {
        gameFont = manager.create(BubbleBlaster::class.java, "/assets/bubbleblaster/fonts/ChicleRegular-xpv5.ttf")
        pixelFont = manager.create(BubbleBlaster::class.java, "/assets/bubbleblaster/fonts/pixel/Pixel-UniCode.ttf")
        monospaceFont = manager.create(BubbleBlaster::class.java, "/assets/bubbleblaster/fonts/dejavu/DejaVuSansMono.ttf")
        sansFont = manager.create(BubbleBlaster::class.java, "/assets/bubbleblaster/fonts/arial-unicode-ms.ttf")
    }

    /**
     * Garbage collector thread.
     */
    @Deprecated("Unused", ReplaceWith("System.gc()"), DeprecationLevel.WARNING)
    fun gcThread() {
        // Garbage collecting.
        System.gc()
    }

    /**
     * Starts game-thread.
     *
     * @param window the window that used to start the game.
     */
    override fun start(window: Window) {
        super.start(window)

        this.canvas.isVisible = true
    }

    /**
     * Checks for exit events, and cleanly exit the game.
     */
    override fun cleanExit() {
        super.cleanExit()
        quitLoadedGame()
    }

    /**
     * Update method, for updating values and doing things.
     */
    override fun tick() {
        @Suppress("SENSELESS_COMPARISON")
        if (playerController == null) {
            return
        }
        ticks++
        val currentScreen = sceneManager.currentScreen
        currentScreen?.tick()
        if (environment != null) {
            environment!!.tick()
        }
        playerController.tick()

        // Call tick event.
        if (isGameLoaded && (currentScreen == null || !currentScreen.doesPauseGame())) {
            eventBus.post(TickEvent(this))
        } else {
            eventBus.post(GuiTickEvent(this))
        }
    }

    /**
     * Render method, for rendering window.
     */
    override fun render(gp: GraphicsProcessor) {
        renderScreenEnv(gp)
    }

    override fun postRender(gp: GraphicsProcessor) {
//        gp.img(pixelOverlay, 0, 0)
    }

    private fun renderScreenEnv(gg: GraphicsProcessor) {
        val screen = screenManager.currentScreen
        if (screen != null) {
            // Clear background.
            gg.background(Color.BLACK)
            gg.clear(0, 0, width, height)
            if (environment != null) {
                try {
                    environmentRenderer.render(gg)
                } catch (t: Throwable) {
                    val report = CrashReport(t)
                    report.addCategory(CrashCategory("Environment being rendered").also {
                        it.add("Environment") { environment }
                        it.add("Renderer") { environmentRenderer }
                    })
                    throw report.reportedException
                }
            }
            screen.render(this, gg)
        } else {
            // Clear background
            gg.background(Color(0, 0, 0))
            gg.clear(0, 0, width, height)
            if (environment != null) {
                try {
                    environmentRenderer.render(gg)
                } catch (t: Throwable) {
                    val report = CrashReport(t)
                    report.addCategory(CrashCategory("Environment being rendered").also {
                        it.add("Environment") { environment }
                        it.add("Renderer") { environmentRenderer }
                    })
                    throw report.reportedException
                }
            }
        }
    }

    @Suppress("unused")
    fun loadSave(saveName: String?) {}
    @Suppress("unused")
    fun createAndLoadSave(saveName: String?) {}
    @Suppress("unused")
    val currentSave: SavedGame?
        get() {
            val loadedGame = loadedGame
            return loadedGame?.savedGame
        }

    @Suppress("unused")
    fun loadGame() {
        val loadedGameReference = AtomicReference(loadedGame)
        displayScene(EnvLoadScreen(fromFile(File(References.SAVES_DIR, "save")), { GameTypes.CLASSIC_MODE.get() }, loadedGameReference))
        loadedGame = loadedGameReference.get()
    }

    fun quitLoadedGame() {
        val loadedGame = loadedGame
        loadedGame?.quit()
    }

    val isGameLoaded: Boolean
        get() = loadedGame != null

    companion object {
        lateinit var bbFlags: BBFlags
            private set
        const val TPS = 20

        // Initial game information / types.
        var mainClassLoader: ClassLoader? = null
            private set

        var audioPlayer: AudioPlayer? = null
            private set

        lateinit var gameDir: File
            private set

        var debugMode = false
            private set

        var devMode = false
            private set

        // Number values.
        var isTextureDump = false
            private set
        var headless = false
            private set

        // Logger.
        @Getter
        val logger: Logger = LogManager.getLogger("QB:Generic")
        var ticks = 0L
            private set

        // Graphical getters.
        val middleX: Double
            get() = instance.width.toDouble() / 2
        val middleY: Double
            get() = instance.height.toDouble() / 2
        val middlePoint: Point2D
            get() = Point2D.Double(middleX, middleY)

        fun reduceTicks2Secs(value: Byte, seconds: Byte): Byte {
            return (value.toDouble() / (TPS.toDouble() * seconds)).toInt().toByte()
        }

        fun reduceTicks2Secs(value: Short, seconds: Short): Short {
            return (value.toDouble() / (TPS.toDouble() * seconds)).toInt().toShort()
        }

        fun reduceTicks2Secs(value: Int, seconds: Int): Int {
            return (value.toDouble() / (TPS.toDouble() * seconds)).toInt()
        }

        fun reduceTicks2Secs(value: Long, seconds: Long): Long {
            return (value.toDouble() / (TPS.toDouble() * seconds)).toLong()
        }

        fun reduceTicks2Secs(value: Float, seconds: Float): Float {
            return (value.toDouble() / (TPS.toDouble() * seconds)).toFloat()
        }

        fun reduceTicks2Secs(value: Double, seconds: Double): Double {
            return value / (TPS.toDouble() * seconds)
        }

        val isPaused: Boolean
            get() {
                val currentScreen: Screen? = instance.currentScreen
                return currentScreen != null && currentScreen.doesPauseGame()
            }

        fun isHeadless(): Boolean {
            return isTextureDump
        }

        // Instance property.
        val instance: BubbleBlaster
            get() = Game.instance as BubbleBlaster

        /**
         * Get event bus.
         *
         * @return The qbubbles event bus.
         */
        val eventBus: BBEventBus
            get() {
                return instance.eventBus
            }

        /**
         * Launch method
         */
        @JvmStatic
        fun main(args: Array<String>, mainClassLoader: PreClassLoader?) {
            // Get game-directory.
            for (arg in args) {
                if (arg.startsWith("gameDir=")) {
                    gameDir = File(arg.substring(8))
                }
                if (arg == "--debug") {
                    debugMode = true
                }
                if (arg == "--dev") {
                    devMode = true
                }
                if (arg == "--textureDump") {
                    isTextureDump = true
                }
            }

            val envVars = System.getenv()
            if (envVars.containsKey("FLAGS")) {
                val flagsCode = envVars["FLAGS"]
                bbFlags = (if (flagsCode != null) BBFlags.parse(flagsCode) else BBFlags())
            }

            // Check if game-dir is assigned, if not the game-dir is not specified in the arguments.
//            @Suppress("SENSELESS_COMPARISON")
//            if (gameDir == null) {
//                System.err.println("Game Directory is not specified!")
//                exitProcess(1)
//            }
            Companion.mainClassLoader = mainClassLoader

            // Boot the game.
            try {
                BubbleBlaster(args)
            } catch (e: IOException) {
                e.printStackTrace()
                //            System.exit(1);
            }
        }
    }
}