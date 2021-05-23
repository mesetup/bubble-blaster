package com.qtech.bubbles.common.random;

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.BubbleProperties;
import com.qtech.bubbles.common.bubble.BubbleSystem;
import com.qtech.bubbles.common.gametype.AbstractGameType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.geom.Rectangle2D;

/**
 * Bubble randomizer class.
 *
 * @author Quinten Jungblut
 */
@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class BubbleRandomizer extends EntityRandomizer {
    private final PseudoRandom rng = new PseudoRandom(4224);
    private final Rng typeRng = new Rng(rng, 0, 0);
    private final Rng hardnessRng = new Rng(rng, 0, 1);
    private final Rng speedRng = new Rng(rng, 0, 2);
    private final Rng radiusRng = new Rng(rng, 0, 3);
    private final Rng xRng = new Rng(rng, 0, 4);
    private final Rng yRng = new Rng(rng, 0, 5);
    private final Rng defenseRng = new Rng(rng, 0, 6);
    private final Rng attackRng = new Rng(rng, 0, 7);
    private final Rng scoreMultiplierRng = new Rng(rng, 0, 8);

    /**
     * Create bubble randomizer instance.
     *
     * @param gameType the game-type to assign the randomizer to.
     */
    public BubbleRandomizer(AbstractGameType gameType) {
        rng.setSeed(gameType.getSeed());
    }

    /**
     * Get a random bubble properties instance.
     *
     * @param bounds   game bounds.
     * @param gameType game type.
     * @return a random bubble properties instance.
     */
    @Override
    public BubbleProperties getRandomProperties(Rectangle2D bounds, AbstractGameType gameType) {
        AbstractBubble type = BubbleSystem.random(typeRng, gameType);
        long ticks = gameType.getTicks();

        // Properties
        double minHardness = type.getHardness();
        double maxHardness = type.getHardness();
        double minSpeed = type.getMinSpeed();
        double maxSpeed = type.getMaxSpeed();
        int minRad = type.getMinRadius();
        int maxRad = type.getMaxRadius();

        // Randomizing.
//        System.out.println("Hardness: " + minHardness + ", " + maxHardness);
        double hardness = hardnessRng.getNumber(minHardness, maxHardness, ticks);
//        System.out.println("Speed: " + minSpeed + ", " + maxSpeed);
        double speed = speedRng.getNumber(minSpeed, maxSpeed, ticks);
//        System.out.println("Rad: " + minRad + ", " + maxRad);
        int radius = radiusRng.getNumber(minRad, maxRad, ticks);

//        System.out.println("Ticks: " + ticks);
//        System.out.println("Hardness: " + hardness);
//        System.out.println("Speed: " + speed);
//        System.out.println("Radius: " + radius);

        if (bounds.getMinX() > bounds.getMaxX() || bounds.getMinY() > bounds.getMaxY()) {
            throw new IllegalStateException("Game bounds is invalid: negative size");
        }

        if (bounds.getMinX() == bounds.getMaxX() || bounds.getMinY() == bounds.getMaxY()) {
            throw new IllegalStateException("Game bounds is invalid: zero size");
        }

//        System.out.println("X: " + bounds.getMinX() + ", " + bounds.getMaxX());
        int x = xRng.getNumber((int) bounds.getMinX(), (int) bounds.getMaxX(), ticks);
//        System.out.println("Y: " + bounds.getMinY() + ", " + bounds.getMaxY());
        int y = yRng.getNumber((int) bounds.getMinY(), (int) bounds.getMaxY(), ticks);

//        System.out.println("X: " +  x);
//        System.out.println("Y: " +  y);

        return new BubbleProperties(type, radius, speed, radius + (type.colors.length * 3) + 4, x, y, type.getDefense(gameType, defenseRng), type.getAttack(gameType, attackRng), type.getScore(gameType, scoreMultiplierRng));
    }

    public PseudoRandom getRNG() {
        return rng;
    }

    public Rng getHardnessRng() {
        return hardnessRng;
    }

    public Rng getSpeedRng() {
        return speedRng;
    }

    public Rng getRadiusRnd() {
        return radiusRng;
    }

    public Rng getXRng() {
        return xRng;
    }

    public Rng getYRng() {
        return yRng;
    }

    public Rng getDefenseRng() {
        return defenseRng;
    }

    public Rng getAttackRng() {
        return attackRng;
    }

    public Rng getScoreMultiplierRng() {
        return scoreMultiplierRng;
    }

    public Rng getTypeRng() {
        return typeRng;
    }
}
