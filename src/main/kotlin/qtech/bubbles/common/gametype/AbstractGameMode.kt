@file:Suppress("unused", "UNUSED_PARAMETER")

package qtech.bubbles.common.gametype

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import qtech.bubbles.bubble.AbstractBubble
import qtech.bubbles.common.Difficulty
import qtech.bubbles.common.InfoTransporter
import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.bubble.BubbleSystem
import qtech.bubbles.common.bubble.BubbleSystem.random
import qtech.bubbles.common.entity.*
import qtech.bubbles.common.gamestate.GameEvent
import qtech.bubbles.common.interfaces.DefaultStateHolder
import qtech.bubbles.common.interfaces.Listener
import qtech.bubbles.common.interfaces.StateHolder
import qtech.bubbles.common.random.BubbleRandomizer
import qtech.bubbles.common.random.PseudoRandom
import qtech.bubbles.common.random.Rng
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.entity.AbstractBubbleEntity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.entity.DamageableEntity
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.environment.Environment
import qtech.hydro.Animation
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.init.Bubbles
import qtech.bubbles.init.GameEvents
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.Registry
import qtech.bubbles.util.CollectionsUtils
import lombok.SneakyThrows
import net.querz.nbt.io.NBTUtil
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.StringTag
import org.bson.BsonDocument
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Point
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.io.IOException
import java.io.OutputStream
import java.math.BigInteger
import java.util.*
import javax.annotation.ParametersAreNonnullByDefault

