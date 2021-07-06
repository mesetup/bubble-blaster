package qtech.hydro

import org.apache.commons.lang3.SystemUtils
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.fusesource.jansi.AnsiConsole
import org.jdesktop.swingx.graphics.FilterComposite
import qtech.bubbles.BubbleBlaster
import qtech.bubbles.common.References
import qtech.bubbles.common.Timer
import qtech.bubbles.common.renderer.CursorRenderer
import qtech.hydro.crash.CrashExceptionHandler
import qtech.hydro.crash.CrashReport
import qtech.bubbles.common.scene.ScreenManager
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.common.streams.CustomOutputStream
import qtech.bubbles.core.controllers.KeyboardController
import qtech.bubbles.core.controllers.MouseController
import qtech.bubbles.core.utils.categories.FileUtils
import qtech.bubbles.core.utils.categories.StringUtils
import qtech.bubbles.event.ExitEvent
import qtech.bubbles.event.FilterEvent
import qtech.bubbles.event.bus.BBEventBus
import qtech.bubbles.gui.Window
import qtech.bubbles.screen.CrashScreen
import qtech.bubbles.settings.GameSettings
import qtech.hydro.crash.CrashCategory
import qtech.hydro.crash.ReportedException
import qtech.hydro.ui.CursorManager
import qtech.hydro.ui.FontManager
import java.awt.*
import java.awt.image.BufferStrategy
import java.awt.image.ImageObserver
import java.io.File
import java.io.PrintStream
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToLong
import kotlin.system.exitProcess

abstract class Game(gameDir: File, val args: Array<String>) {
    var cursor: Cursor
        get() = window.cursor
        set(value) {
            window.cursor = value
        }
    val graphicsConfiguration: GraphicsConfiguration by lazy { canvas.graphicsConfiguration }
    val bounds: Rectangle
        get() = canvas.bounds
    var workDir: File = gameDir
        private set
    abstract val fullscreen: Boolean
    protected lateinit var window: Window

    lateinit var mainThread: Thread
        private set

    private val tasks: MutableList<Runnable> = CopyOnWriteArrayList()

    val eventBus = BBEventBus()
    var graphicsConfigurator: GraphicsConfigurator
        protected set
    @Volatile
    var running: Boolean = false
        protected set
    var screenManager: ScreenManager
        protected set

    open val defaultFont: Font
        get() = canvas.font
    open val defaultCursor: Cursor
        get() = Cursor.getDefaultCursor()

    protected val canvas: Canvas = Canvas()


    val currentScreen: Screen?
        get() = screenManager.currentScreen
    val isOnMainThread: Boolean
        get() = Thread.currentThread() === mainThread
    val fontName: String
        get() = canvas.font.fontName
    val font: Font
        get() = canvas.font
    var width: Int
        get() = canvas.width
        set(value) {
            window.setSize(value, window.height)
            canvas.setSize(value, window.height)
        }
    var height: Int
        get() = canvas.height
        set(value) {
            window.setSize(window.width, value)
            canvas.setSize(window.width, value)
        }

    val imageObserver: ImageObserver = ProtectedImageObserver(canvas)

    init {
        instance = this

        workDir = gameDir

        requireNotNull(workDir)

        val exceptionHandler = CrashExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)

