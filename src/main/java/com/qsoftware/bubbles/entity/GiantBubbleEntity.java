package com.qsoftware.bubbles.entity;

import com.qsoftware.bubbles.common.BubbleProperties;
import com.qsoftware.bubbles.common.entity.AbstractBubbleEntity;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.random.BubbleRandomizer;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.init.EntityInit;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;

import java.awt.*;

/**
 * <h1>Bubble Entity.</h1>
 * One of the most important parts of the game.
 *
 * @see AbstractBubbleEntity
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class GiantBubbleEntity extends BubbleEntity {
    // Entity type.
    private static final EntityType<BubbleEntity> entityType = EntityInit.BUBBLE.get();

    public GiantBubbleEntity(Scene scene, AbstractGameType gameType) {
        super(scene, gameType);
        this.addCollidable(EntityInit.PLAYER.get());
    }

    /**
     * <h1>Spawn Event Handler</h1>
     * On-spawn.
     *
     * @param pos the position for spawn.
     */
    @Override
    public void onSpawn(Point pos) {
        BubbleRandomizer randomizer = this.gameType.getBubbleRandomizer();

        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (!(currentScene instanceof GameScene)) return;
        GameScene gameScene = (GameScene) currentScene;

        // Get random properties
        BubbleProperties properties = randomizer.getRandomProperties(gameScene.getGameType().getGameBounds(), gameType);

        // Bubble Type
        this.bubbleType = properties.getType();

        // Attributes.
        this.attributes.set(Attribute.ATTACK, bubbleType.getAttack(gameType, gameType.getBubbleRandomizer().getAttackRng()));
        this.attributes.set(Attribute.DEFENSE, bubbleType.getDefense(gameType, gameType.getBubbleRandomizer().getDefenseRng()));
        this.attributes.set(Attribute.SCORE_MULTIPLIER, bubbleType.getScore(gameType, gameType.getBubbleRandomizer().getScoreMultiplierRng()));

        // Dynamic values
        this.radius = properties.getRadius() * 4 + 80;
        this.speed = properties.getSpeed() / (Math.PI);
        this.baseSpeed = properties.getSpeed() / (Math.PI);
        this.baseRadius = properties.getRadius() * 4 + 80;
        this.maxHardness = properties.getHardness() * 4 + 80;
        this.hardness = properties.getHardness() * 4 + 80;

        // Static values.
        this.bounceAmount = bubbleType.getBounceAmount();

        // Set velocity
        this.velX = -baseSpeed;

        bindEvents();
    }
}
