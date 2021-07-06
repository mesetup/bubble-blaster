package qtech.bubbles.effect

import qtech.bubbles.common.AttributeMap
import qtech.bubbles.common.effect.Effect
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.entity.Entity

class SpeedBoostEffect : Effect() {
    override val attributeModifiers: AttributeMap
        get() {
            val attributeMap = AttributeMap()
            attributeMap.set(Attribute.SPEED, 1f)
            return attributeMap
        }

    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }
}