package com.qtech.bubbles.effect

import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.entity.player.PlayerEntity

class ParalyzeEffect : Effect() {
    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }

    override fun onStart(effectInstance: EffectInstance, entity: Entity) {
        if (entity is PlayerEntity) {
            entity.isMotionEnabled = false
        }
    }

    override fun onStop(entity: Entity?) {
        if (entity is PlayerEntity) {
            entity.isMotionEnabled = true
        }
    }

    override fun updateStrength() {
        // Do nothing.
    }
}