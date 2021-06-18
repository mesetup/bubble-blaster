package com.qtech.bubbles.common.random

import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import com.qtech.bubbles.common.BubbleProperties
import com.qtech.bubbles.common.bubble.BubbleSystem.random
import com.qtech.bubbles.common.gametype.AbstractGameMode
import java.awt.geom.Rectangle2D
import javax.annotation.ParametersAreNonnullByDefault

/**
 * Bubble randomizer class.
 *
 * @author Quinten Jungblut
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
class BubbleRandomizer(gameMode: AbstractGameMode) : EntityRandomizer() {
    val rNG = PseudoRandom(4224)
    val typeRng = Rng(rNG, 0, 0)
    val hardnessRng = Rng(rNG, 0, 1)
    val speedRng = Rng(rNG, 0, 2)
    val radiusRnd = Rng(rNG, 0, 3)
    val xRng = Rng(rNG, 0, 4)
    val yRng = Rng(rNG, 0, 5)
    val defenseRng = Rng(rNG, 0, 6)
    val attackRng = Rng(rNG, 0, 7)
    val scoreMultiplierRng = Rng(rNG, 0, 8)

    /**
     * Get a random bubble properties instance.
     *
     * @param bounds   game bounds.
     * @param gameMode game type.
     * @return a random bubble properties instance.
     */
    override fun getRandomProperties(bounds: Rectangle2D, gameMode: AbstractGameMode): BubbleProperties {
        val type = random(typeRng, gameMode)
        val ticks = gameMode.ticks

        // Properties
        val minHardness = type!!.hardness
        val maxHardness = type.hardness
        val minSpeed = type.minSpeed
        val maxSpeed = type.maxSpeed
        val minRad = type.minRadius
        val maxRad = type.maxRadius

        // Randomizing.
//        println("Speed: $minSpeed, $maxSpeed");
        val speed = speedRng.getNumber(minSpeed, maxSpeed, ticks)
//        println("Rad: $minRad, $maxRad");
        val radius = radiusRnd.getNumber(minRad, maxRad, ticks)

//        println("Ticks: $ticks");
//        println("Hardness: $hardness");
//        println("Speed: $speed");
//        println("Radius: $radius");
        check(!(bounds.minX > bounds.maxX || bounds.minY > bounds.maxY)) { "Game bounds is invalid: negative size" }
        check(!(bounds.minX == bounds.maxX || bounds.minY == bounds.maxY)) { "Game bounds is invalid: zero size" }

//        println("X: " + bounds.minX + ", " + bounds.maxX);
        val x = xRng.getNumber(bounds.minX.toInt(), bounds.maxX.toInt(), ticks)
//        println("Y: " + bounds.minY + ", " + bounds.maxY);
        val y = yRng.getNumber(bounds.minY.toInt(), bounds.maxY.toInt(), ticks)

//        println("X: $x")
//        println("Y: $y")
        return BubbleProperties(type, radius.toFloat(), speed, radius + type.colors!!.size * 3 + 4, x, y, type.getDefense(gameMode, defenseRng), type.getAttack(gameMode, attackRng), type.getScore(gameMode, scoreMultiplierRng))
    }

    /**
     * Create bubble randomizer instance.
     *
     * @param gameType the game-type to assign the randomizer to.
     */
    init {
        rNG.seed = gameMode.seed
    }
}