package com.qtech.bubbles

import com.qtech.bubbles.addon.loader.AddonManager
import com.qtech.bubbles.common.References
import com.qtech.bubbles.common.Timer
import com.qtech.bubbles.common.Timer.time
import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.common.crash.ReportedException
import com.qtech.bubbles.common.scene.ScreenManager
import com.qtech.bubbles.common.screen.Screen
import com.qtech.bubbles.common.streams.CustomOutputStream
import com.qtech.bubbles.core.common.SavedGame
import com.qtech.bubbles.core.common.SavedGame.Companion.fromFile
import com.qtech.bubbles.core.utils.categories.FileUtils.setCwd
import com.qtech.bubbles.core.utils.categories.StringUtils
import com.qtech.bubbles.entity.player.PlayerController
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.environment.EnvironmentRenderer
import com.qtech.bubbles.event.ExitEvent
import com.qtech.bubbles.event.FilterEvent
import com.qtech.bubbles.event.GuiTickEvent
import com.qtech.bubbles.event.TickEvent
import com.qtech.bubbles.event.bus.BBEventBus
import com.qtech.bubbles.graphics.GraphicsEngine
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.graphics.NullGraphics
import com.qtech.bubbles.gui.Window
import com.qtech.bubbles.init.GameTypes
import com.qtech.bubbles.media.AudioPlayer
import com.qtech.bubbles.screen.CrashScreen
import com.qtech.bubbles.screen.EnvLoadScreen
import com.qtech.bubbles.settings.GameSettings
import com.qtech.bubbles.util.Util.sceneManager
import com.qtech.preloader.PreClassLoader
import lombok.Getter
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.fusesource.jansi.AnsiConsole
import org.jdesktop.swingx.JXFrame
import org.jdesktop.swingx.graphics.FilterComposite
import java.awt.*
import java.awt.geom.Point2D
import java.awt.image.BufferStrategy
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.PrintStream
import java.lang.instrument.Instrumentation
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.roundToLong
import kotlin.system.exitProcess

/**
 * The QBubbles game main class.
 *
 * @since 0.0.1-indev1
 */
@Suppress("unused", "UNUSED_PARAMETER")
@ParametersAreNonnullByDefault
class BubbleBlaster : Canvas() {
    // Event-bus.
    val eventBus = BBEventBus()
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
    val blankCursor: Cursor
    val defaultCursor: Cursor
    val pointerCursor: Cursor
    val textCursor: Cursor

    // Font names.
    val fontName: String

    // GraphicsProcessor / GUI.
    var graphicsEngine: GraphicsEngine

    @Getter
    lateinit var window: Window
        private set

    // Managers.
    val addonManager: AddonManager
    val screenManager: ScreenManager

    // Running value.
    @Getter
    @Volatile
    private var running = false

    // Randomizers.
    val random = Random()
    val environmentRenderer = EnvironmentRenderer()
    val tasks: MutableList<Runnable> = CopyOnWriteArrayList()
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

    // GUI getters.
    val frame: JXFrame
        get() = window.frame

    // Threads
    lateinit var renderThread: Thread
        private set
    lateinit var mainThread: Thread
        private set
    lateinit var gcThread: Thread
        private set


    // Constructor.
    init {
        // Assign instance.
        instance = this

        Thread.setDefaultUncaughtExceptionHandler(BBExceptionHandler())

        try {
            screenManager = ScreenManager.instance
            addonManager = AddonManager.instance
            for (thread in Thread.getAllStackTraces().keys) {
                if (thread.name == "JavaFX Application Thread") {
                    thread.name = "BubbleBlasterApp"
                }
                if (thread.name == "AWT-EventQueue-0") {
                    thread.name = "BB-EventBus"
                }
            }

            // Add ansi color compatibility in console.
            AnsiConsole.systemInstall()
            setCwd(References.QBUBBLES_DIR)

            // Transparent 16 x 16 pixel cursor image.
            val cursorImg = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)

            // Create a new blank cursor.
            blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, Point(0, 0), "blank cursor")

            // Transparent 16 x 16 pixel cursor image.
            val cursorImg1 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
            val polygon = Polygon(intArrayOf(0, 10, 5, 0), intArrayOf(0, 12, 12, 16), 4)
            val gg = GraphicsProcessor(cursorImg1.createGraphics())
            gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            gg.color(Color.black)
            gg.polyF(polygon)
            gg.color(Color.white)
            gg.poly(polygon)
            gg.dispose()

