package com.qtech.bubbles.effect

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.entity.Entity

class LuckEffect : Effect() {
    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }

    override val attributeModifiers: AttributeMap
        get() {
            val map = AttributeMap()
            map.set(Attribute.LUCK, 2.0f)
            return map
        }
}