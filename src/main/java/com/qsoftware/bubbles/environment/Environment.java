package com.qsoftware.bubbles.environment;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.entity.*;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.utils.categories.ShapeUtils;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.event.CollisionEvent;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.scene.GameScene;
import org.bson.BsonDocument;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Environment {
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final AbstractGameType gameType;

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

    /**
     * Naturally spawn an entity.
     *
     * @param entityType type of entity to spawn.
     */
    public final void spawnEntity(EntityType<?> entityType) {
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
                Entity entity = entityType.create(gameType.getScene(), gameType);
                Point pos = gameType.getSpawnLocation(entity);
                spawnEntity(entity, pos);
            });
            return;
        }
        Entity entity = entityType.create(gameType.getScene(), gameType);
        Point pos = gameType.getSpawnLocation(entity);
        spawnEntity(entity, pos);
    }

    /**
     * Naturally spawn an entity using a specific position.
     *
     * @param entity entity to spawn.
     * @param pos    spawn location.
     */
    public final void spawnEntity(Entity entity, Point pos) {
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
                entity.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos));
                entity.onSpawn(pos);
                this.entities.add(entity);
            });
            return;
        }
        entity.prepareSpawn(EntitySpawnData.fromNaturalSpawn(pos));
        entity.onSpawn(pos);
        this.entities.add(entity);
    }

    public AbstractGameType getGameType() {
        return gameType;
    }

    public void tick() {
        if (gameType.isInitialized()) {
            List<Entity> entityList = this.entities;
            for (int i = 0; i < entityList.size(); i++) {
                Entity entity = entityList.get(i);
                entity.tick(this);

                @Nullable Scene scene = QBubbles.getInstance().getSceneManager().getCurrentScene();

                if (scene instanceof GameScene) {
                    GameScene gameScene = (GameScene) scene;

                    if (!gameScene.isPaused()) {
                        for (int j = i; j < entities.size(); j++) {
                            Entity target = entities.get(j);
                            try {
                                // Check is collisionable with each other
                                if (entity.isCollidingWith(target)) {

                                    // Check intersection.
                                    if (ShapeUtils.checkIntersection(entity.getShape(), target.getShape())) {

                                        // Handling collision by posting collision event, and let the intersected entities attack each other.
                                        QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, entity, target));
                                        QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, target, entity));

                                        gameType.attack(target, entity.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(entity, DamageSourceType.COLLISION));
                                        gameType.attack(entity, target.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(target, DamageSourceType.COLLISION));
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                QBubbles.getLogger().info("Array index was out of bounds! Check check double check!");
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
     * <h1>Safe Remove Bubble</h1>
     * Safely removes a entity. Removes the entity from the game-objects set, then removes it from
     * the gameObjects in {@link AbstractGameType#getGameObjects()}  GameScene}
     *
     * @param entity The entity to remove.
     */
    public void removeEntity(Entity entity) {
        if (!QBubbles.getInstance().isOnMainThread()) {
            QBubbles.getInstance().runLater(() -> {
                entity.delete();
                entities.remove(entity);
            });
        }
        entity.delete();
        entities.remove(entity);

    }

    public <T extends Entity> Collection<Object> getEntitiesByClass(Class<T> bubbleEntityClass) {
        return Collections.unmodifiableList(entities.stream()
                .filter((entity) -> bubbleEntityClass.isAssignableFrom(entity.getClass()))
                .collect(Collectors.toList()));
    }
}
