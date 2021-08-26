package com.qtech.bubbles.entity;

import com.qtech.bubbles.common.BubbleProperties;
import com.qtech.bubbles.common.entity.AbstractBubbleEntity;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.random.BubbleRandomizer;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.init.Entities;

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
    private static final EntityType<BubbleEntity> entityType = Entities.BUBBLE.get();

    public GiantBubbleEntity(AbstractGameType gameType) {
        super(gameType);
        this.addCollidable(Entities.PLAYER.get());
    }

    /**
     * <h1>Spawn Event Handler</h1>
     * On-spawn.
     *
     * @param pos         the position for spawn.
     * @param environment the environment to spawn in.
     */
    @Override
    public void onSpawn(Point pos, Environment environment) {
        BubbleRandomizer randomizer = this.gameType.getBubbleRandomizer();

        // Get random properties
        BubbleProperties properties = randomizer.getRandomProperties(environment.getGameType().getGameBounds(), gameType);

        // Bubble Type
        this.bubbleType = properties.getType();

        // Attributes.
        this.attributes.setBase(Attribute.ATTACK, bubbleType.getAttack(gameType, gameType.getBubbleRandomizer().getAttackRng()));
        this.attributes.setBase(Attribute.DEFENSE, bubbleType.getDefense(gameType, gameType.getBubbleRandomizer().getDefenseRng()));
        this.attributes.setBase(Attribute.SCORE_MULTIPLIER, bubbleType.getScore(gameType, gameType.getBubbleRandomizer().getScoreMultiplierRng()));

        // Dynamic values
        this.radius = properties.getRadius() * 4 + 80;
        this.speed = properties.getSpeed() / (Math.PI);
        this.attributes.setBase(Attribute.SPEED, (float) (properties.getSpeed() / (Math.PI)));
        this.baseRadius = properties.getRadius() * 4 + 80;
        this.attributes.setBase(Attribute.MAX_DAMAGE, properties.getDamageValue() * 4 + 80);
        this.damageValue = properties.getDamageValue() * 4 + 80;

        // Static values.
        this.bounceAmount = bubbleType.getBounceAmount();

        // Set velocity
        this.velX = -baseSpeed;

        bindEvents();
    }
}
