package qtech.bubbles.ability

import qtech.bubbles.ability.triggers.AbilityKeyTrigger
import qtech.bubbles.ability.triggers.types.AbilityKeyTriggerType
import qtech.bubbles.common.ability.Ability
import qtech.bubbles.common.ability.AbilityTrigger
import qtech.bubbles.common.ability.AbilityTriggerType
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.init.Abilities
import qtech.bubbles.util.helpers.MathHelper.clamp
import java.awt.event.KeyEvent
import java.awt.geom.Point2D
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class TeleportAbility : Ability<TeleportAbility>(Objects.requireNonNull(Abilities.TELEPORT_ABILITY).get()) {
    override val triggerKey: Int
        get() = KeyEvent.VK_SHIFT
    override val keyTriggerType: AbilityKeyTriggerType
        get() = AbilityKeyTriggerType.HOLD

    override fun trigger(trigger: AbilityTrigger?) {
        val entity = trigger!!.entity
        if (entity is PlayerEntity) {
            val startTime = entity.getTag<Any>().getNumber("TeleportAbilityStartTime").toLong()
            entity.getTag<Any>().remove("TeleportAbilityStartTime")
            var deltaTime = System.currentTimeMillis() - startTime
            deltaTime = clamp(deltaTime, 0, 2500) // 0 to 2.5 seconds.
            val deltaMotion = (deltaTime.toDouble() / 100).pow(2.0)

            // Calculate Velocity X and Y.
            val angelRadians = Math.toRadians(entity.rotation)
            val tempVelX = cos(angelRadians) * deltaMotion
            val tempVelY = sin(angelRadians) * deltaMotion
            val pos: Point2D = Point2D.Double(entity.x + tempVelX, entity.y + tempVelY)
            entity.teleport(pos)
            subtractValue(deltaTime.toInt())
            cooldown = (deltaTime / 3).toInt()
        }
    }

    override fun onKeyTrigger(trigger: AbilityKeyTrigger?) {
        val entity = trigger!!.entity
        if (entity is PlayerEntity) {
            entity.getTag<Any>().putLong("teleportAbilityStartTime", System.currentTimeMillis())
        }
    }

    override fun triggerEntity() {}
    override fun isTriggerable(entity: Entity?): Boolean {
        return entity is PlayerEntity
    }

    override val isRegeneratable: Boolean
        get() = true
    override val triggerType: AbilityTriggerType
        get() = AbilityTriggerType.KEY_TRIGGER
}