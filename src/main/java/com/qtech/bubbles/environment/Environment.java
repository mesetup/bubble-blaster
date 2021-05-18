package com.qtech.bubbles.environment;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.entity.*;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.utils.categories.ShapeUtils;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.registry.Registers;
import com.qtech.utilities.datetime.DateTime;
import org.bson.BsonDocument;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
                String type = entityData.getString("Type").getValue();
                EntityType<?> entityType = Registers.ENTITIES.get(ResourceLocation.fromString(type));
                Entity entity = entityType.create(gameType.getScene(), gameType, entityData);
                entity.prepareSpawn(EntitySpawnData.fromLoadSpawn(entityData));
                entity.setState(entityData);

                this.entities.add(entity);
            });
            return;
        }
        String type = entityData.getString("Type").getValue();
        EntityType<?> entityType = Registers.ENTITIES.get(ResourceLocation.fromString(type));
        Entity entity = entityType.create(gameType.getScene(), gameType, entityData);
        entity.prepareSpawn(EntitySpawnData.fromLoadSpawn(entityData));
        entity.setState(entityData);

        this.entities.add(entity);
    }

    private void gameEventHandlerThread() {
        while (QBubbles.getInstance().environment == this) {
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
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
                Entity entity = entityType.create(gameType.getScene(), gameType);
                Point pos = gameType.getSpawnLocation(entity);
                spawn(entity, pos);
            });
            return;
        }
        Entity entity = entityType.create(gameType.getScene(), gameType);
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
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
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
            List<Entity> entityList = this.entities;
            for (int i = 0; i < entityList.size(); i++) {
                Entity entity = entityList.get(i);
                entity.tick(this);
                Screen scene = QBubbles.getInstance().getCurrentScene();

                if (QBubbles.getInstance().isGameLoaded() && (scene == null || scene.doesPauseGame())) {
                    for (int j = i + 1; j < entities.size(); j++) {
                        Entity target = entities.get(j);
                        // Check is collisionable with each other
                        if (entity.isCollidingWith(target)) {
                            // Check intersection.
                            if (ShapeUtils.checkIntersection(entity.getShape(), target.getShape())) {
                                // Handling collision by posting collision event, and let the intersected entities attack each other.
                                QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), 1, entity, target));
                                QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), 1, target, entity));

                                gameType.attack(target, entity.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(entity, DamageSourceType.COLLISION));
                                gameType.attack(entity, target.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(target, DamageSourceType.COLLISION));
                            }
                        }
                    }
                }
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
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(entity::delete);
        }
        entity.delete();
    }

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> bubbleEntityClass) {
        return entities.stream()
                .filter((entity) -> bubbleEntityClass.isAssignableFrom(entity.getClass()))
                .map(bubbleEntityClass::cast)
                .collect(Collectors.toUnmodifiableList());
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
    }
}