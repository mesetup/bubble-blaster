package qtech.bubbles.screen

import lombok.SneakyThrows
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import qtech.bubbles.BubbleBlaster
import qtech.bubbles.command.*
import qtech.bubbles.common.InfoTransporter
import qtech.bubbles.common.LoggableProgress
import qtech.bubbles.common.bubble.BubbleSystem
import qtech.bubbles.common.command.CommandConstructor
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import qtech.bubbles.data.GlobalSaveData
import qtech.bubbles.event.BBLoadCompleteEvent
import qtech.bubbles.event.Bus.addonEventBus
import qtech.bubbles.event.Bus.qBubblesEventBus
import qtech.bubbles.event.TextureRenderEvent
import qtech.bubbles.event.XInputEventThread
import qtech.bubbles.event.registry.RegistryEvent
import qtech.bubbles.event.registry.RegistryEvent.Dump
import qtech.bubbles.mods.loader.ModLoader
import qtech.bubbles.registry.Registers
import qtech.bubbles.util.Util
import qtech.hydro.Game
import qtech.hydro.GraphicsProcessor
import qtech.hydro.TextureCollection
import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.GradientPaint
import java.awt.geom.Rectangle2D
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CopyOnWriteArrayList

class LoadScreen : Screen(), Runnable {
    private val messages: MutableList<Pair<String, Long>> = CopyOnWriteArrayList()
    private var modLoader: ModLoader? = null
    private var mainProgress: LoggableProgress? = null
    private var subProgress1: LoggableProgress? = null
    private val mainLogger = InfoTransporter { s: String -> logMain(s) }
    private val subLogger1 = InfoTransporter { s: String -> logSub1(s) }
    private var mainMessage: String? = ""
    private var subMessage1: String? = ""
    override val defaultCursor: Cursor
        get() = BubbleBlaster.instance.blankCursor

    override fun init() {
        LOGGER.info("Showing LoadScene")
        BubbleBlaster.eventBus.register(this)
        if (!BubbleBlaster.isHeadless()) {
//            BubbleBlaster.instance.window.isVisible = true
        }
        run()
    }

    override fun tick() {
        messages.removeIf { pair: Pair<String, Long> -> pair.second + 2000 < System.currentTimeMillis() }
    }

    override fun onClose(to: Screen?): Boolean {
        if (!isDone) {
            return false
        }
        BubbleBlaster.eventBus.unregister(this)
        return true
    }

