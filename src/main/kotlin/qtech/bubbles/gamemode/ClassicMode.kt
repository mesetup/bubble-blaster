@file:Suppress("unused")

package qtech.bubbles.gamemode

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import qtech.bubbles.common.Difficulty
import qtech.bubbles.common.IRegistryEntry
import qtech.bubbles.common.InfoTransporter
import qtech.hydro.crash.CrashReport
import qtech.bubbles.entity.Entity
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.entity.types.EntityType
import qtech.bubbles.environment.Environment
import qtech.bubbles.event.FilterEvent
import qtech.bubbles.event.SubscribeEvent
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.init.Bubbles
import qtech.bubbles.init.Entities
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.Registry
import qtech.bubbles.settings.GameSettings
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag
import net.querz.nbt.tag.StringTag
import org.apache.logging.log4j.LogManager
import org.bson.BsonInvalidOperationException
import qtech.hydro.crash.CrashCategory
import java.awt.Point
import java.awt.geom.Rectangle2D
import java.io.IOException
import java.util.*
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.roundToInt

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
open class ClassicMode : AbstractGameMode() {
    // Hud and events-active flag.
    final override var hud: ClassicModeHud? = ClassicModeHud(this)
        private set
    var isEventActive = false
        private set

    // Threads
    var spawner: Thread? = null
        private set

    override fun init(environment: Environment, infoTransporter: InfoTransporter) {
        this.environment = environment
        try {
            // Init defaults.
            initDefaults()
            hud = ClassicModeHud(this)

            // Spawn bubbles
            infoTransporter.log("Spawning bubbles...")
            ticks = -1
            for (i in 0 until maxBubbles) {
                var bubble = randomBubble
                while (!bubble.canSpawn(this)) {
                    ticks--
                    bubble = randomBubble
                }
                if (bubble !== Bubbles.LEVEL_UP_BUBBLE.get()) {
                    val pos = Point(bubblesXPosRng!!.getNumber(0, BubbleBlaster.instance.width, -i - 1), bubblesYPosRng!!.getNumber(0, BubbleBlaster.instance.width, -i - 1))
                    environment.spawn(Entities.BUBBLE.get().create(this), pos)
                }
                ticks--
                infoTransporter.log("Spawning bubble $i/$maxBubbles")
            }
            ticks = 0

            // Spawn player
            infoTransporter.log("Spawning player...")
            player = PlayerEntity(environment.gameMode)
            environment.spawn(player, Point(game.width / 4, BubbleBlaster.instance.height / 2))
        } catch (e: Exception) {
            val crashReport = CrashReport(e)
            crashReport.addCategory(CrashCategory("Could not initialize classic mode").also {
                it.add("HUD") { hud!!::class.qualifiedName }
                it.add("Player") { player!!::class.qualifiedName }
                it.add("Ticks") { ticks }
            })
            throw crashReport.reportedException
        }

        // Bind events.
        bindEvents()
        maxBubbles = GameSettings.instance().maxBubbles
        spawner = Thread({ spawnerThread() }, "SpawnerThread")
        this.isInitialized = true
    }

    override fun load(environment: Environment, infoTransporter: InfoTransporter) {
        this.environment = environment
        hud = ClassicModeHud(this)
        this.isInitialized = true
    }

    /**
     * <h1>Load Game Type.</h1>
     * Used for initializing the game.
     */
    override fun start() {
        spawner!!.start()
    }

    fun spawnerThread() {
//        while (eventActive) {
//            if (this.environment.getEntitiesByClass(BubbleEntity.class).size() < maxBubbles) {
//                AbstractBubble bubble = getRandomBubble();
//                if (bubble.canSpawn(this)) {
//                    environment.spawnEntity(EntityInit.BUBBLE.get());
//                }
//            }
//        }
    }

    @SubscribeEvent
    fun onFilter(evt: FilterEvent?) {
//        evt.addFilter(filter);
    }

    /**
     * Creates save data.
     *
     * @param savedGame       the saved game to write the initial data to,
     * @param infoTransporter an info transporter to the save-loading screen.
     * @see AbstractGameMode.loadSaveData
     * @see AbstractGameMode.dumpSaveData
     */
    override fun createSaveData(savedGame: SavedGame, infoTransporter: InfoTransporter) {
        try {
            infoTransporter.log("Dumping registries in save...")
            savedGame.createFolders("Registries")
            dumpRegistryData(savedGame, "Registries/ItemTypes", Registers.ITEMS)
            dumpRegistryData(savedGame, "Registries/AmmoTypes", Registers.AMMO_TYPES)
            dumpRegistryData(savedGame, "Registries/GameStates", Registers.GAME_EVENTS)
            dumpRegistryData(savedGame, "Registries/EntityTypes", Registers.ENTITIES)
            dumpRegistryData(savedGame, "Registries/BubbleTypes", Registers.BUBBLES)
            dumpRegistryData(savedGame, "Registries/AbilityTypes", Registers.ABILITIES)
            dumpRegistryData(savedGame, "Registries/Effects", Registers.EFFECTS)
            dumpRegistryData(savedGame, "Registries/Cursors", Registers.CURSORS)
            infoTransporter.log("Dumping default data...")
            savedGame.dumpData("Entities", CompoundTag())
            savedGame.dumpData("GameStates", CompoundTag())
        } catch (t: Throwable) {
            throw RuntimeException(t)
        }
    }

