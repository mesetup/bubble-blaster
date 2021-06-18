@file:Suppress("unused")

package com.qtech.bubbles.common

import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.random.Rng
import com.qtech.bubbles.common.random.BubbleRandomizer
import java.awt.Point
import java.awt.geom.Point2D

/**
 * Bubble properties, used for [AbstractGameMode] objects / classes, and used by [BubbleRandomizer] for returning the randomized bubble properties.
 *
 * @author Quinten Jungblut
 * @see BubbleRandomizer
 */
class BubbleProperties : EntityProperties {
    /**
     * @return the bubble hardness.
     */
    val damageValue: Float

    /**
     * @return the bubble speed.
     */
    val speed: Float

    /**
     * @return the bubble radius.
     */
    val radius: Int

    /**
     * @return the bubble defense value.
     */
    val defense: Float

    /**
     * @return the bubble attack value.
     */
    val attack: Float

    /**
     * @return the bubble score multiplier.
     */
    val scoreMultiplier: Float

    /**
     * @return the [bubble type][AbstractBubble].
     */
    val type: AbstractBubble

    /**
     * Bubble properties: Constructor.
     *
     * @param type        the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed       the bubble speed.
     * @param radius      the bubble radius.
     * @param x           the bubble x coordinate.
     * @param y           the bubble y coordinate.
     */
    constructor(type: AbstractBubble, damageValue: Float, speed: Double, radius: Int, x: Int, y: Int) : super(x, y) {

        // Type.
        this.type = type

        // Values.
        this.damageValue = damageValue
        this.speed = speed.toFloat()
        this.radius = radius

        // Attributes.
        defense = type.defense
        attack = type.attack
        scoreMultiplier = type.score
    }

    /**
     * Bubble properties: Constructor.
     *
     * @param type        the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed       the bubble speed.
     * @param radius      the bubble radius.
     * @param x           the bubble x coordinate.
     * @param y           the bubble y coordinate.
     * @param gameMode    the game-type where the randomizing would be used.
     * @param rng         the RNG for the bubble to generate unsolved values.
     */
    constructor(type: AbstractBubble, damageValue: Float, speed: Double, radius: Int, x: Int, y: Int, gameMode: AbstractGameMode?, rng: Rng?) : super(x, y) {

        // Type.
        this.type = type

        // Values.
        this.damageValue = damageValue
        this.speed = speed.toFloat()
        this.radius = radius

        // Attributes.
        defense = type.getDefense(gameMode!!, rng!!)
        attack = type.getAttack(gameMode, rng)
        scoreMultiplier = type.getScore(gameMode, rng)
    }

    /**
     * Bubble properties: Constructor/
     *
     * @param type        the bubble type.
     * @param damageValue the bubble hardness.
     * @param speed       the bubble speed.
     * @param radius      the bubble radius.
     * @param x           the bubble x coordinate.
     * @param y           the bubble y coordinate.
     * @param defense     the bubble defense value.
     * @param attack      the bubble attack value.
     * @param score       the bubble score value.
     */
    constructor(type: AbstractBubble, damageValue: Float, speed: Double, radius: Int, x: Int, y: Int, defense: Float, attack: Float, score: Float) : super(x, y) {

        // Type.
        this.type = type

        // Values.
        this.damageValue = damageValue
        this.speed = speed.toFloat()
        this.radius = radius

        // Attributes.
        this.defense = defense
        this.attack = attack
        scoreMultiplier = score
    }

    /**
     * @return the bubble [position (AWT Point)][Point].
     * @see Point
     */
    val point: Point
        get() = Point(x, y)

    /**
     * @return the bubble [position (AWT 2D Point [Float])][Point2D.Float].
     * @see Point2D.Float
     */
    val point2D: Point2D.Float
        get() = Point2D.Float(x.toFloat(), y.toFloat())

    /**
     * @return the bubble [position][Point2D.Double].
     * @see Point2D.Double
     */
    val position: Point2D.Double
        get() = Point2D.Double(x.toDouble(), y.toDouble())
}