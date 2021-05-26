package com.qtech.bubbles.gametype;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.Difficulty;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.IRegistryEntry;
import com.qtech.bubbles.common.InfoTransporter;
import com.qtech.bubbles.common.crash.CrashReport;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.core.common.SavedGame;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.event.FilterEvent;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.gametype.hud.ClassicHUD;
import com.qtech.bubbles.init.Bubbles;
import com.qtech.bubbles.init.Entities;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.settings.GameSettings;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonString;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class ClassicType extends AbstractGameType {
    private static int maxBubbles = 1_000;

    // Hud and events-active flag.
    private ClassicHUD hud;
    private boolean eventActive;

    // Threads
    private Thread spawner;
    private EventBus.Handler binding;

    public ClassicType() {
        super();
    }

    @Override
    public void init(Environment environment, InfoTransporter infoTransporter) {
        this.environment = environment;

        try {
            // Init defaults.
            this.initDefaults();

            this.hud = new ClassicHUD(this);

            // Spawn bubbles
            infoTransporter.log("Spawning bubbles...");

            ticks = -1;
            for (int i = 0; i < maxBubbles; i++) {
                AbstractBubble bubble = getRandomBubble();
                while (!bubble.canSpawn(this)) {
                    ticks--;
                    bubble = getRandomBubble();
                }

                if (bubble != Bubbles.LEVEL_UP_BUBBLE.get()) {
                    Point pos = new Point(this.bubblesXPosRng.getNumber(0, BubbleBlaster.getInstance().getWidth(), -i - 1), this.bubblesYPosRng.getNumber(0, BubbleBlaster.getInstance().getWidth(), -i - 1));
                    environment.spawn(Entities.BUBBLE.get().create(this), pos);
                }

                ticks--;

                infoTransporter.log("Spawning bubble " + i + "/" + maxBubbles);
            }

            ticks = 0;

            // Spawn player
            infoTransporter.log("Spawning player...");
            this.player = new PlayerEntity(environment.getGameType());
            environment.spawn(player, new Point(game.getWidth() / 4, BubbleBlaster.getInstance().getHeight() / 2));
        } catch (Exception e) {
            CrashReport crashReport = new CrashReport("Could not initialize classic game type.", e);
            throw crashReport.getReportedException();
        }

        // Bind events.
        this.bindEvents();

        ClassicType.maxBubbles = GameSettings.instance().getMaxBubbles();
        this.spawner = new Thread(this::spawnerThread, "SpawnerThread");
        this.initialized = true;
    }

    @Override
    public void load(Environment environment, InfoTransporter infoTransporter) {
        this.environment = environment;
        this.hud = new ClassicHUD(this);
        this.initialized = true;
    }

    /**
     * <h1>Load Game Type.</h1>
     * Used for initializing the game.
     */
    @Override
    public void start() {
        spawner.start();
    }

    @SuppressWarnings("EmptyMethod")
    public void spawnerThread() {
//        while (eventActive) {
//            if (this.environment.getEntitiesByClass(BubbleEntity.class).size() < maxBubbles) {
//                AbstractBubble bubble = getRandomBubble();
//                if (bubble.canSpawn(this)) {
//                    environment.spawnEntity(EntityInit.BUBBLE.get());
//                }
//            }
//        }
    }

    @SuppressWarnings("EmptyMethod")
    @SubscribeEvent
    public void onFilter(FilterEvent evt) {
//        evt.addFilter(filter);
    }

    public static int getMaxBubbles() {
        return maxBubbles;
    }

    @SuppressWarnings("unused")
    public static void setMaxBubbles(int maxBubbles) {
        ClassicType.maxBubbles = maxBubbles;
    }

    public ClassicHUD getHud() {
        return hud;
    }

    @SuppressWarnings("unused")
    public boolean isEventActive() {
        return eventActive;
    }

    @Override
    protected void initDefaults() {
        super.initDefaults();
//        addRandom("qbubbles:bubbles_x");
//        addRandom("qbubbles:bubbles_y");
//        addRandom("qbubbles:bubbles_radius");
//        addRandom("qbubbles:bubbles_speed");
    }

    /**
     * Creates save data.
     *
     * @param savedGame       the saved game to write the initial data to,
     * @param infoTransporter an info transporter to the save-loading screen.
     * @see AbstractGameType#loadSaveData(SavedGame, InfoTransporter)
     * @see AbstractGameType#dumpSaveData(SavedGame)
     */
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    @Override
    public void createSaveData(SavedGame savedGame, InfoTransporter infoTransporter) {
        File saveDir = savedGame.getDirectory();
        try {
            infoTransporter.log("Dumping registries in save...");
            savedGame.createFolders("Registries");
            dumpRegistryData(savedGame, "Registries/ItemTypes", Registers.ITEMS);
            dumpRegistryData(savedGame, "Registries/AmmoTypes", Registers.AMMO_TYPES);
            dumpRegistryData(savedGame, "Registries/GameStates", Registers.GAME_EVENTS);
            dumpRegistryData(savedGame, "Registries/EntityTypes", Registers.ENTITIES);
            dumpRegistryData(savedGame, "Registries/BubbleTypes", Registers.BUBBLES);
            dumpRegistryData(savedGame, "Registries/AbilityTypes", Registers.ABILITIES);
            dumpRegistryData(savedGame, "Registries/Effects", Registers.EFFECTS);
            dumpRegistryData(savedGame, "Registries/Cursors", Registers.CURSORS);

            infoTransporter.log("Dumping default data...");
            savedGame.dumpData("Entities", new BsonDocument());
            savedGame.dumpData("GameStates", new BsonDocument());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    private <T extends IRegistryEntry> void dumpRegistryData(SavedGame savedGame, String relPath, Registry<T> registry) {
        BsonDocument document = new BsonDocument();
        BsonArray entries = new BsonArray();

        for (T type : registry.values()) {
            entries.add(new BsonString(type.getRegistryName().toString()));
        }
        document.put("Entries", entries);

        try {
            savedGame.dumpData(relPath, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads save data.
     *
     * @see AbstractGameType#createSaveData(SavedGame, InfoTransporter)
     * @see AbstractGameType#dumpSaveData(SavedGame)
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    public void loadSaveData(SavedGame savedGame, InfoTransporter infoTransporter) {

    }

    /**
     * Creates save data.
     *
     * @param savedGame The saved game to write the save data to.
     * @see AbstractGameType#createSaveData(SavedGame, InfoTransporter)
     * @see AbstractGameType#loadSaveData(SavedGame, InfoTransporter)
     */
    @Override
    public void dumpSaveData(SavedGame savedGame) {
        try {
            BsonDocument entities = new BsonDocument();
            BsonArray entityArray = new BsonArray();
            for (Entity entity : this.environment.getEntities()) {
                if (entity.isSpawned()) {
                    entityArray.add(entity.getState());
                }
            }

            entities.put("Values", entityArray);
            savedGame.dumpData("Entities", entities);

            BsonDocument gameStates = new BsonDocument();

            savedGame.dumpData("GameStates", new BsonDocument());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GraphicsProcessor gg) {

    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument state = super.getState();
        state.put("Difficulty", new BsonString(this.difficulty.name()));

        return state;
    }

    @Override
    public void setState(BsonDocument document) {
        this.difficulty = Difficulty.valueOf(document.getString("Difficulty").getValue());
    }

    @Override
    public boolean repair(SavedGame savedGame) {
        try {
            BsonDocument document = savedGame.loadInfoData();
            try {
                int dataVersion = document.getInt32("GameTypeVersion").getValue();
                if (dataVersion != 0) {
                    return false;
                }
                return false;
            } catch (BsonInvalidOperationException e) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean convert(SavedGame savedGame) {
        try {
            BsonDocument document = savedGame.loadInfoData();
            int dataVersion = document.getInt32("GameTypeVersion").getValue();
            if (dataVersion == 0) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getGameTypeVersion() {
        return 0;
    }

    @Override
    public void renderHUD(GraphicsProcessor gg) {
        hud.renderHUD(gg);
    }

    @Override
    public void renderGUI(GraphicsProcessor gg) {

    }

    @Override
    public ClassicHUD getHUD() {
        return this.hud;
    }

    @Override
    public Rectangle2D getGameBounds() {
        return new Rectangle2D.Double(70d, 0d, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight() - 70d);
    }

    @Override
    public PlayerEntity getPlayer() {
        return this.player;
    }

    /**
     * <h1>Trigger Game Over</h1>
     * Deletes player and set game over flag in ClassicHUD. Showing Game Over message.
     *
     * @see ClassicHUD#setGameOver()
     */
    @Override
    public void triggerGameOver() {
        if (!isGameOver()) {
            setResultScore(Math.round(Objects.requireNonNull(getPlayer()).getScore()));
        }

        environment.gameOver(player);
        player.delete();
        hud.setGameOver();
        gameOver = true;
    }

    @Override
    public Point getSpawnLocation(Entity entity) {
        return new Point(
                (int) getGameBounds().getMaxX() + entity.getBounds().width,
                (int) bubblesYPosRng.getNumber(getGameBounds().getMinY() - entity.getBounds().height, getGameBounds().getMaxY() + entity.getBounds().height, getTicks())
        );
    }

    @Override
    public void quit() {
        for (Entity entity : environment.getEntities()) {
            entity.delete();
        }
        this.hud = null;
    }

    @Override
    public void bindEvents() {
//        tickEventCode = QUpdateEvent.getInstance().addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        renderEventCode = QRenderEvent.getInstance().addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);

        BubbleBlaster.getEventBus().register(this);
        this.eventActive = true;
    }

    @Override
    public void unbindEvents() throws NoSuchElementException {
//        QUpdateEvent.getInstance().removeListener(tickEventCode);
//        QRenderEvent.getInstance().removeListener(renderEventCode);

        BubbleBlaster.getEventBus().unregister(this);
        this.eventActive = false;
    }

    /**
     * @return True if the events are active, false otherwise.
     */
    @Override
    public boolean eventsAreActive() {
        return eventActive;
    }

    public Thread getSpawner() {
        return spawner;
    }

    @Override
    public void tick() {
        super.tick();

        if (environment.getEntities().stream().filter((entity) -> entity instanceof BubbleEntity).count() < GameSettings.instance().getMaxBubbles()) {
            EntityType<? extends BubbleEntity> entityType = BubbleEntity.getRandomType(scene, this);
            BubbleEntity bubbleEntity = entityType.create(this);
            if (bubbleEntity.getBubbleType().canSpawn(this)) {
                environment.spawn(entityType);
            }
        }

        if (initialized) {
            if (player == null) {
                throw new IllegalStateException("Player is null while initialized.");
            }
        }
    }

    @Override
    public long getEntityId(Entity entity) {
        return getTicks();
    }
}
