package qtech.bubbles.effect

import qtech.bubbles.common.AttributeMap
import qtech.bubbles.common.effect.Effect
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.entity.Entity

class AttackBoostEffect : Effect() {
    override fun execute(entity: Entity, effectInstance: EffectInstance) {}
    override fun updateStrength() {}
    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }

    override val attributeModifiers: AttributeMap
        get() {
            val attributes = AttributeMap()
            attributes.set(Attribute.ATTACK, 1f)
            return attributes
        }
}