/**
 * <h1>GameType baseclass</h1>
 * Baseclass for all game-types, such as [ClassicType]
 *
 * @see ClassicType
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
abstract class AbstractGameMode : RegistryEntry(), StateHolder, DefaultStateHolder, Listener {
    var environment: Environment? = null
        protected set
    var seed: BigInteger = BigInteger.valueOf(512)

    //    protected final List<Entity> entities = new CopyOnWriteArrayList<>();
    @Deprecated("")
    protected val bubbles: List<BubbleEntity> = ArrayList()

    // Types.
    @JvmField
    protected val game: BubbleBlaster = BubbleBlaster.instance

    // Flags.
    var isGameOver = false
        protected set
    var isGlobalBubbleFreeze = false
    var isBloodMoonActive = false
        private set
    private var bloodMoonTriggered = false

    // Enums.
    @JvmField
    var difficulty = Difficulty.NORMAL

    // State difficulties.
    private val stateDifficultyModifiers = Collections.synchronizedMap(HashMap<GameEvent, Float>())

    // Animations:
    private var bloodMoonAnimation: Animation? = null
    private var bloodMoonAnimation1: Animation? = null

    // Modifiers
    var globalBubbleSpeedModifier = 1.0
        get() = if (isGlobalBubbleFreeze) 0.0 else field
    private var stateDifficultyModifier = 1f
    private val gameEventActive = HashSet<GameEvent>()
    private val bloodMoonRandom = Rng(PseudoRandom(seed), 256, 0)
    protected var savedGame: SavedGame? = null

    // Checks:
    private var nextBloodMoonCheck: Long = 0

    // Values:
    var resultScore: Long = 0
    var ticks: Long = 0
        protected set
    var isInitialized = false
        protected set
    var scoreMultiplier = 1.0
    var isXCoordOverridden = false
        protected set
    var isYCoordOverridden = false
        protected set

    val seedBytes: ByteArray
        get() = rNG.seed.toByteArray()

    // Random & seeding.
    // Randomizers
    val rNG = PseudoRandom(seed)
    protected var rngIndex = 0
    protected var bubbleTypesRng: Rng? = null
    @JvmField
    protected var bubblesXPosRng: Rng? = null
    @JvmField
    protected var bubblesYPosRng: Rng? = null
    protected var bubblesSpeedRng: Rng? = null
    protected var bubblesRadiusRng: Rng? = null
    protected var bubblesDefenseRng: Rng? = null
    protected var bubblesAttackRng: Rng? = null
    protected var bubblesScoreRng: Rng? = null
    var bubbleRandomizer = BubbleRandomizer(this)
        protected set
    protected val rngTypes = HashMap<ResourceLocation, Rng>()

    // Initial entities:

    /**
     * <h1>Initialize Randomizers.</h1>
     * Initializes the randomizers such as for bubble coords, or radius.
     *
     * @see .addRNG
     */
    protected open fun initDefaults() {
        bubbleTypesRng = addRNG("bubbleblaster:bubbles_system", 0, 0)
        bubblesXPosRng = addRNG("bubbleblaster:bubbles_x", 0, 1)
        bubblesYPosRng = addRNG("bubbleblaster:bubbles_y", 0, 2)
        bubblesSpeedRng = addRNG("bubbleblaster:bubbles_speed", 0, 3)
        bubblesRadiusRng = addRNG("bubbleblaster:bubbles_radius", 0, 4)
        bubblesDefenseRng = addRNG("bubbleblaster:bubbles_defense", 0, 5)
        bubblesAttackRng = addRNG("bubbleblaster:bubbles_attack", 0, 6)
        bubblesScoreRng = addRNG("bubbleblaster:bubbles_score", 0, 7)
    }

    /**
     * <h1>Add Randomizer</h1>
     * Adds a randomizer to the game type.
     *
     * @param key The key (name) to save it to.
     * @return A [QBRandom] object.
     */
    @Suppress("SameParameterValue")
    protected fun addRNG(key: String, index: Int, subIndex: Int): Rng {
        val rand = Rng(rNG, index, subIndex)
        rngTypes[ResourceLocation.fromString(key)] = rand
        return rand
    }

    /**
     * <h1>Load Game Type.</h1>
     * Used for start the game-type.
     */
    abstract fun start()

    /**
     * <h1>Initialize Game Type.</h1>
     * Used for initialize the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    abstract fun init(environment: Environment, infoTransporter: InfoTransporter)

    /**
     * <h1>Load Game Type.</h1>
     * Used for loading the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    abstract fun load(environment: Environment, infoTransporter: InfoTransporter)

    /**
     * <h1>Create Save Data</h1>
     * Used for creating the save data.
     *
     * @param savedGame       the saved game to create the data for.
     * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
     */
    abstract fun createSaveData(savedGame: SavedGame, infoTransporter: InfoTransporter)

    /**
     * <h1>Load Save Data</h1>
     * Used for loading the save data.
     */
    abstract fun loadSaveData(savedGame: SavedGame, infoTransporter: InfoTransporter)

    /**
     * <h1>Dump Save Data</h1>
     * Used for storing the save data.
     *
     * @param savedGame the saved game to write the data to.
     */
    abstract fun dumpSaveData(savedGame: SavedGame?)

    /**
     * <h1>Render Event</h1>
     * Render event, renders objects to the canvas.
     *
     * @see RenderEvent
     */
    abstract fun render(gg: GraphicsProcessor?)

    /**
     * <h1>Dump Default State</h1>
     * Dumps the default state to the given saved game.
     *
     * @see SavedGame
     */
    fun dumpDefaultState(savedGame: SavedGame, infoTransporter: InfoTransporter?) {
        if (!BubbleBlaster.instance.isGameLoaded) {
            return
        }
        try {
            savedGame.dumpData("Gave", getDefaultState())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * <h1>Dump State to Output Stream</h1>
     * Dumps the game-type state to the output stream.
     *
     * @param output the output stream to write the state to.
     * @throws IOException when an I/O error occurred.
     */
    @Throws(IOException::class)
    fun dumpState(output: OutputStream?) {
        val nbt = getState()
        BubbleBlaster.instance.currentSave!!
        NBTUtil.write(nbt, output, true)
    }

    @SneakyThrows
    fun dumpState(savedGame: SavedGame) {
        savedGame.dumpData("Configuration", getState())
    }

    /**
     * Repair a saved game.
     *
     * @param savedGame the saved game to repair.
     * @return if repair is successful.
     */
    abstract fun repair(savedGame: SavedGame?): Boolean

    /**
     * Convert a saved game.
     *
     * @param savedGame the saved game to convert.
     * @return if conversion is successful.
     */
    abstract fun convert(savedGame: SavedGame?): Boolean

    /**
     * Get game-type build version.
     *
     * @return the game-type version.
     */
    abstract val gameTypeVersion: Int

    /**
     * Check for missing entries in the registry to load the saved game.
     *
     * @param savedGame the saved game to check for.
     * @return a hashmap container as key the registry, and as value an list of missing resource locations of the registry.
     * @throws IOException when an I/O error occurred.
     */
    @Throws(IOException::class)
    fun checkRegistry(savedGame: SavedGame): HashMap<Registry<*>, MutableList<ResourceLocation>> {
        val missing = HashMap<Registry<*>, MutableList<ResourceLocation>>()
        val nbt = savedGame.loadData("registry")

        // Bubble registry.
        val bubbles = nbt.getListTag("Bubbles")
        for (value in bubbles) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.BUBBLES.contains(type)) {
                    missing[Registers.BUBBLES]!!.add(type)
                }
            }
        }

        // Ammo type registry.
        val ammoTypes = nbt.getListTag("AmmoTypes")
        for (value in ammoTypes) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.AMMO_TYPES.contains(type)) {
                    missing[Registers.AMMO_TYPES]!!.add(type)
                }
            }
        }

        // Effect registry.
        val effects = nbt.getListTag("Effects")
        for (value in effects) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.EFFECTS.contains(type)) {
                    missing[Registers.EFFECTS]!!.add(type)
                }
            }
        }

        // Entity registry.
        val entities = nbt.getListTag("Entities")
        for (value in entities) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.ENTITIES.contains(type)) {
                    missing[Registers.ENTITIES]!!.add(type)
                }
            }
        }

        // Game state registry.
        val gameStates = nbt.getListTag("GameStates")
        for (value in gameStates) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.GAME_EVENTS.contains(type)) {
                    missing[Registers.GAME_EVENTS]!!.add(type)
                }
            }
        }

        // Ability registry.
        val abilities = nbt.getListTag("Abilities")
        for (value in abilities) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.ABILITIES.contains(type)) {
                    missing[Registers.ABILITIES]!!.add(type)
                }
            }
        }

        // Cursor registry.
        val cursors = nbt.getListTag("Cursors")
        for (value in cursors) {
            if (value is StringTag) {
                val type = ResourceLocation.fromString(value.value)
                if (!Registers.CURSORS.contains(type)) {
                    missing[Registers.CURSORS]!!.add(type)
                }
            }
        }
        return missing
    }

    /**
     * <h1>Update Event</h1>
     * Update event, ticks data.
     */
    open fun tick() {
        if (this.isInitialized) {
            ticks++
            bloodMoonUpdate()
        }
    }

    //            bubbleType = BubbleSystem.random(randBubblesSystem);
