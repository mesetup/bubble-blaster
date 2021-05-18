package com.qtech.bubbles.common.gametype;

import com.qtech.bubbles.LoadedGame;
import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.*;
import com.qtech.bubbles.common.bubble.BubbleSystem;
import com.qtech.bubbles.common.entity.AbstractBubbleEntity;
import com.qtech.bubbles.common.entity.DamageSource;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.common.entity.LivingEntity;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.interfaces.DefaultStateHolder;
import com.qtech.bubbles.common.interfaces.Listener;
import com.qtech.bubbles.common.interfaces.StateHolder;
import com.qtech.bubbles.common.random.*;
import com.qtech.bubbles.common.scene.Scene;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.event.RenderEvent;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.TickEvent;
import com.qtech.bubbles.gametype.ClassicType;
import com.qtech.bubbles.graphics.Animation;
import com.qtech.bubbles.init.Bubbles;
import com.qtech.bubbles.init.GameEvents;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.util.CollectionsUtils;
import org.bson.*;
import org.bson.codecs.BsonDocumentCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.*;

/**
 * <h1>GameType baseclass</h1>
 * Baseclass for all game-types, such as {@link ClassicType}
 *
 * @see ClassicType
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation", "unused", "FieldCanBeLocal", "UnusedReturnValue"})
public abstract class AbstractGameType extends RegistryEntry implements StateHolder, DefaultStateHolder, Listener {
    protected Environment environment;
    protected BigInteger seed = BigInteger.valueOf(512);
    //    protected final List<Entity> entities = new CopyOnWriteArrayList<>();
    @Deprecated
    protected final List<BubbleEntity> bubbles = new ArrayList<>();

    // Types.
    protected final QBubbles game = QBubbles.getInstance();

    // Flags.
    protected boolean gameOver = false;
    protected boolean globalBubbleFreeze = false;
    private boolean bloodMoonActive;
    private boolean bloodMoonTriggered;

    // Enums.
    @SuppressWarnings("FieldMayBeFinal")
    protected Difficulty difficulty = Difficulty.NORMAL;

    // State difficulties.
    @SuppressWarnings("FieldCanBeLocal")
    private final Map<GameEvent, Float> stateDifficultyModifiers = Collections.synchronizedMap(new HashMap<>());

    // Scene
    protected Scene scene;

    // Animations:
    private Animation bloodMoonAnimation = null;
    private Animation bloodMoonAnimation1;

    // Modifiers
    private double globalBubbleSpeedModifier = 1;
    private float stateDifficultyModifier = 1;
    private final HashSet<GameEvent> gameEventActive = new HashSet<>();
    private final Rng bloodMoonRandom = new Rng(new PseudoRandom(seed), 256, 0);
    protected SavedGame savedGame;

    // Checks:
    private long nextBloodMoonCheck;

    // Values:
    private long resultScore;
    protected long ticks;
    protected boolean initialized = false;

    // Random & seeding.
    public PseudoRandom getRNG() {
        return rng;
    }

    public BigInteger getSeed() {
        return rng.getSeed();
    }

    public byte[] getSeedBytes() {
        return rng.getSeed().toByteArray();
    }

    // Randomizers
    protected final PseudoRandom rng = new PseudoRandom(seed);
    protected int rngIndex = 0;
    protected Rng bubbleTypesRng;
    protected Rng bubblesXPosRng;
    protected Rng bubblesYPosRng;
    protected Rng bubblesSpeedRng;
    protected Rng bubblesRadiusRng;
    protected Rng bubblesDefenseRng;
    protected Rng bubblesAttackRng;
    protected Rng bubblesScoreRng;
    protected BubbleRandomizer bubbleRandomizer = new BubbleRandomizer(this);
    protected final HashMap<ResourceLocation, Rng> rngTypes = new HashMap<>();

    // Initial entities:
    protected PlayerEntity player;

    /**
     * Game-type constructor.
     */
    public AbstractGameType() {
        rng.setSeed(seed);
        this.initDefaults();
    }


    /**
     * <h1>Initialize Randomizers.</h1>
     * Initializes the randomizers such as for bubble coords, or radius.
     *
     * @see #addRNG(String, int, int)
     */
    protected void initDefaults() {
        bubbleTypesRng = addRNG("qbubbles:bubbles_system", 0, 0);
        bubblesXPosRng = addRNG("qbubbles:bubbles_x", 0, 1);
        bubblesYPosRng = addRNG("qbubbles:bubbles_y", 0, 2);
        bubblesSpeedRng = addRNG("qbubbles:bubbles_speed", 0, 3);
        bubblesRadiusRng = addRNG("qbubbles:bubbles_radius", 0, 4);
        bubblesDefenseRng = addRNG("qbubbles:bubbles_defense", 0, 5);
        bubblesAttackRng = addRNG("qbubbles:bubbles_attack", 0, 6);
        bubblesScoreRng = addRNG("qbubbles:bubbles_score", 0, 7);
    }

    /**
     * <h1>Add Randomizer</h1>
     * Adds a randomizer to the game type.
     *
     * @param key The key (name) to save it to.
     * @return A {@link QBRandom} object.
     */
    protected Rng addRNG(String key, int index, int subIndex) {
        Rng rand = new Rng(rng, index, subIndex);
        rngTypes.put(ResourceLocation.fromString(key), rand);
        return rand;
    }

    /**
     * <h1>Load Game Type.</h1>
     * Used for start the game-type.
     */
    public abstract void start();

    /**
     * <h1>Initialize Game Type.</h1>
     * Used for initialize the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    public abstract void init(Environment environment, InfoTransporter infoTransporter);

    /**
     * <h1>Load Game Type.</h1>
     * Used for loading the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    public abstract void load(Environment environment, InfoTransporter infoTransporter);

    /**
     * <h1>Create Save Data</h1>
     * Used for creating the save data.
     *
     * @param savedGame       the saved game to create the data for.
     * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
     */
    public abstract void createSaveData(SavedGame savedGame, InfoTransporter infoTransporter);

    /**
     * <h1>Load Save Data</h1>
     * Used for loading the save data.
     */
    @SuppressWarnings("EmptyMethod")
    public abstract void loadSaveData(SavedGame savedGame, InfoTransporter infoTransporter);

    /**
     * <h1>Dump Save Data</h1>
     * Used for storing the save data.
     *
     * @param savedGame the saved game to write the data to.
     */
    public abstract void dumpSaveData(SavedGame savedGame);

    /**
     * <h1>Render Event</h1>
     * Render event, renders objects to the canvas.
     *
     * @see RenderEvent
     */
    @SuppressWarnings("EmptyMethod")
    public abstract void render(Graphics2D gg);

    /**
     * <h1>Dump Default State</h1>
     * Dumps the default state to the given saved game.
     *
     * @see SavedGame
     */
    public void dumpDefaultState(SavedGame savedGame, InfoTransporter infoTransporter) {
        if (!QBubbles.getInstance().isGameLoaded()) {
            return;
        }

        try {
            savedGame.dumpData("Gave", getDefaultState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>Dump State to Output Stream</h1>
     * Dumps the game-type state to the output stream.
     *
     * @param output the output stream to write the state to.
     * @throws IOException when an I/O error occurred.
     */
    public void dumpState(OutputStream output) throws IOException {
        BsonDocument document = getState();
        SavedGame currentSave = QBubbles.getInstance().getCurrentSave();

        assert currentSave != null;
        output.write(new RawBsonDocument(document, new BsonDocumentCodec()).getByteBuffer().array());
    }

    /**
     * <h1>Load State from Bytearray.</h1>
     * Loads the game-type state from a bytearray.
     *
     * @param save            an bytearray of data to get the game-type from.
     * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
     * @return the game-type loaded from the save.
     */
    public static AbstractGameType loadState(SavedGame save, InfoTransporter infoTransporter) throws IOException {
        BsonDocument document = save.loadData("Game");
        String name = document.getString("Name").getValue();
        ResourceLocation resource = ResourceLocation.fromString(name);

        AbstractGameType gameType = Registers.GAME_TYPES.get(resource);
        gameType.setState(document);
        gameType.savedGame = save;
        return gameType;
    }

    /**
     * Repair a saved game.
     *
     * @param savedGame the saved game to repair.
     * @return if repair is successful.
     */
    public abstract boolean repair(SavedGame savedGame);

    /**
     * Convert a saved game.
     *
     * @param savedGame the saved game to convert.
     * @return if conversion is successful.
     */
    public abstract boolean convert(SavedGame savedGame);

    /**
     * Get game-type build version.
     *
     * @return the game-type version.
     */
    public abstract int getGameTypeVersion();

    /**
     * Check for missing entries in the registry to load the saved game.
     *
     * @param savedGame the saved game to check for.
     * @return a hashmap container as key the registry, and as value an list of missing resource locations of the registry.
     * @throws IOException when an I/O error occurred.
     */
    public HashMap<Registry<?>, List<ResourceLocation>> checkRegistry(SavedGame savedGame) throws IOException {
        HashMap<Registry<?>, List<ResourceLocation>> missing = new HashMap<>();
        BsonDocument bsonDocument = savedGame.loadData("registry");

        // Bubble registry.
        BsonArray bubbles = bsonDocument.getArray("Bubbles");
        for (BsonValue value : bubbles) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.BUBBLES.contains(type)) {
                    missing.get(Registers.BUBBLES).add(type);
                }
            }
        }

        // Ammo type registry.
        BsonArray ammoTypes = bsonDocument.getArray("AmmoTypes");
        for (BsonValue value : ammoTypes) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.AMMO_TYPES.contains(type)) {
                    missing.get(Registers.AMMO_TYPES).add(type);
                }
            }
        }

        // Effect registry.
        BsonArray effects = bsonDocument.getArray("Effects");
        for (BsonValue value : effects) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.EFFECTS.contains(type)) {
                    missing.get(Registers.EFFECTS).add(type);
                }
            }
        }

        // Entity registry.
        BsonArray entities = bsonDocument.getArray("Entities");
        for (BsonValue value : entities) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.ENTITIES.contains(type)) {
                    missing.get(Registers.ENTITIES).add(type);
                }
            }
        }

        // Game state registry.
        BsonArray gameStates = bsonDocument.getArray("GameStates");
        for (BsonValue value : gameStates) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.GAME_EVENTS.contains(type)) {
                    missing.get(Registers.GAME_EVENTS).add(type);
                }
            }
        }

        // Ability registry.
        BsonArray abilities = bsonDocument.getArray("Abilities");
        for (BsonValue value : abilities) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.ABILITIES.contains(type)) {
                    missing.get(Registers.ABILITIES).add(type);
                }
            }
        }

        // Cursor registry.
        BsonArray cursors = bsonDocument.getArray("Cursors");
        for (BsonValue value : cursors) {
            if (value.isString()) {
                ResourceLocation type = ResourceLocation.fromString(value.asString().getValue());
                if (!Registers.CURSORS.contains(type)) {
                    missing.get(Registers.CURSORS).add(type);
                }
            }
        }

        return missing;
    }

    /**
     * <h1>Update Event</h1>
     * Update event, ticks data.
     */
    public void tick() {
        if (initialized) {
            this.ticks++;
        }
    }

    /**
     * <h1>Attack an entity.</h1>
     * Attacks an entity no matter if it is a {@link LivingEntity} or a {@link AbstractBubbleEntity}.
     *
     * @param entity The {@link Entity} to attack.
     * @param value  The <b>’raw‘</b> attack value.
     */
    @Deprecated
    public void attack(Entity entity, double value) {
        if (entity instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) entity;
            e.attack(value);
        } else if (entity instanceof AbstractBubbleEntity) {
            AbstractBubbleEntity e = (AbstractBubbleEntity) entity;
            e.damage(value);
        }
    }

    /**
     * <h1>Get a Random Bubble</h1>
     * Gets a random bubble from the bubble system.
     * Uses the randoms initiated in {@link #initDefaults()}.
     *
     * @return The bubble type.
     * @see BubbleSystem#random(Rng, AbstractGameType)
     */
    @NotNull
    public AbstractBubble getRandomBubble() {
        AbstractBubble bubbleType = BubbleSystem.random(getBubbleRandomizer().getTypeRng(), this);
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
            bubbleType = BubbleSystem.random(bubbleRandomizer.getTypeRng(), this);
        }

        boolean canSpawn = bubbleType.canSpawn(this);

        if (canSpawn) {
            return bubbleType;
        }
        return Bubbles.NORMAL_BUBBLE.get();
    }

    public abstract HUD getHUD();

    public abstract Rectangle2D getGameBounds();

    @Nullable
    public abstract PlayerEntity getPlayer();

    public BubbleRandomizer getBubbleRandomizer() {
        return bubbleRandomizer;
    }

    protected void setBubbleRandomizer(BubbleRandomizer randomizer) {
        this.bubbleRandomizer = randomizer;
    }

    public abstract void triggerGameOver();

    @Override
    public final BsonDocument getDefaultState() {
        BsonDocument nbt = new BsonDocument();
        nbt.put("Name", new BsonString(this.getRegistryName().toString()));
        nbt.put("Seed", new BsonBinary(getSeedBytes()));

        return nbt;
    }

    /**
     * <h1>Get State from the Game-type to a Bson Document</h1>
     * Dumps the game-type's state to a bson document.
     */
    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument nbt = new BsonDocument();
        nbt.put("Name", new BsonString(this.getRegistryName().toString()));
        nbt.put("Seed", new BsonBinary(getSeedBytes()));

        return nbt;
    }

    /**
     * <h1>Load State from a Bson Document to the Game-type</h1>
     * Loads the game-type's state from a bson document.
     *
     * @param document the bson document containing the game-type data.
     */
    @Override
    public void setState(BsonDocument document) {
        this.seed = new BigInteger(document.getBinary("Seed", new BsonBinary(new BigInteger("512").toByteArray())).getData());
    }

    @Nullable
    public static AbstractGameType getGameTypeFromState(BsonDocument nbt) {
        try {
            return Registry.getRegistry(AbstractGameType.class).get(ResourceLocation.fromString(nbt.getString("Name").getValue()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public abstract Point getSpawnLocation(Entity entity);

//    public abstract void spawnEntity(Entity entity, Point pos, @Nullable EntityRandomizer randomizer) throws InvalidObjectException;
//
//    public void spawnBubble(BubbleEntity bubble, Point pos) throws InvalidObjectException {
//        spawnBubble(bubble, pos, null);
//    }
//
//    public abstract void spawnBubble(BubbleEntity bubble, Point pos, @Nullable BubbleRandomizer randomizer) throws InvalidObjectException;
//
//    public abstract Point2D getNewBubbleLocation(int radius, Rng randX, Rng randY);

    @SuppressWarnings("EmptyMethod")
    public void registerSpawnedEntity(Entity entity) {
//        this.entities.add(entity);
//        Main.getLogger().info("" + this.entities.add(entity));
//        Main.getLogger().info("Added Entity");
    }

    public void attack(Entity target, double damage, DamageSource damageSource) {
        if (target instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) target;
            e.attack(damage, damageSource);
        } else if (target instanceof AbstractBubbleEntity) {
            AbstractBubbleEntity e = (AbstractBubbleEntity) target;
            e.damage(damage, damageSource);
        }
    }

    public final float getLocalDifficulty() {
//        System.out.println("001_A: " + stateDifficultyModifiers);
//        System.out.println("001_B: " + stateDifficultyModifiers.values());
//        System.out.println("001_C: " + new ArrayList<>(stateDifficultyModifiers.values()));
        stateDifficultyModifier = CollectionsUtils.max(new ArrayList<>(stateDifficultyModifiers.values()), 1f);

        Difficulty difficulty = getDifficulty();
        if (getPlayer() == null) return difficulty.getDefaultLocal() * stateDifficultyModifier;

        return ((getPlayer().getLevel() - 1) * 5 + 1) * difficulty.getDefaultLocal() * stateDifficultyModifier;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public synchronized final void setStateDifficultyModifier(GameEvent gameEvent, float modifier) {
//        System.out.println("SET_DIFF_MOD_001: " + gameState);
//        System.out.println("SET_DIFF_MOD_002: " + modifier);
        stateDifficultyModifiers.put(gameEvent, modifier);
//        System.out.println("SET_DIFF_MOD_003: " + stateDifficultyModifiers);
    }

    public synchronized final void removeStateDifficultyModifier(GameEvent gameEvent) {
//        System.out.println("REMOVE_DIFF_MOD_001: " + gameState);
        stateDifficultyModifiers.remove(gameEvent);
//        System.out.println("REMOVE_DIFF_MOD_002: " + stateDifficultyModifiers);
    }

    public final Object getStateDifficultyModifier(GameEvent gameEvent) {
        return stateDifficultyModifiers.get(gameEvent);
    }

    public Scene getScene() {
        return scene;
    }

    public void quit() {
        gameOver = false;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public abstract void renderHUD(Graphics2D gg);

    @SuppressWarnings("EmptyMethod")
    public abstract void renderGUI(Graphics2D gg);

    public void drawBubble(Graphics2D g, double x, double y, int radius, Color... colors) {
        double i = 0f;
        for (Color color : colors) {
            if (i == 0) {
                if (colors.length >= 2) {
                    g.setStroke(new BasicStroke(2.2f));
                } else {
                    g.setStroke(new BasicStroke(2.0f));
                }
            } else if (i == colors.length - 1) {
                g.setStroke(new BasicStroke(2.0f));
            } else {
                g.setStroke(new BasicStroke(2.2f));
            }

            g.setColor(color);

            Ellipse2D ellipse = this.getEllipse(x - (float) radius / 2, y - (float) radius / 2, radius, i);
            g.draw(ellipse);

            i += 2f;
        }
    }

    private Ellipse2D getEllipse(double x, double y, double r, double i) {
        return new Ellipse2D.Double(x + i, y + i, r - i * 2f, r - i * 2f);
    }

    public void setGlobalBubbleSpeedModifier(double speedModifier) {
        this.globalBubbleSpeedModifier = speedModifier;
    }

    public double getGlobalBubbleSpeedModifier() {
        return globalBubbleFreeze ? 0 : globalBubbleSpeedModifier;
    }

    public void setGlobalBubbleFreeze(boolean b) {
        this.globalBubbleFreeze = b;
    }

    public boolean isGlobalBubbleFreeze() {
        return this.globalBubbleFreeze;
    }

    public boolean isGameStateActive(GameEvent gameEvent) {
        return gameEventActive.contains(gameEvent);
    }

    public void addGameStateActive(GameEvent gameEvent) {
        gameEventActive.add(gameEvent);
    }

    public void removeGameStateActive(GameEvent gameEvent) {
        gameEventActive.remove(gameEvent);
    }

    public boolean isBloodMoonActive() {
        return bloodMoonActive;
    }

    @SubscribeEvent
    public void bloodMoonUpdate(TickEvent event) {
        LoadedGame loadedGame = QBubbles.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return;
        }

        if (!bloodMoonTriggered) {
            if (nextBloodMoonCheck == 0) {
                nextBloodMoonCheck = System.currentTimeMillis() + 10000;
            }

            if (nextBloodMoonCheck < System.currentTimeMillis()) {
//                System.out.println("Holy Quackemoly");
                if (bloodMoonRandom.getNumber(0, 720, getTicks()) == 0) {
                    triggerBloodMoon();
                } else {
                    nextBloodMoonCheck = System.currentTimeMillis() + 10000;
                }
            }
        } else {
            if (bloodMoonAnimation != null) {
                setGlobalBubbleSpeedModifier(bloodMoonAnimation.animate());
                if (bloodMoonAnimation.isEnded()) {
                    GameEvents.BLOOD_MOON_EVENT.get().activate();
                    bloodMoonActive = true;

                    loadedGame.getAmbientAudio().stop();
                    bloodMoonAnimation = null;
                    bloodMoonAnimation1 = new Animation(8d, 1d, 1000d);
                }
            } else if (bloodMoonAnimation1 != null) {
                setGlobalBubbleSpeedModifier(bloodMoonAnimation1.animate());
                if (bloodMoonAnimation1.isEnded()) {
                    bloodMoonAnimation1 = null;
                }
            } else {
                setGlobalBubbleSpeedModifier(1);
            }
        }
    }

    public void triggerBloodMoon() {
        System.out.println("Triggering blood moon...");
        if (!bloodMoonTriggered) {
            System.out.println("Triggered blood moon.");
            bloodMoonTriggered = true;
            bloodMoonAnimation = new Animation(1d, 8d, 10000d);
        } else {
            System.out.println("Blood moon already triggered!");
        }

        QBubbles.getInstance().getGraphicsEngine().disableAntialiasing();
    }

    public void stopBloodMoon() {
        LoadedGame loadedGame = QBubbles.getInstance().getLoadedGame();
        if (loadedGame == null) {
            return;
        }

        if (bloodMoonActive) {
            bloodMoonActive = false;
            bloodMoonTriggered = false;
            GameEvents.BLOOD_MOON_EVENT.get().deactivate();
            loadedGame.getAmbientAudio().stop();
        }

        QBubbles.getInstance().getGraphicsEngine().resetAntialiasing();
    }

    public long getResultScore() {
        return resultScore;
    }

    public void setResultScore(long resultScore) {
        this.resultScore = resultScore;
    }

    public long getTicks() {
        return ticks;
    }

    public abstract long getEntityId(Entity entity);

    public boolean isInitialized() {
        return initialized;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
