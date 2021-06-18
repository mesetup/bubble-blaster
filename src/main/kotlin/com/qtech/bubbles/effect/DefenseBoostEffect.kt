package com.qtech.bubbles.effect

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.entity.Entity

class DefenseBoostEffect : Effect() {
    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }

    override fun execute(entity: Entity, effectInstance: EffectInstance) {}
    override val attributeModifiers: AttributeMap
        get() {
            val map = AttributeMap()
            map.set(Attribute.DEFENSE, 1f)
            return map
        }
}