//            boolean canSpawn = bubbleType.canSpawn(this);
//
//            if (bubbleType.equals(BubbleInit.LEVEL_UP_BUBBLE)) {
//                System.out.println(canSpawn);
//            }
//
//            if (canSpawn) break;
//        }
    /**
     * <h1>Get a Random Bubble</h1>
     * Gets a random bubble from the bubble system.
     * Uses the randoms initiated in [.initDefaults].
     *
     * @return The bubble type.
     * @see BubbleSystem.random
     */
    val randomBubble: AbstractBubble
        get() {
            var bubbleType = random(bubbleRandomizer.typeRng, this)
            //        while (true) {
//            bubbleType = BubbleSystem.random(randBubblesSystem);
//            boolean canSpawn = bubbleType.canSpawn(this);
//
//            if (bubbleType.equals(BubbleInit.LEVEL_UP_BUBBLE)) {
//                System.out.println(canSpawn);
//            }
//
//            if (canSpawn) break;
//        }
            while (bubbleType == null) {
                bubbleType = random(bubbleRandomizer.typeRng, this)
            }
            val canSpawn = bubbleType.canSpawn(this)
            return if (canSpawn) {
                bubbleType
            } else Bubbles.NORMAL_BUBBLE.get()
        }
    abstract val hud: HUD?
    abstract val gameBounds: Rectangle2D?
    abstract val player: PlayerEntity?
    abstract fun triggerGameOver()
    override fun getDefaultState(): CompoundTag {
        val nbt = CompoundTag()
        nbt.putString("name", registryName.toString())
        nbt.putByteArray("seed", seedBytes)
        return nbt
    }

    /**
     * <h1>Get State from the Game-type to a Bson Document</h1>
     * Dumps the game-type's state to a bson document.
     */
    override fun getState(): CompoundTag {
        val nbt = CompoundTag()
        nbt.putString("name", registryName.toString())
        nbt.putByteArray("seed", seedBytes)
        return nbt
    }

    /**
     * <h1>Load State from a Bson Document to the Game-type</h1>
     * Loads the game-type's state from a bson document.
     *
     * @param state the bson document containing the game-type data.
     */
    override fun setState(state: CompoundTag) {
        seed = BigInteger(state.getByteArray("seed", BigInteger("512").toByteArray()))
    }

    abstract fun getSpawnLocation(entity: Entity?): Point?

    //    public abstract void spawnEntity(Entity entity, Point pos, @Nullable EntityRandomizer randomizer) throws InvalidObjectException;
    //
    //    public void spawnBubble(BubbleEntity bubble, Point pos) throws InvalidObjectException {
    //        spawnBubble(bubble, pos, null);
    //    }
    //
    //    public abstract void spawnBubble(BubbleEntity bubble, Point pos, @Nullable BubbleRandomizer randomizer) throws InvalidObjectException;
    //
    //    public abstract Point2D getNewBubbleLocation(int radius, Rng randX, Rng randY);
    fun registerSpawnedEntity(entity: Entity?) {
//        this.entities.add(entity);
//        Main.getLogger().info("" + this.entities.add(entity));
//        Main.getLogger().info("Added Entity");
    }

    fun attack(target: Entity?, damage: Double, damageSource: EntityDamageSource?) {
        if (target is DamageableEntity) {
            target.damage(damage, damageSource)
        } else if (target is AbstractBubbleEntity) {
            target.damage(damage, damageSource)
        }
    }

    //        System.out.println("001_A: " + stateDifficultyModifiers);
