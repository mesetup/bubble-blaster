package com.qtech.bubbles.effect

import com.jhlabs.image.HSBAdjustFilter
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.DamageSourceType
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.entity.EntityDamageSource
import com.qtech.bubbles.event.FilterEvent

class PoisonEffect : Effect() {
    override fun onFilter(effectInstance: EffectInstance, evt: FilterEvent) {
        val filter = HSBAdjustFilter()
        filter.setHFactor((System.currentTimeMillis() - effectInstance.startTime).toFloat() / 3000 % 1)
        evt.addFilter(filter)
    }

    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return System.currentTimeMillis() >= effectInstance.tag.getLong("nextDamage")
    }

    override fun execute(entity: Entity, effectInstance: EffectInstance) {
        entity.gameMode.attack(entity, effectInstance.strength.toDouble() / 2, EntityDamageSource(null, DamageSourceType.POISON))
        val tag = effectInstance.tag
        val nextDamage = tag.getLong("nextDamage")
        tag.putLong("nextDamage", nextDamage + 2000)
    }

    override fun onStart(effectInstance: EffectInstance, entity: Entity) {
        val tag = effectInstance.tag
        tag.putLong("nextDamage", System.currentTimeMillis() + 2000)
        tag.putLong("startTime", System.currentTimeMillis())
    }

    override fun onStop(entity: Entity?) {
        // Do nothing
    }

    override fun updateStrength() {
        // Do nothing
    }
}