    override fun render(game: Game, gp: GraphicsProcessor) {
        if (game is BubbleBlaster) {
            gp.color(Color(72, 72, 72))
            gp.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)

            if (mainProgress != null) {
                run {
                    val progress: Int = mainProgress!!.progress
                    val max: Int = mainProgress!!.max
                    if (mainMessage != null) {
                        gp.color(Color(128, 128, 128))
                        drawCenteredString(
                            gp,
                            mainMessage!!,
                            Rectangle2D.Double(
                                0.0,
                                BubbleBlaster.instance.height.toDouble() / 2 - 15,
                                BubbleBlaster.instance.width.toDouble(),
                                30.0
                            ),
                            Font(
                                game.sansFontName, Font.PLAIN, 20
                            )
                        )
                    }
                    gp.color(Color(128, 128, 128))
                    gp.rectF(BubbleBlaster.instance.width / 2 - 150, BubbleBlaster.instance.height / 2 + 15, 300, 3)
                    gp.color(Color(0, 192, 255))
                    val p = GradientPaint(
                        0F,
                        BubbleBlaster.instance.width.toFloat() / 2 - 150,
                        Color(0, 192, 255),
                        BubbleBlaster.instance.width.toFloat() / 2 + 150,
                        0f,
                        Color(0, 255, 192)
                    )
                    gp.paint(p)
                    gp.rectF(
                        BubbleBlaster.instance.width / 2 - 150,
                        BubbleBlaster.instance.height / 2 + 15,
                        (300.0 * progress.toDouble() / max.toDouble()).toInt(),
                        3
                    )
                }
                if (subProgress1 != null) {
                    val progress: Int = subProgress1!!.progress
                    val max: Int = subProgress1!!.max
                    if (subMessage1 != null) {
                        gp.color(Color(128, 128, 128))
                        drawCenteredString(
                            gp,
                            subMessage1!!,
                            Rectangle2D.Double(
                                0.0,
                                BubbleBlaster.instance.height.toDouble() / 2 + 60,
                                BubbleBlaster.instance.width.toDouble(),
                                30.0
                            ),
                            Font(
                                game.sansFontName, Font.PLAIN, 20
                            )
                        )
                    }
                    gp.color(Color(128, 128, 128))
                    gp.rectF(BubbleBlaster.instance.width / 2 - 150, BubbleBlaster.instance.height / 2 + 90, 300, 3)
                    gp.color(Color(0, 192, 255))
                    val p = GradientPaint(
                        0F,
                        BubbleBlaster.instance.width.toFloat() / 2 - 150,
                        Color(0, 192, 255),
                        BubbleBlaster.instance.width.toFloat() / 2 + 150,
                        0f,
                        Color(0, 255, 192)
                    )
                    gp.paint(p)
                    gp.rectF(
                        BubbleBlaster.instance.width / 2 - 150,
                        BubbleBlaster.instance.height / 2 + 90,
                        (300.0 * progress.toDouble() / max.toDouble()).toInt(),
                        3
                    )
                }
            }
            gp.color(Color(127, 127, 127))
//        GraphicsUtils.drawCenteredString(gg, this.title, new Rectangle2D.Double(0, (double) QBubbles.getInstance().getHeight() / 2, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.BOLD, 50));
//        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) QBubbles.getInstance().getHeight() / 2) + 40, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
//        if (GameSettings.instance().isTextAntialiasEnabled())
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    override fun renderGUI(game: Game, gg: GraphicsProcessor) {}

    @SneakyThrows
    override fun run() {
        mainProgress = LoggableProgress(mainLogger, 10)

        // Get game directory in Java's File format.
        val gameDir = BubbleBlaster.gameDir

        // Check game directory exists, if not, create it!
        if (!gameDir.exists()) {
            check(gameDir.mkdirs()) { "Game Directory isn't created!" }
        }

        // Initialize registries.
//        ModManager()
        LOGGER.info("Loading addons...")
        mainProgress!!.log("Loading addons...")
        mainProgress!!.increment()
        modLoader = ModLoader(this)
        subProgress1 = null
        LOGGER.info("Constructing addons...")
        mainProgress!!.log("Constructing addons...")
        mainProgress!!.increment()
        modLoader!!.constructMods()
        subProgress1 = null

        // Loading object holders
        mainProgress!!.log("Loading object-holders...")
        mainProgress!!.increment()
//        loadObjectHolders()
        subProgress1 = null
        mainProgress!!.log("Initializing Bubble Blaster")
        mainProgress!!.increment()
        initialize()
        subProgress1 = null
        LOGGER.info("Setup addons...")
        mainProgress!!.log("Setup mods")
        mainProgress!!.increment()
        modLoader!!.addonSetup()
        subProgress1 = null

        // GameScene and ClassicType initialization.
        mainProgress!!.log("Registering...")
        mainProgress!!.increment()
        subProgress1 = LoggableProgress(subLogger1, 9)
        subProgress1!!.log("Effects")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.EFFECTS))
        subProgress1!!.log("Abilities")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.ABILITIES))
        subProgress1!!.log("Ammo Types")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.AMMO_TYPES))
        subProgress1!!.log("Entities")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.ENTITIES))
        subProgress1!!.log("Bubbles")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.BUBBLES))
        subProgress1!!.log("Game States")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.GAME_EVENTS))
        subProgress1!!.log("Game Types")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.GAME_TYPES))
        subProgress1!!.log("Cursors")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.CURSORS))
        subProgress1!!.log("Texture Collections")
        subProgress1!!.increment()
        addonEventBus.post(RegistryEvent.Register(Registers.TEXTURE_COLLECTIONS))
        subProgress1 = null
        mainProgress!!.log("")
        mainProgress!!.increment()
        val values: Collection<TextureCollection> = Registers.TEXTURE_COLLECTIONS.values()
        subProgress1 = LoggableProgress(subLogger1, values.size)
        for (collection in values) {
            qBubblesEventBus.post(TextureRenderEvent(collection))
            subProgress1!!.increment()
        }
        if (BubbleBlaster.isTextureDump) {
            val path2 = Paths.get(".", "dump/textures")
            FileUtils.deleteDirectory(path2.toFile())
            for (collection in values) {
                val path = collection.registryName.toString().replace(":", "-")
                val path1 = Paths.get(".", "dump/textures", path)
                Files.createDirectories(path1)
                collection.dump(path1)
            }
            BubbleBlaster.instance.shutdown()
            return
        }

        // BubbleSystem
        mainProgress!!.log("Initialize bubble system...")
        mainProgress!!.increment()
        BubbleSystem.init()

        // Load complete.
        mainProgress!!.log("Load Complete!")
        mainProgress!!.increment()
        BubbleBlaster.eventBus.post(BBLoadCompleteEvent(modLoader!!))

        // Registry dump.
        mainProgress!!.log("Registry Dump.")
        mainProgress!!.increment()
        BubbleBlaster.eventBus.post(Dump())
        isDone = true
        Util.game.screenManager.displayScreen(TitleScreen())
    }

    @Synchronized
    fun initialize() {
        val main = Util.game

        // Log
        logInfo("Initializing Bubble Blaster 2...")

        // XInput event listener thread.
        XInputEventThread.start()

        // Commands
        logInfo("Initializing Commands...")
        CommandConstructor.add("tp", TeleportCommand())
        CommandConstructor.add("heal", HealCommand())
        CommandConstructor.add("level", LevelCommand())
        CommandConstructor.add("score", ScoreCommand())
        CommandConstructor.add("effect", EffectCommand())
        CommandConstructor.add("spawn", SpawnCommand())
        CommandConstructor.add("shutdown", ShutdownCommand())
        CommandConstructor.add("teleport", TeleportCommand())
        CommandConstructor.add("game-over", GameOverCommand())
        CommandConstructor.add("blood-moon", BloodMoonCommand())

        // Game data.
        GlobalSaveData()
    }

    fun logInfo(description: String) {
        messages.add(Pair(description, System.currentTimeMillis()))
    }

    private fun logSub1(s: String) {
        subMessage1 = s
    }

    private fun logMain(s: String) {
        mainMessage = s
    }

    companion object {
        private val LOGGER = LogManager.getLogger("QB:GameLoader")
        private var instance: LoadScreen? = null
        var isDone = false
            private set

        @JvmStatic
        fun getAddonLoader(): ModLoader? {
            return if (instance == null) {
                null
            } else instance!!.modLoader
        }

        @JvmStatic
        fun get(): LoadScreen? {
            return if (isDone) null else instance
        }
    }

    init {
        instance = this
    }
}