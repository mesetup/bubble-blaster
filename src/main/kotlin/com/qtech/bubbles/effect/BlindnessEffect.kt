package com.qtech.bubbles.effect

import com.jhlabs.image.ContrastFilter
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.event.FilterEvent

class BlindnessEffect : Effect() {
    var startTime: Long = 0
        private set

    override fun onFilter(effectInstance: EffectInstance, evt: FilterEvent) {
//        HSBAdjustFilter filter = new HSBAdjustFilter();
//        filter.setHFactor((float) (System.currentTimeMillis() - startTime) / 3000 % 1);
        val filter = ContrastFilter()
        filter.contrast = 0.25f + 0.25f / effectInstance.strength.toFloat()
        evt.addFilter(filter)
        val filter1 = ContrastFilter()
        filter1.brightness = 0.5f / effectInstance.strength.toFloat()
        evt.addFilter(filter1)
    }

    override fun onStart(effectInstance: EffectInstance, entity: Entity) {
        startTime = System.currentTimeMillis()
    }

    override fun onStop(entity: Entity?) {
        // Do nothing
    }

    override fun updateStrength() {
        // Do nothing
    }

    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }
}