        try {
            screenManager = ScreenManager.instance
            initManagers()
            for (thread in Thread.getAllStackTraces().keys) {
                thread.uncaughtExceptionHandler = exceptionHandler
                if (thread.name == "JavaFX Application Thread") {
                    thread.name = "BubbleBlasterApp"
                }
                if (thread.name == "AWT-EventQueue-0") {
                    thread.name = "BB-EventBus"
                }
            }

            // Add ansi color compatibility in console.
            AnsiConsole.systemInstall()

            FileUtils.cwd = workDir

            val cursorManager = CursorManager()
            try {
                loadCursors(cursorManager)
            } catch (t: Throwable) {
                val report = CrashReport(t)
                val currentContext = cursorManager.currentContext
                if (currentContext != null) {
                    report.addCategory(CrashCategory("Cursor being loaded").also {
                        it.add("Name") { currentContext.name }
                        it.add("Image Class") { currentContext.image::class.qualifiedName }
                    })
                }
                val createContext = CursorRenderer.createContext
                if (createContext != null) {
                    report.addCategory(CrashCategory("Cursor being created").also {
                        it.add("Name") { createContext.name }
                    })
                }
                throw report.reportedException
            }

            Thread.setDefaultUncaughtExceptionHandler(CrashExceptionHandler())

            // Hook output for logger.
            System.setErr(PrintStream(CustomOutputStream("STDERR", Level.ERROR)))
            System.setOut(PrintStream(CustomOutputStream("STDOUT", Level.INFO)))

            // Logs directory creation.
            References.LOGS_DIR.mkdirs()

            try{
                val fontManager = FontManager()
                loadFonts(fontManager)
            } catch (e: FontFormatException) {
                // Check for null pointer exception.
                if (e is NullPointerException) {
                    System.err.println("Couldn't load fonts.")
                }
                e.printStackTrace()
            } catch (e: NullPointerException) {
                System.err.println("Couldn't load fonts.")
                e.printStackTrace()
            }

            // Create the graphics configurator.
            graphicsConfigurator = GraphicsConfigurator()
            if (!BubbleBlaster.isHeadless()) {
                // Internal canvas.
                window = Window(this, fullscreen, canvas)
                window.cursor = defaultCursor

                // Request focus

                screenManager.onStart { window.isVisible = true }
                canvas.requestFocus()
            }

            // Set font.
            canvas.font = defaultFont

            // Register event bus.
            eventBus.register(this)

            // Start scene-manager.
            Objects.requireNonNull(this.screenManager).start()
        } catch (t: Throwable) {
            // Create crash report instance, and convert to string.
            val crashReport = CrashReport(t)
            val crashString = crashReport.toString()

            // Split the crash report into lines.
            val strings = StringUtils.splitIntoLines(crashString)

            // Print each lien in the stderr output.
            for (string in strings) {
                System.err.println(string)
            }

            // Exit the process with status 1.
            exitProcess(1)
        }
    }
    abstract fun initManagers()

    abstract fun render(gp: GraphicsProcessor)

    abstract fun tick()

    abstract fun loadCursors(manager: CursorManager)

    abstract fun loadFonts(manager: FontManager)
    fun displayScene(scene: Screen?) {
        screenManager.displayScreen(scene)
    }

    fun displayScene(scene: Screen?, force: Boolean) {
        screenManager.displayScreen(scene, force)
    }

    @Suppress("unused")
    fun runLater(runnable: Runnable) {
        tasks.add(runnable)
    }

    protected fun mainThread() {
        val tickCap = 1.0 / BubbleBlaster.TPS.toDouble()
        var frameTime = 0.0
        var frames = 0.0
        var time = Timer.time
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
                        val crashReport = CrashReport(t).also {
                            it.addCategory(CrashCategory("Game being ticked").also { it1 ->
                                it1.add("Can tick") { canTick }
                                it1.add("Unprocessed") { unprocessed }
                                it1.add("Tick Cap") { tickCap }
                            })
                        }
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
                    val crashReport = CrashReport(t)
                    crashReport.addCategory(CrashCategory("Game being rendered").also{
                        it.add("Frame time") { frameTime }
                        it.add("Frames") { frames }
                        it.add("Fps") { fps }
                    })
                    throw crashReport.reportedException
                }
                for (task in tasks) {
                    task.run()
                }
                tasks.clear()
            }
        } catch (e: ReportedException) {
            if (currentScreen is CrashScreen) {
                val report = CrashReport(e)
                println("Error occurred in displaying crash screen:")
                println(report.toString())
                close(1)
            }

            val crashReport = CrashReport(e)
            val crashString = crashReport.toString()
            println(crashString)
            try {
                displayScene(CrashScreen(e.crashReport), true)
            } catch(t: Throwable) {
                val report = CrashReport(t)
                println("Error occurred in showing crash screen:")
                println(report.toString())
            }

            mainThread()
        }

        // Force shutdown because of crash.
        close(0)
    }

    fun render() {
        val g: Graphics
        var bs: BufferStrategy? = null
        if (BubbleBlaster.isHeadless()) {
            g = NullGraphics()
        } else {
            // Buffer strategy (triple buffering).
            bs = this.canvas.bufferStrategy

            // Create buffers if not created yet.
            if (bs == null) {
                this.canvas.createBufferStrategy(2)
                return
            }

            // Get GraphicsProcessor and GraphicsProcessor objects.
            g = bs.drawGraphics
        }
        val gg = GraphicsProcessor(g)
        if (this.graphicsConfigurator.isAntialiasingEnabled && GameSettings.instance().isTextAntialiasEnabled) gg.hint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        )
        if (graphicsConfigurator.isAntialiasingEnabled && GameSettings.instance().isAntialiasEnabled) gg.hint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        render(gg)

        currentScreen?.render(this, gg)
        if (this.screenManager.currentScreen != null) screenManager.currentScreen!!.renderGUI(this, gg)

        // Disable Antialias
        if (this.graphicsConfigurator.isAntialiasingEnabled && GameSettings.instance().isTextAntialiasEnabled) gg.hint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        )
        if (this.graphicsConfigurator.isAntialiasingEnabled && GameSettings.instance().isAntialiasEnabled) gg.hint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF
        )

        val filterEvent = FilterEvent()
        eventBus.post(filterEvent)
        for (filter in filterEvent.filters) {
            gg.composite(FilterComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f), filter))
        }

        postRender(GraphicsProcessor(g))

        // Dispose and show.
        g.dispose()
        if (!BubbleBlaster.isHeadless()) {
            bs!!.show()
        }

        // Add key listener.
        logger.info("Loading input event listeners / threads...")
        canvas.addKeyListener(KeyboardController.instance())
        canvas.addMouseListener(MouseController.instance())
        canvas.addMouseMotionListener(MouseController.instance())
        canvas.addMouseWheelListener(MouseController.instance())
    }

    open fun postRender(gp: GraphicsProcessor) {

    }

    open fun start(window: Window) {
        if (canvas.bounds.minX > canvas.bounds.maxX || canvas.bounds.minY > canvas.bounds.maxY) {
            canvas.bounds = window.bounds
        }
        if (canvas.bounds.minX == canvas.bounds.maxX || canvas.bounds.minY == canvas.bounds.maxY) {
            canvas.bounds = window.bounds
        }
        check(!(canvas.bounds.minX > canvas.bounds.maxX || canvas.bounds.minY > canvas.bounds.maxY)) { "Game bounds is invalid: negative size" }
        check(!(canvas.bounds.minX == canvas.bounds.maxX || canvas.bounds.minY == canvas.bounds.maxY)) { "Game bounds is invalid: zero size" }

        running = true

        logger.debug(this.canvas.isVisible)

        mainThread = Thread({ mainThread() }, "TickThread")
        mainThread.start()
    }

    fun configureWindow(window: Window) {
        window.cursor = defaultCursor
    }

    /**
     * Stops the game.
     */
    @Synchronized
    fun stop() {
        try {
            mainThread.join()
            running = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Lazy shutdown.
     */
    fun shutdown() {
        running = false
    }

    /**
     * Force shutdown the game.
     */
    fun close(code: Int) {
        try {
            // Shut-down game.
            BubbleBlaster.logger.warn("Bubble Blaster 2 is now shutting down...")

            // Check for exit events.
            cleanExit()

            // Stop thread, when fails: interrupt thread, when that also fails show error.
            try {
                // Interrupt thread.
                if (mainThread.isAlive && Thread.currentThread() !== mainThread) {
                    mainThread.interrupt()
                    BubbleBlaster.logger.info("Interrupted main thread.")
                }
            } catch (ignore: SecurityException) {
                BubbleBlaster.logger.error("Cannot stop and interrupt game-thread because of security exceptions.")
            }
        } catch (t: Throwable) {
            try {
                BubbleBlaster.logger.fatal("Exception trying to close Bubble Blaster", t)
            } catch (t: Throwable) {
                exitProcess(1)
            }
            exitProcess(1)
        }
        exitProcess(code)
    }

    /**
     * Force shutdown the game.
     */
    fun close() {
        try {
            // Shut-down game.
            BubbleBlaster.logger.warn("Bubble Blaster 2 is now shutting down...")

            // Check for exit events.
            cleanExit()

            // Stop thread, when fails: interrupt thread, when that also fails show error.
            try {
                // Interrupt thread.
                if (mainThread.isAlive && Thread.currentThread() !== mainThread) {
                    mainThread.interrupt()
                    BubbleBlaster.logger.info("Interrupted main thread.")
                }
            } catch (ignore: SecurityException) {
                BubbleBlaster.logger.error("Cannot stop and interrupt game-thread because of security exceptions.")
            }
        } catch (t: Throwable) {
            try {
                BubbleBlaster.logger.fatal("Exception trying to close Bubble Blaster", t)
            } catch (t: Throwable) {
            }
        }
    }

    open fun cleanExit() {
        eventBus.post(ExitEvent())
    }

    fun requestWindowUserAttention() {
        if (SystemUtils.IS_JAVA_9) {
            // Let the taskbar icon flash. (Java 9+)
            val taskbar = Taskbar.getTaskbar()
            try {
                taskbar.requestWindowUserAttention(window)
            } catch (ignored: UnsupportedOperationException) {
            }
        }
    }

    companion object {
        val logger: Logger = LogManager.getLogger("PreGame")
        var fps = 0
            private set
        lateinit var instance: Game
            private set
    }
}