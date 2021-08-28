package com.qtech.bubbles.environment;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.EntitySpawnData;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.registry.Registers;
import com.qtech.utilities.datetime.DateTime;
import org.bson.BsonDocument;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Environment {
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final AbstractGameType gameType;
    private GameEvent currentGameEvent;
    private Thread gameEventHandlerThread;

    public Environment(AbstractGameType gameType) {
        this.gameType = gameType;
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    /**
     * Spawn entity from loading.
     *
     * @param entityData the data of the entity to spawn.
     */
    public final void spawnEntityFromState(BsonDocument entityData) {
        if (!BubbleBlaster.getInstance().isOnMainThread()) {
            BubbleBlaster.getInstance().runLater(() -> {
                loadAndSpawnEntity(entityData);
            });
            return;
        }
        loadAndSpawnEntity(entityData);
    }

    private void loadAndSpawnEntity(BsonDocument entityData) {
        String type = entityData.getString("Type").getValue();
        EntityType<?> entityType = Registers.ENTITIES.get(ResourceEntry.fromString(type));
        Entity entity = entityType.create(gameType, entityData);
        entity.prepareSpawn(EntitySpawnData.fromLoadSpawn(entityData));
        entity.setState(entityData);

        this.entities.add(entity);
    }

    private void gameEventHandlerThread() {
        while (BubbleBlaster.getInstance().environment == this) {
            if (currentGameEvent != null) {
                if (!currentGameEvent.isActive(DateTime.current())) {
                    currentGameEvent = null;
                }

                continue;
            }
            for (GameEvent gameEvent : Registers.GAME_EVENTS.values()) {
                if (gameEvent.isActive(DateTime.current())) {
                    currentGameEvent = gameEvent;
                    break;
                }
            }
        }
    }

    /**
     * Naturally spawn an entity.
     *
     * @param entityType type of entity to spawn.
     */
    public final void spawn(EntityType<?> entityType) {
        if (!BubbleBlaster.getInstance().isOnMainThread()) {
            BubbleBlaster.getInstance().runLater(() -> {
                Entity entity = entityType.create(gameType);
                Point pos = gameType.getSpawnLocation(entity);
                spawn(entity, pos);
            });
            return;
        }
        Entity entity = entityType.create(gameType);
        Point pos = gameType.getSpawnLocation(entity);
        spawn(entity, pos);
    }

    /**
     * Naturally spawn an entity using a specific position.
     *
     * @param entity entity to spawn.
     * @param pos    spawn location.
     */
    public final void spawn(Entity entity, Point pos) {
        if (!BubbleBlaster.getInstance().isOnMainThread()) {
            BubbleBlaster.getInstance().runLater(() -> {
                entity.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos));
                entity.onSpawn(pos, this);
                this.entities.add(entity);
            });
            return;
        }
        entity.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos));
        entity.onSpawn(pos, this);
        this.entities.add(entity);
    }

    public AbstractGameType getGameType() {
        return gameType;
    }

    public void tick() {
        if (gameType.isInitialized()) {
            this.entities.removeIf(Entity::isMarkedForDeletion);
            for (Entity entity : this.entities) {
                entity.tick(this);
            }
            gameType.tick();
        }
    }

    public void gameOver(PlayerEntity player) {
        entities.remove(player);
    }

    public void joinPlayer(PlayerEntity player) {
        entities.add(player);
    }

    /**
     * <h1>Safe Remove Entity</h1>
     * Safely removes a entity. Removes the entity from the game-objects set, then removes it from
     * the gameObjects in {@link Environment#getEntities()}  GameScene}
     *
     * @param entity The entity to remove.
     * @deprecated Use {@link Entity#delete()} instead.
     */
    @Deprecated
    public void removeEntity(Entity entity) {
        if (!BubbleBlaster.getInstance().isOnMainThread()) {
            BubbleBlaster.getInstance().runLater(entity::delete);
        }
        entity.delete();
    }

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> bubbleEntityClass) {
        return entities.stream()
                .filter((entity) -> bubbleEntityClass.isAssignableFrom(entity.getClass()))
                .map(bubbleEntityClass::cast).toList();
    }

    public GameEvent getCurrentGameEvent() {
        return currentGameEvent;
    }

    public void start() {
        this.gameEventHandlerThread = new Thread(this::gameEventHandlerThread, "GameEventHandler");
        this.gameEventHandlerThread.start();
    }

    @Deprecated
    public void quit() {
        this.gameEventHandlerThread.interrupt();
        this.gameType.quit();
    }

    public void setCurrentGameEvent(GameEvent currentGameEvent) {
        this.currentGameEvent = currentGameEvent;
//        this.currentGameEvent.activate();
    }
}
