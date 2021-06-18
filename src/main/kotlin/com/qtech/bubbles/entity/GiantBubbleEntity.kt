package com.qtech.bubbles.entity

import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.init.Entities
import java.awt.Point

/**
 * <h1>Bubble Entity.</h1>
 * One of the most important parts of the game.
 *
 * @see AbstractBubbleEntity
 */
class GiantBubbleEntity(gameMode: AbstractGameMode) : BubbleEntity(gameMode) {
    /**
     * <h1>Spawn Event Handler</h1>
     * On-spawn.
     *
     * @param pos         the position for spawn.
     * @param environment the environment to spawn in.
     */
    override fun onSpawn(pos: Point?, environment: Environment?) {
        val randomizer = gameMode.bubbleRandomizer

        // Get random properties
        val properties = randomizer.getRandomProperties(environment!!.gameMode.gameBounds!!, gameMode)

        // Bubble Type
        bubbleType = properties.type

        // Attributes.
        attributeMap.set(Attribute.ATTACK, bubbleType.getAttack(gameMode, gameMode.bubbleRandomizer.attackRng))
        attributeMap.set(Attribute.DEFENSE, bubbleType.getDefense(gameMode, gameMode.bubbleRandomizer.defenseRng))
        attributeMap.set(Attribute.SCORE_MULTIPLIER, bubbleType.getScore(gameMode, gameMode.bubbleRandomizer.scoreMultiplierRng))

        // Dynamic values
        radius = properties.radius * 4 + 80
        speed = (properties.speed / Math.PI).toFloat()
        attributeMap.set(Attribute.SPEED, (properties.speed / Math.PI).toFloat())
        baseRadius = properties.radius * 4 + 80
        attributeMap.set(Attribute.MAX_DAMAGE, properties.damageValue * 4 + 80)
        damageValue = properties.damageValue * 4 + 80

        // Static values.
        bounceAmount = bubbleType.bounceAmount

        // Set velocity
        velocityX = (-baseSpeed)
        bindEvents()
    }

    init {
        this.addCollidable(Entities.PLAYER.get())
    }
}