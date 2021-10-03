package com.ultreon.bubbles.common.gametype;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.LoadedGame;
import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.common.Difficulty;
import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.bubbles.common.interfaces.DefaultStateHolder;
import com.ultreon.bubbles.common.interfaces.StateHolder;
import com.ultreon.bubbles.common.random.BubbleRandomizer;
import com.ultreon.bubbles.common.random.PseudoRandom;
import com.ultreon.bubbles.common.random.QBRandom;
import com.ultreon.bubbles.common.random.Rng;
import com.ultreon.bubbles.entity.AbstractBubbleEntity;
import com.ultreon.bubbles.entity.BubbleEntity;
import com.ultreon.bubbles.entity.DamageableEntity;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.bubble.BubbleSystem;
import com.ultreon.bubbles.entity.damage.DamageSource;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.gametype.ClassicType;
import com.ultreon.bubbles.init.Bubbles;
import com.ultreon.bubbles.init.GameEvents;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.bubbles.util.CollectionsUtils;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.event.RenderEvent;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.render.ValueAnimator;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.gui.IGuiListener;
import org.bson.*;
import org.bson.codecs.BsonDocumentCodec;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameType baseclass
 * Baseclass for all game-types, such as {@link ClassicType}
 *
 * @see ClassicType
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation", "unused", "FieldCanBeLocal", "UnusedReturnValue"})
public abstract class AbstractGameType extends RegistryEntry implements StateHolder, DefaultStateHolder, IGuiListener {
    protected Environment environment;
    protected BigInteger seed = BigInteger.valueOf(512);
    //    protected final List<Entity> entities = new CopyOnWriteArrayList<>();
    @Deprecated
    protected final List<BubbleEntity> bubbles = new ArrayList<>();

    // Types.
    protected final BubbleBlaster game = BubbleBlaster.instance();

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
    private final Map<GameEvent, Float> stateDifficultyModifiers = new ConcurrentHashMap<>();

    // Scene
    protected Screen screen;

    // Animations:
    private ValueAnimator bloodMoonValueAnimator = null;
    private ValueAnimator bloodMoonValueAnimator1;

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
    private final Difficulty.ModifierMap modifierMap = new Difficulty.ModifierMap();

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
    protected final HashMap<ResourceEntry, Rng> rngTypes = new HashMap<>();

    /**
     * Game-type constructor.
     */
    public AbstractGameType() {
        rng.setSeed(seed);
        this.initDefaults();
    }


    /**
     * Initialize Randomizers.
     * Initializes the randomizers such as for bubble coords, or radius.
     *
     * @see #addRNG(String, int, int)
     */
    protected void initDefaults() {
        bubbleTypesRng = addRNG("bubbleblaster:bubbles_system", 0, 0);
        bubblesXPosRng = addRNG("bubbleblaster:bubbles_x", 0, 1);
        bubblesYPosRng = addRNG("bubbleblaster:bubbles_y", 0, 2);
        bubblesSpeedRng = addRNG("bubbleblaster:bubbles_speed", 0, 3);
        bubblesRadiusRng = addRNG("bubbleblaster:bubbles_radius", 0, 4);
        bubblesDefenseRng = addRNG("bubbleblaster:bubbles_defense", 0, 5);
        bubblesAttackRng = addRNG("bubbleblaster:bubbles_attack", 0, 6);
        bubblesScoreRng = addRNG("bubbleblaster:bubbles_score", 0, 7);
    }

    /**
     * Add Randomizer
     * Adds a randomizer to the game type.
     *
     * @param key The key (name) to save it to.
     * @return A {@link QBRandom} object.
     */
    protected Rng addRNG(String key, int index, int subIndex) {
        Rng rand = new Rng(rng, index, subIndex);
        rngTypes.put(ResourceEntry.fromString(key), rand);
        return rand;
    }

    /**
     * Load Game Type.
     * Used for start the game-type.
     */
    public abstract void start();

    /**
     * Initialize Game Type.
     * Used for initialize the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    public abstract void init(Environment environment, InfoTransporter infoTransporter);

    /**
     * Load Game Type.
     * Used for loading the game-type.
     *
     * @param infoTransporter info transporter, used for showing info about loading the game-type.
     */
    public abstract void load(Environment environment, InfoTransporter infoTransporter);

    /**
     * Create Save Data
     * Used for creating the save data.
     *
     * @param savedGame       the saved game to create the data for.
     * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
     */
    public abstract void createSaveData(SavedGame savedGame, InfoTransporter infoTransporter);

    /**
     * Load Save Data
     * Used for loading the save data.
     */
    @SuppressWarnings("EmptyMethod")
    public abstract void loadSaveData(SavedGame savedGame, InfoTransporter infoTransporter);

