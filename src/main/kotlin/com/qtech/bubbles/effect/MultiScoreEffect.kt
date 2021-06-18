package com.qtech.bubbles.effect

import com.qtech.bubbles.common.AttributeMap
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.Attribute
import com.qtech.bubbles.entity.Entity

class MultiScoreEffect : Effect() {
    override val attributeModifiers: AttributeMap
        get() {
            val attributeMap = AttributeMap()
            attributeMap.set(Attribute.SCORE_MULTIPLIER, 1f)
            return attributeMap
        }

    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }
}