            // Create a new blank cursor.
            defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg1, Point(1, 1), "default cursor")

            // Transparent 16 x 16 pixel cursor image.
            val cursorImg2 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
            val polygon2 = Polygon(intArrayOf(10, 20, 15, 10), intArrayOf(10, 22, 22, 26), 4)
            val gg2 = GraphicsProcessor(cursorImg2.createGraphics())
            gg2.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            gg2.color(Color.white)
            gg2.oval(0, 0, 20, 20)
            gg2.color(Color.white)
            gg2.oval(2, 2, 16, 16)
            gg2.color(Color.black)
            gg2.polyF(polygon2)
            gg2.color(Color.white)
            gg2.poly(polygon2)
            gg2.color(Color.black)
            gg2.oval(1, 1, 18, 18)
            gg2.dispose()

            // Create a new blank cursor.
            pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg2, Point(11, 11), "pointer cursor")

            // Transparent 16 x 16 pixel cursor image.
            val cursorImg3 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
            val gg3 = GraphicsProcessor(cursorImg3.createGraphics())
            gg3.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            gg3.color(Color.white)
            gg3.line(0, 1, 0, 24)
            gg3.color(Color.white)
            gg3.line(1, 0, 1, 25)
            gg3.color(Color.white)
            gg3.line(2, 1, 2, 24)
            gg3.color(Color.black)
            gg3.line(1, 1, 1, 24)
            gg3.dispose()

            // Create a new blank cursor.
            textCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg3, Point(1, 12), "text cursor")
            Thread.setDefaultUncaughtExceptionHandler(BBExceptionHandler())

            // Hook output for logger.
            System.setErr(PrintStream(CustomOutputStream("STDERR", Level.ERROR)))
            System.setOut(PrintStream(CustomOutputStream("STDOUT", Level.INFO)))

            // Logs directory creation.
            References.LOGS_DIR.mkdirs()

            // Font Name
            fontName = "Chicle Regular"

            // Register Game Font.
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            try {
                gameFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/ChicleRegular-xpv5.ttf")))
                ge.registerFont(gameFont)
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/pixel/Pixel-UniCode.ttf")))
                ge.registerFont(pixelFont)
                monospaceFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/dejavu/DejaVuSansMono.ttf")))
                ge.registerFont(monospaceFont)
                sansFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster::class.java.getResourceAsStream("/assets/bubbleblaster/fonts/arial-unicode-ms.ttf")))
                ge.registerFont(sansFont)
            } catch (e: FontFormatException) {
                if (e is NullPointerException) {
                    System.err.println("Couldn't load fonts.")
                }
                e.printStackTrace()
            } catch (e: NullPointerException) {
                System.err.println("Couldn't load fonts.")
                e.printStackTrace()
            }

    //        System.out.println(getFontMetrics(new Font(sansFont.getFontName(), Font.PLAIN, 12)).stringWidth("\u00A0\u00A0\u00A0\u00A0"));
    //        System.exit(0);
            graphicsEngine = GraphicsEngine()
            if (!isHeadless()) {
                // Internal canvas.
                window = Window(this, !devMode)
                window.cursor = defaultCursor

                // Request focus
                requestFocus()
            }

            // Set font.
            font = sansFont

            // Register event bus.
            eventBus.register(this)

            // Start scene-manager.
            Objects.requireNonNull(this.screenManager).start()
        } catch (t: Throwable) {
            val crashReport = CrashReport("Oops, game crashed!", t)
            val crashString = crashReport.toString()
            val strings = StringUtils.splitIntoLines(crashString)
            for (string in strings) {
                System.err.println(string)
            }

            exitProcess(1)
        }
    }

    fun gcThread() {
        System.gc()
    }

    /**
     * Starts game-thread.
     */
    fun start(window: Window) {
        running = true
        this.isVisible = true
        if (bounds.minX > bounds.maxX || bounds.minY > bounds.maxY) {
            bounds = window.bounds
        }
        if (bounds.minX == bounds.maxX || bounds.minY == bounds.maxY) {
            bounds = window.bounds
        }
        check(!(bounds.minX > bounds.maxX || bounds.minY > bounds.maxY)) { "Game bounds is invalid: negative size" }
        check(!(bounds.minX == bounds.maxX || bounds.minY == bounds.maxY)) { "Game bounds is invalid: zero size" }
        logger.debug(this.isVisible)

//        //noinspection Convert2Lambda,Anonymous2MethodRef,Anonymous2MethodRef
//        renderThread = new Thread(new Runnable() {public void run() {
//            QBubbles.this.renderThread();}}, "RenderThread");
//        getRenderThread().start();
        mainThread = Thread({ mainThread() }, "TickThread")
        mainThread.start()
    }

    /**
     * Stops game-thread.
     */
    @Synchronized
    fun stop() {
        try {
            renderThread.join()
            mainThread.join()
            running = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shutdown() {
        running = false
    }

    fun close() {
        // Shut-down game.
        logger.warn("Bubble Blaster 2 is now shutting down...")

        // Check for exit events.
        checkForExitEvents()

        // Stop thread, when fails: interrupt thread, when that also fails show error.
        try {
            // Interrupt thread.
            if (mainThread.isAlive && Thread.currentThread() !== mainThread) {
                mainThread.interrupt()
                logger.info("Interrupted main thread.")
            }
        } catch (ignore: SecurityException) {
            logger.error("Cannot stop and interrupt game-thread because of security exceptions.")
        }
        exitProcess(0)
    }

    private fun checkForExitEvents() {
        eventBus.post(ExitEvent())
        quitLoadedGame()
    }

    private fun mainThread() {
        val tickCap = 1.0 / TPS.toDouble()
        var frameTime = 0.0
        var frames = 0.0
        var time = time
        var unprocessed = 0.0
        try {
            while (running) {
                var canTick = false
                val time2 = Timer.time
                val passed = time2 - time
                unprocessed += passed
                frameTime += passed
                time = time2
                while (unprocessed >= tickCap) {
                    unprocessed -= tickCap
                    canTick = true
                }
                if (canTick) {
                    try {
                        tick()
                    } catch (t: Throwable) {
                        val crashReport = CrashReport("Game being ticked.", t)
                        throw crashReport.reportedException
                    }
                }
                if (frameTime >= 1.0) {
                    frameTime = 0.0
                    fps = frames.roundToLong().toInt()
                    frames = 0.0
                }
                frames++
                try {
                    render()
                } catch (t: Throwable) {
                    val crashReport = CrashReport("Game being rendered.", t)
                    throw crashReport.reportedException
                }
                for (task in tasks) {
                    task.run()
                }
                tasks.clear()
            }
        } catch (e: ReportedException) {
            val crashReport = CrashReport("Oops, game crashed!", e)
            val crashString = crashReport.toString()
            val strings = StringUtils.splitIntoLines(crashString)
            for (string in strings) {
                System.err.println(string)
            }
            displayScene(CrashScreen(e.crashReport), true)
            while (running) Thread.onSpinWait()
        }
        close()
    }

    /**
     * Update method, for updating values and doing things.
     */
    private fun tick() {
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
    private fun render() {
        val g: Graphics
        var bs: BufferStrategy? = null
        if (isHeadless()) {
            g = NullGraphics()
        } else {
            // Buffer strategy (triple buffering).
            bs = bufferStrategy

            // Create buffers if not created yet.
            if (bs == null) {
                this.createBufferStrategy(2)
                return
            }

            // Get GraphicsProcessor and GraphicsProcessor objects.
            g = bs.drawGraphics
        }
        val gg = GraphicsProcessor(g)
        if (graphicsEngine.isAntialiasingEnabled && GameSettings.instance().isTextAntialiasEnabled) gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        if (graphicsEngine.isAntialiasingEnabled && GameSettings.instance().isAntialiasEnabled) gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        renderScreenEnv(gg)
        if (screenManager.currentScreen != null) screenManager.currentScreen!!.renderGUI(this, gg)

        // Disable Antialias
        if (graphicsEngine.isAntialiasingEnabled && GameSettings.instance().isTextAntialiasEnabled) gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        if (graphicsEngine.isAntialiasingEnabled && GameSettings.instance().isAntialiasEnabled) gg.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)

        val filterEvent = FilterEvent()
        eventBus.post(filterEvent)
        for (filter in filterEvent.filters) {
            gg.composite(FilterComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f), filter))
        }

        // Dispose and show.
        g.dispose()
        if (!isHeadless()) {
            bs!!.show()
        }
    }

    private fun renderScreenEnv(gg: GraphicsProcessor) {
        val screen = screenManager.currentScreen
        if (screen != null) {
            // Clear background.
            gg.background(Color.BLACK)
            gg.clear(0, 0, width, height)
            if (environment != null) {
                environmentRenderer.render(gg)
            }
            screen.render(this, gg)
        } else {
            // Clear background
            gg.background(Color(0, 0, 0))
            gg.clear(0, 0, width, height)
            if (environment != null) {
                environmentRenderer.render(gg)
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

    fun displayScene(scene: Screen?) {
        screenManager.displayScreen(scene)
    }

    fun displayScene(scene: Screen?, force: Boolean) {
        screenManager.displayScreen(scene, force)
    }

    val currentScreen: Screen?
        get() = screenManager.currentScreen
    @Suppress("unused")
    val isOnMainThread: Boolean
        get() = Thread.currentThread() === mainThread
    @Suppress("unused")
    val isOnRenderThread: Boolean
        get() = Thread.currentThread() === renderThread

    @Suppress("unused")
    fun runLater(runnable: Runnable) {
        tasks.add(runnable)
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
        var fps = 0
            private set
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
        lateinit var instance: BubbleBlaster
            private set

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
            @Suppress("SENSELESS_COMPARISON")
            if (gameDir == null) {
                System.err.println("Game Directory is not specified!")
                exitProcess(1)
            }
            Companion.mainClassLoader = mainClassLoader

            // Boot the game.
            try {
                BubbleBlaster()
            } catch (e: IOException) {
                e.printStackTrace()
                //            System.exit(1);
            }
        }
    }
}