    private fun <T : IRegistryEntry?> dumpRegistryData(savedGame: SavedGame?, relPath: String, registry: Registry<T>) {
        val document = CompoundTag()
        val entries =
            ListTag(StringTag::class.java)
        for (type in registry.values()) {
            entries.addString(type!!.registryName.toString())
        }
        document.put("Entries", entries)
        try {
            savedGame!!.dumpData(relPath, document)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Loads save data.
     *
     * @see AbstractGameMode.createSaveData
     * @see AbstractGameMode.dumpSaveData
     */
    override fun loadSaveData(savedGame: SavedGame, infoTransporter: InfoTransporter) {}

    /**
     * Creates save data.
     *
     * @param savedGame The saved game to write the save data to.
     * @see AbstractGameMode.createSaveData
     * @see AbstractGameMode.loadSaveData
     */
    override fun dumpSaveData(savedGame: SavedGame?) {
        try {
            val entities = CompoundTag()
            val entityArray = ListTag(CompoundTag::class.java)
            for (entity in environment!!.entities) {
                if (entity.isSpawned) {
                    entityArray.add(entity.getState())
                }
            }
            entities.put("Values", entityArray)
            savedGame!!.dumpData("Entities", entities)
            savedGame.dumpData("GameStates", CompoundTag())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun render(gg: GraphicsProcessor?) {}
    override fun getState(): CompoundTag {
        val tag = super.getState()
        tag.putString("Difficulty", difficulty.name)
        return tag
    }

    override fun setState(state: CompoundTag) {
        super.setState(state)
        difficulty = Difficulty.valueOf(state.getString("difficulty"))
    }

    override fun repair(savedGame: SavedGame?): Boolean {
        return try {
            val document = savedGame!!.loadInfoData()
            try {
                val dataVersion = document.getInt32("GameTypeVersion").value
                if (dataVersion != 0) {
                    false
                } else false
            } catch (e: BsonInvalidOperationException) {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun convert(savedGame: SavedGame?): Boolean {
        return try {
            val document = savedGame!!.loadInfoData()
            val dataVersion = document.getInt32("GameTypeVersion").value
            dataVersion == 0
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override val gameTypeVersion: Int
        get() = 0

    override fun renderHUD(gg: GraphicsProcessor?) {
        hud?.renderHUD(gg)
    }

    override fun renderGUI(gg: GraphicsProcessor?) {}
    override val gameBounds: Rectangle2D
        get() = Rectangle2D.Double(0.0, 70.0, BubbleBlaster.instance.width.toDouble(), BubbleBlaster.instance.height - 70.0)

    override var player: PlayerEntity? = null
        protected set

    /**
     * <h1>Trigger Game Over</h1>
     * Deletes player and set game over flag in ClassicHUD. Showing Game Over message.
     *
     * @see ClassicModeHud.setGameOver
     */
    override fun triggerGameOver() {
        if (!isGameOver) {
            resultScore = Objects.requireNonNull(player)!!.score.roundToInt().toLong()
        }
        environment!!.gameOver(player!!)
        player!!.delete()
        hud?.setGameOver()
        isGameOver = true
    }

    override fun getSpawnLocation(entity: Entity?): Point {
        return Point(
            gameBounds.maxX.toInt() + entity!!.bounds!!.width,
            bubblesYPosRng!!.getNumber(gameBounds.minY - entity.bounds!!.height, gameBounds.maxY + entity.bounds!!.height, ticks).toInt()
        )
    }

    override fun quit() {
        for (entity in environment!!.entities) {
            entity.delete()
        }
        hud = null
    }

    override fun bindEvents() {
//        tickEventCode = QUpdateEvent.getInstance().addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        renderEventCode = QRenderEvent.getInstance().addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);
        BubbleBlaster.eventBus.register(this)
        isEventActive = true
    }

    @Throws(NoSuchElementException::class)
    override fun unbindEvents() {
//        QUpdateEvent.getInstance().removeListener(tickEventCode);
//        QRenderEvent.getInstance().removeListener(renderEventCode);
        BubbleBlaster.eventBus.unregister(this)
        isEventActive = false
    }

    /**
     * @return True if the events are active, false otherwise.
     */
    override fun eventsAreBound(): Boolean {
        return isEventActive
    }

    override fun tick() {
        super.tick()
        if (environment!!.entities.stream().filter { entity: Entity? -> entity is BubbleEntity }.count() < GameSettings.instance().maxBubbles) {
            val entityType: EntityType<out BubbleEntity> = BubbleEntity.getRandomType(this)
            val bubbleEntity = entityType.create(this)
            if (bubbleEntity.bubbleType.canSpawn(this)) {
                environment!!.spawn(entityType)
            }
        }
    }

    override fun getEntityId(entity: Entity?): Long {
        return ticks
    }

    companion object {
        private val logger = LogManager.getLogger("QB:ClassicType")
        private var maxBubbles = GameSettings.instance().maxBubbles
        fun getMaxBubbles(): Int {
            return maxBubbles
        }

        fun setMaxBubbles(maxBubbles: Int) {
            Companion.maxBubbles = maxBubbles
        }
    }

    init {
        overrideXCoords()
    }
}