    /**
     * Dump Save Data
     * Used for storing the save data.
     *
     * @param savedGame the saved game to write the data to.
     */
    public abstract void dumpSaveData(SavedGame savedGame);

    /**
     * Render Event
     * Render event, renders objects to the canvas.
     *
     * @see RenderEvent
     */
    @SuppressWarnings("EmptyMethod")
    public abstract void render(Renderer gg);

    /**
     * Dump Default State
     * Dumps the default state to the given saved game.
     *
     * @see SavedGame
     */
    public void dumpDefaultState(SavedGame savedGame, InfoTransporter infoTransporter) {
        if (!BubbleBlaster.instance().isGameLoaded()) {
            return;
        }

        try {
            savedGame.dumpData("Gave", getDefaultState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dump State to Output Stream
     * Dumps the game-type state to the output stream.
     *
     * @param output the output stream to write the state to.
     * @throws IOException when an I/O error occurred.
     */
    public void dumpState(OutputStream output) throws IOException {
        BsonDocument document = getState();
        SavedGame currentSave = BubbleBlaster.instance().getCurrentSave();

        assert currentSave != null;
        output.write(new RawBsonDocument(document, new BsonDocumentCodec()).getByteBuffer().array());
    }

    /**
     * Load State from Bytearray.
     * Loads the game-type state from a bytearray.
     *
     * @param save            an bytearray of data to get the game-type from.
     * @param infoTransporter info transporter for showing current status to load scene or save loading scene.
     * @return the game-type loaded from the save.
     */
    public static AbstractGameType loadState(SavedGame save, InfoTransporter infoTransporter) throws IOException {
        BsonDocument document = save.loadData("Game");
        String name = document.getString("Name").getValue();
        ResourceEntry resource = ResourceEntry.fromString(name);

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
    public HashMap<Registry<?>, List<ResourceEntry>> checkRegistry(SavedGame savedGame) throws IOException {
        HashMap<Registry<?>, List<ResourceEntry>> missing = new HashMap<>();
        BsonDocument bsonDocument = savedGame.loadData("registry");

        // Bubble registry.
        BsonArray bubbles = bsonDocument.getArray("Bubbles");
        for (BsonValue value : bubbles) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.BUBBLES.contains(type)) {
                    missing.get(Registers.BUBBLES).add(type);
                }
            }
        }

        // Ammo type registry.
        BsonArray ammoTypes = bsonDocument.getArray("AmmoTypes");
        for (BsonValue value : ammoTypes) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.AMMO_TYPES.contains(type)) {
                    missing.get(Registers.AMMO_TYPES).add(type);
                }
            }
        }

        // Effect registry.
        BsonArray effects = bsonDocument.getArray("Effects");
        for (BsonValue value : effects) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.EFFECTS.contains(type)) {
                    missing.get(Registers.EFFECTS).add(type);
                }
            }
        }

        // Entity registry.
        BsonArray entities = bsonDocument.getArray("Entities");
        for (BsonValue value : entities) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.ENTITIES.contains(type)) {
                    missing.get(Registers.ENTITIES).add(type);
                }
            }
        }

        // Game state registry.
        BsonArray gameStates = bsonDocument.getArray("GameStates");
        for (BsonValue value : gameStates) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.GAME_EVENTS.contains(type)) {
                    missing.get(Registers.GAME_EVENTS).add(type);
                }
            }
        }

        // Ability registry.
        BsonArray abilities = bsonDocument.getArray("Abilities");
        for (BsonValue value : abilities) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.ABILITIES.contains(type)) {
                    missing.get(Registers.ABILITIES).add(type);
                }
            }
        }

        // Cursor registry.
        BsonArray cursors = bsonDocument.getArray("Cursors");
        for (BsonValue value : cursors) {
            if (value.isString()) {
                ResourceEntry type = ResourceEntry.fromString(value.asString().getValue());
                if (!Registers.CURSORS.contains(type)) {
                    missing.get(Registers.CURSORS).add(type);
                }
            }
        }

        return missing;
    }

    /**
     * Update Event
     * Update event, ticks data.
     */
    public void tick() {
        if (initialized) {
            this.ticks++;
            bloodMoonUpdate();
        }
    }

    /**
     * Attack an entity.
     * Attacks an entity no matter if it is a {@link DamageableEntity} or a {@link AbstractBubbleEntity}.
     *
     * @param entity The {@link Entity} to attack.
     * @param value  The <b>’raw‘</b> attack value.
     */
    @SuppressWarnings("ConstantConditions")
    @Deprecated
    public void attack(Entity entity, double value) {
        if (entity instanceof DamageableEntity e) {
            e.damage(value);
        } else if (entity instanceof AbstractBubbleEntity e) {
            e.damage(value);
        }
    }

    /**
     * Get a Random Bubble
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

        int retries = 5;
        while (bubbleType == null) {
            bubbleType = BubbleSystem.random(bubbleRandomizer.getTypeRng(), this);
            if (retries-- == 0) break;
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
     * Get State from the Game-type to a Bson Document
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
     * Load State from a Bson Document to the Game-type
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
            return Registry.getRegistry(AbstractGameType.class).get(ResourceEntry.fromString(nbt.getString("Name").getValue()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public abstract Point getSpawnLocation(Entity entity);

    @Deprecated
    @SuppressWarnings("EmptyMethod")
    public void registerSpawnedEntity(Entity entity) {

    }

    @SuppressWarnings("ConstantConditions")
    public void attack(Entity target, double damage, DamageSource damageSource) {
        if (target instanceof DamageableEntity e) {
            e.damage(damage, damageSource);
        } else if (target instanceof AbstractBubbleEntity e) {
            e.damage(damage, damageSource);
        }
    }

    public final float getLocalDifficulty() {
        stateDifficultyModifier = CollectionsUtils.max(new ArrayList<>(stateDifficultyModifiers.values()), 1f);

        Difficulty difficulty = getDifficulty();
        if (getPlayer() == null) return difficulty.getPlainModifier() * stateDifficultyModifier;

        return ((getPlayer().getLevel() - 1) * 5 + 1) * difficulty.getPlainModifier() * stateDifficultyModifier;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public final void setStateDifficultyModifier(GameEvent gameEvent, float modifier) {
        stateDifficultyModifiers.put(gameEvent, modifier);
    }

    public final void removeStateDifficultyModifier(GameEvent gameEvent) {
        stateDifficultyModifiers.remove(gameEvent);
    }

//    public final <T> void getDifficultyModifier(Difficulty.Modifier.Type<T> type, T key) {
//        this.modifierMap
//    }

    public final Object getStateDifficultyModifier(GameEvent gameEvent) {
        return stateDifficultyModifiers.get(gameEvent);
    }

    public Screen getScreen() {
        return screen;
    }

    public void quit() {
        gameOver = false;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public abstract void renderHUD(Renderer gg);

    @SuppressWarnings("EmptyMethod")
    public abstract void renderGUI(Renderer gg);

    public void drawBubble(Renderer g, double x, double y, int radius, Color... colors) {
        double i = 0f;
        for (Color color : colors) {
            if (i == 0) {
                if (colors.length >= 2) {
                    g.stroke(new BasicStroke(2.2f));
                } else {
                    g.stroke(new BasicStroke(2.0f));
                }
            } else if (i == colors.length - 1) {
                g.stroke(new BasicStroke(2.0f));
            } else {
                g.stroke(new BasicStroke(2.2f));
            }

            g.color(color);

            Ellipse2D ellipse = this.getEllipse(x - (float) radius / 2, y - (float) radius / 2, radius, i);
            g.outline(ellipse);

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

    public void bloodMoonUpdate() {
        LoadedGame loadedGame = BubbleBlaster.instance().getLoadedGame();
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
            if (bloodMoonValueAnimator != null) {
                setGlobalBubbleSpeedModifier(bloodMoonValueAnimator.animate());
                if (bloodMoonValueAnimator.isEnded()) {
                    GameEvents.BLOOD_MOON_EVENT.get().activate();
                    this.environment.setCurrentGameEvent(GameEvents.BLOOD_MOON_EVENT.get());
                    bloodMoonActive = true;

                    //noinspection ConstantConditions
                    if (loadedGame != null && loadedGame.getAmbientAudio() != null) {
                        loadedGame.getAmbientAudio().stop();
                    }
                    bloodMoonValueAnimator = null;
                    bloodMoonValueAnimator1 = new ValueAnimator(8d, 1d, 1000d);
                }
            } else if (bloodMoonValueAnimator1 != null) {
                setGlobalBubbleSpeedModifier(bloodMoonValueAnimator1.animate());
                if (bloodMoonValueAnimator1.isEnded()) {
                    bloodMoonValueAnimator1 = null;
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
            bloodMoonValueAnimator = new ValueAnimator(1d, 8d, 10000d);
        } else {
            System.out.println("Blood moon already triggered!");
        }

        BubbleBlaster.instance().getRenderSettings().disableAntialiasing();
    }

    public void stopBloodMoon() {
        LoadedGame loadedGame = BubbleBlaster.instance().getLoadedGame();
        if (loadedGame == null) {
            return;
        }

        if (bloodMoonActive) {
            bloodMoonActive = false;
            bloodMoonTriggered = false;
            GameEvents.BLOOD_MOON_EVENT.get().deactivate();
            loadedGame.getAmbientAudio().stop();
        }

        BubbleBlaster.instance().getRenderSettings().resetAntialiasing();
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