//        System.out.println("001_B: " + stateDifficultyModifiers.values());
//        System.out.println("001_C: " + new ArrayList<>(stateDifficultyModifiers.values()));
    val localDifficulty: Float
        get() {
//        System.out.println("001_A: " + stateDifficultyModifiers);
//        System.out.println("001_B: " + stateDifficultyModifiers.values());
//        System.out.println("001_C: " + new ArrayList<>(stateDifficultyModifiers.values()));
            stateDifficultyModifier = CollectionsUtils.max(ArrayList(stateDifficultyModifiers.values), 1f)
            val difficulty = difficulty
            return if (player == null) difficulty.defaultLocal * stateDifficultyModifier else ((player!!.level - 1) * 5 + 1) * difficulty.defaultLocal * stateDifficultyModifier
        }

    @Synchronized
    fun setStateDifficultyModifier(gameEvent: GameEvent, modifier: Float) {
//        System.out.println("SET_DIFF_MOD_001: " + gameState);
//        System.out.println("SET_DIFF_MOD_002: " + modifier);
        stateDifficultyModifiers[gameEvent] = modifier
        //        System.out.println("SET_DIFF_MOD_003: " + stateDifficultyModifiers);
    }

    @Synchronized
    fun removeStateDifficultyModifier(gameEvent: GameEvent) {
//        System.out.println("REMOVE_DIFF_MOD_001: " + gameState);
        stateDifficultyModifiers.remove(gameEvent)
        //        System.out.println("REMOVE_DIFF_MOD_002: " + stateDifficultyModifiers);
    }

    fun getStateDifficultyModifier(gameEvent: GameEvent): Any? {
        return stateDifficultyModifiers[gameEvent]
    }

    open fun quit() {
        isGameOver = false
    }

    abstract fun renderHUD(gg: GraphicsProcessor?)
    abstract fun renderGUI(gg: GraphicsProcessor?)
    fun drawBubble(g: GraphicsProcessor, x: Double, y: Double, radius: Int, vararg colors: Color?) {
        var i = 0.0
        for (color in colors) {
            if (i == 0.0) {
                if (colors.size >= 2) {
                    g.stroke(BasicStroke(2.2f))
                } else {
                    g.stroke(BasicStroke(2.0f))
                }
            } else if (i == (colors.size - 1).toDouble()) {
                g.stroke(BasicStroke(2.0f))
            } else {
                g.stroke(BasicStroke(2.2f))
            }
            g.color(color)
            val ellipse = getEllipse(x - radius.toFloat() / 2, y - radius.toFloat() / 2, radius.toDouble(), i)
            g.shape(ellipse)
            i += 2.0
        }
    }

    private fun getEllipse(x: Double, y: Double, r: Double, i: Double): Ellipse2D {
        return Ellipse2D.Double(x + i, y + i, r - i * 2f, r - i * 2f)
    }

    fun isGameStateActive(gameEvent: GameEvent): Boolean {
        return gameEventActive.contains(gameEvent)
    }

    fun addGameStateActive(gameEvent: GameEvent) {
        gameEventActive.add(gameEvent)
    }

    fun removeGameStateActive(gameEvent: GameEvent) {
        gameEventActive.remove(gameEvent)
    }

    fun bloodMoonUpdate() {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        if (!bloodMoonTriggered) {
            if (nextBloodMoonCheck == 0L) {
                nextBloodMoonCheck = System.currentTimeMillis() + 10000
            }
            if (nextBloodMoonCheck < System.currentTimeMillis()) {
//                System.out.println("Holy Quackemoly");
                if (bloodMoonRandom.getNumber(0, 720, ticks) == 0) {
                    triggerBloodMoon()
                } else {
                    nextBloodMoonCheck = System.currentTimeMillis() + 10000
                }
            }
        } else {
            if (bloodMoonAnimation != null) {
                globalBubbleSpeedModifier = bloodMoonAnimation!!.animate()
                if (bloodMoonAnimation!!.isEnded) {
                    GameEvents.BLOOD_MOON_EVENT.get().activate()
                    environment!!.currentGameEvent = GameEvents.BLOOD_MOON_EVENT.get()
                    isBloodMoonActive = true
                    if (loadedGame.ambientAudio != null) {
                        loadedGame.ambientAudio!!.stop()
                    }
                    bloodMoonAnimation = null
                    bloodMoonAnimation1 = Animation(8.0, 1.0, 1000.0)
                }
            } else if (bloodMoonAnimation1 != null) {
                globalBubbleSpeedModifier = bloodMoonAnimation1!!.animate()
                if (bloodMoonAnimation1!!.isEnded) {
                    bloodMoonAnimation1 = null
                }
            } else {
                globalBubbleSpeedModifier = 1.0
            }
        }
    }

    fun triggerBloodMoon() {
        println("Triggering blood moon...")
        if (!bloodMoonTriggered) {
            println("Triggered blood moon.")
            bloodMoonTriggered = true
            bloodMoonAnimation = Animation(1.0, 8.0, 10000.0)
        } else {
            println("Blood moon already triggered!")
        }
        BubbleBlaster.instance.graphicsConfigurator.disableAntialiasing()
    }

    fun stopBloodMoon() {
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        if (isBloodMoonActive) {
            isBloodMoonActive = false
            bloodMoonTriggered = false
            GameEvents.BLOOD_MOON_EVENT.get().deactivate()
            loadedGame.ambientAudio!!.stop()
        }
        BubbleBlaster.instance.graphicsConfigurator.resetAntialiasing()
    }

    abstract fun getEntityId(entity: Entity?): Long
    protected fun overrideXCoords() {
        isXCoordOverridden = true
    }

    protected fun overrideYCoords() {
        isYCoordOverridden = true
    }

    companion object {
        /**
         * <h1>Load State from Bytearray.</h1>
         * Loads the game-type state from a bytearray.
         *
         * @param save            an bytearray of data to get the game-type from.
         * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
         * @return the game-type loaded from the save.
         */
        @JvmStatic
        @Throws(IOException::class)
        fun loadState(save: SavedGame, infoTransporter: InfoTransporter?): AbstractGameMode {
            val document = save.loadData("Game")
            val name = document.getString("Name")
            val resource = ResourceLocation.fromString(name)
            val gameType = Registers.GAME_TYPES[resource]
            gameType!!.setState(document)
            gameType.savedGame = save
            return gameType
        }

        fun getGameTypeFromState(nbt: BsonDocument): AbstractGameMode? {
            return try {
                Registry.getRegistry(AbstractGameMode::class.java)[ResourceLocation.fromString(nbt.getString("Name").value)]
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    /**
     * Game-type constructor.
     */
    init {
        rNG.seed = seed
        initDefaults()
    }
}