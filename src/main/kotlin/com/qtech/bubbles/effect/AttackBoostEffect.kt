package com.qtech.bubbles.effect

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.entity.Entity

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