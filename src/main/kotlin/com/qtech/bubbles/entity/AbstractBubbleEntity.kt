package com.qtech.bubbles.entity

import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.entity.types.EntityType
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.event.CollisionEvent
import com.qtech.bubbles.event.SubscribeEvent
import java.awt.Point

/**
 * <h1>ItemType Entity base class</h1>
 * For entities such as a [bubble][BubbleEntity]
 *
 * @see Entity
 */
abstract class AbstractBubbleEntity  // Constructor
    (type: EntityType<*>, gameMode: AbstractGameMode) : DamageableEntity(type, gameMode) {
    @SubscribeEvent
    override fun onCollision(event: CollisionEvent) {
    }

    override fun onSpawn(pos: Point?, environment: Environment?) {
        damageValue = maxDamageValue
    }

    fun restoreDamage(value: Double) {
        if (damageValue + value > maxDamageValue) {
            damageValue = maxDamageValue
            return
        }
        damageValue += value.toFloat()
    }

    override fun toSimpleString(): String {
        return registryName.toString() + "@(" + Math.round(x) + "," + Math.round(y) + ")"
    }
}