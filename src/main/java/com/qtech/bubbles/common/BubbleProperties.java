package com.qtech.bubbles.common;

import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.random.BubbleRandomizer;
import com.qtech.bubbles.common.random.Rng;
import com.qtech.bubbles.util.position.AbsolutePosition;
import com.qtech.bubbles.util.position.Position;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Bubble properties, used for {@link AbstractGameType} objects / classes, and used by {@link BubbleRandomizer} for returning the randomized bubble properties.
 *
 * @author Quinten Jungblut
 * @see BubbleRandomizer
 */
@SuppressWarnings("unused")
public class BubbleProperties extends EntityProperties {
    private final float damageValue;
    private final double speed;
    private final int radius;
    private final float defense;
    private final float attack;
    private final float score;
    private final AbstractBubble type;

    /**
     * Bubble properties: Constructor.
     *
     * @param type     the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed    the bubble speed.
     * @param radius   the bubble radius.
     * @param x        the bubble x coordinate.
     * @param y        the bubble y coordinate.
     * @param gameType the game-type.
     */
    public BubbleProperties(AbstractBubble type, float damageValue, double speed, int radius, int x, int y, AbstractGameType gameType) {
        super(x, y);

        // Type.
        this.type = type;

        // Values.
        this.damageValue = damageValue;
        this.speed = speed;
        this.radius = radius;

        // Attributes.
        this.defense = type.getDefense();
        this.attack = type.getAttack();
        this.score = type.getScore();
    }

    /**
     * Bubble properties: Constructor.
     *  @param type     the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed    the bubble speed.
     * @param radius   the bubble radius.
     * @param x        the bubble x coordinate.
     * @param y        the bubble y coordinate.
     * @param gameType the game-type where the randomizing would be used.
     * @param rng      the RNG for the bubble to generate unsolved values.
     */
    public BubbleProperties(AbstractBubble type, float damageValue, double speed, int radius, int x, int y, AbstractGameType gameType, Rng rng) {
        super(x, y);

        // Type.
        this.type = type;

        // Values.
        this.damageValue = damageValue;
        this.speed = speed;
        this.radius = radius;

        // Attributes.
        this.defense = type.getDefense(gameType, rng);
        this.attack = type.getAttack(gameType, rng);
        this.score = type.getScore(gameType, rng);
    }

    /**
     * Bubble properties: Constructor/
     *  @param type     the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed    the bubble speed.
     * @param radius   the bubble radius.
     * @param x        the bubble x coordinate.
     * @param y        the bubble y coordinate.
     * @param defense  the bubble defense value.
     * @param attack   the bubble attack value.
     * @param score    the bubble score value.
     */
    public BubbleProperties(AbstractBubble type, float damageValue, double speed, int radius, int x, int y, float defense, float attack, float score) {
        super(x, y);

        // Type.
        this.type = type;

        // Values.
        this.damageValue = damageValue;
        this.speed = speed;
        this.radius = radius;

        // Attributes.
        this.defense = defense;
        this.attack = attack;
        this.score = score;
    }

    /**
     * @return the {@link AbstractBubble bubble type}.
     */
    public AbstractBubble getType() {
        return type;
    }

    /**
     * @return the bubble speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return the bubble radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return the bubble {@link Point position (AWT Point)}.
     * @see Point
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * @return the bubble {@link Point2D.Float position (AWT 2D Point [float])}.
     * @see Point2D.Float
     */
    public Point2D.Float getPoint2D() {
        return new Point2D.Float(x, y);
    }

    /**
     * @return the bubble {@link Position position}.
     * @see Position
     */
    public Position getPosition() {
        return new AbsolutePosition(x, y);
    }

    /**
     * @return the bubble hardness.
     */
    public float getDamageValue() {
        return damageValue;
    }

    /**
     * @return the bubble defense value.
     */
    public float getDefense() {
        return defense;
    }

    /**
     * @return the bubble attack value.
     */
    public float getAttack() {
        return attack;
    }

    /**
     * @return the bubble score multiplier.
     */
    public float getScoreMultiplier() {
        return score;
    }
}
