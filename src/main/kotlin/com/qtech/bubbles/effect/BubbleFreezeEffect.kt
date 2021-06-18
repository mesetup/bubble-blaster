package com.qtech.bubbles.effect

import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.entity.player.PlayerEntity

class BubbleFreezeEffect : Effect() {
    override fun canExecute(entity: Entity, effectInstance: EffectInstance): Boolean {
        return false
    }

    override fun onStart(effectInstance: EffectInstance, entity: Entity) {
        if (entity is PlayerEntity) {
            if (!entity.gameMode.isGlobalBubbleFreeze) {
                entity.gameMode.isGlobalBubbleFreeze = true
            }
        }
    }

    override fun onStop(entity: Entity?) {
        if (entity is PlayerEntity) {
            if (entity.gameMode.isGlobalBubbleFreeze) {
                entity.gameMode.isGlobalBubbleFreeze = false
            }
        }
    }
}