package com.qsoftware.bubbles.gametype;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.Difficulty;
import com.qsoftware.bubbles.common.GameObject;
import com.qsoftware.bubbles.common.IRegistryEntry;
import com.qsoftware.bubbles.common.InfoTransporter;
import com.qsoftware.bubbles.common.crash.CrashReport;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.FilterEvent;
import com.qsoftware.bubbles.event.SubscribeEvent;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.gametype.hud.ClassicHUD;
import com.qsoftware.bubbles.init.BubbleInit;
import com.qsoftware.bubbles.init.EntityInit;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.Registry;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.settings.GameSettings;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class ClassicType extends AbstractGameType {
    private static int maxBubbles = 1_000;

    // Hud and events-active flag.
    private ClassicHUD hud;
    private boolean eventActive;
    @Deprecated
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    // Threads
    private Thread spawner;
    private EventBus.Handler binding;

    // Nullable
    @Nullable
    private GameScene gameScene;

    public ClassicType() {
        super();
    }

    @Override
    public void init(Environment environment, InfoTransporter infoTransporter) {
        try {
            this.environment = environment;
            // Init defaults.
            this.initDefaults();

            this.hud = new ClassicHUD(this, null);

            // Spawn bubbles
            infoTransporter.log("Spawning bubbles...");

            ticks = -1;
            for (int i = 0; i < maxBubbles; i++) {
                AbstractBubble bubble = getRandomBubble();
                while (!bubble.canSpawn(this)) {
                    ticks--;
                    bubble = getRandomBubble();
                }

                //            if (bubble == BubbleInit.LEVEL_UP_BUBBLE) {
                //                System.out.println(bubble.canSpawn(this));
                //            }

                if (bubble != BubbleInit.LEVEL_UP_BUBBLE.get()) {
                    Point pos = new Point(this.bubblesXPosRng.getNumber(0, QBubbles.getInstance().getWidth(), -i - 1), this.bubblesYPosRng.getNumber(0, QBubbles.getInstance().getWidth(), -i - 1));
                    environment.spawnEntity(EntityInit.BUBBLE.get().create(scene, this), pos);
                }

                ticks--;

                infoTransporter.log("Spawning bubble " + i + "/" + maxBubbles);
            }

            ticks = 0;

            // Spawn player
            infoTransporter.log("Spawning player...");
            this.player = new PlayerEntity(scene, this);
            this.game.environment.spawnEntity(player, new Point(game.getWidth() / 4, QBubbles.getInstance().getHeight() / 2));
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
        this.hud = new ClassicHUD(this, null);
        this.initialized = true;
    }

    /**
     * <h1>Load Game Type.</h1>
     * Used for initializing the game.
     */
    @Override
    public void start() {
        hud.setScene(scene);
        spawner.start();
    }

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

    @SubscribeEvent
    public void onFilter(FilterEvent evt) {
//        evt.addFilter(filter);
    }

    @Deprecated
    @Override
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Deprecated
    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
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
            dumpRegistryData(savedGame, "Registries/GameStates", Registers.STATES);
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
    public void loadSaveData(SavedGame savedGame, InfoTransporter infoTransporter) throws IOException {
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
    public void render(Graphics2D gg) {

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
    public void renderHUD(QBubbles game, Graphics2D gg) {
        hud.renderHUD(game, gg);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        renderHUD(game, gg);
    }

    @Override
    public ClassicHUD getHUD() {
        return this.hud;
    }

    @Override
    public Rectangle2D getGameBounds() {
        return new Rectangle2D.Double(70d, 0d, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight() - 70d);
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
            setResultScore(Math.round(getPlayer().getScore()));
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
    public void exit() {
        for (Entity entity : environment.getEntities()) {
            environment.removeEntity(entity);
        }
        this.hud = null;
    }

    @Override
    public void bindEvents() {
//        tickEventCode = QUpdateEvent.getInstance().addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.HIGHER);
//        renderEventCode = QRenderEvent.getInstance().addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.HIGHER);

        QBubbles.getEventBus().register(this);
        this.eventActive = true;
    }

    @Override
    public void unbindEvents() throws NoSuchElementException {
//        QUpdateEvent.getInstance().removeListener(tickEventCode);
//        QRenderEvent.getInstance().removeListener(renderEventCode);

        QBubbles.getEventBus().unregister(this);
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
            BubbleEntity bubbleEntity = entityType.create(scene, this);
            if (bubbleEntity.getBubbleType().canSpawn(this)) {
                environment.spawnEntity(entityType);
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
