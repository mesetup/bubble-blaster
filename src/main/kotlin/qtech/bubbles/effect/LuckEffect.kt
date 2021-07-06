package qtech.bubbles.effect

import qtech.bubbles.common.AttributeMap
import qtech.bubbles.common.effect.Effect
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.entity.Entity

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