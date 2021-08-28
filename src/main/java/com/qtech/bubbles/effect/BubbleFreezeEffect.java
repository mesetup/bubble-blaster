package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.utilities.python.builtins.ValueError;

public class BubbleFreezeEffect extends StatusEffect {
    public BubbleFreezeEffect() throws ValueError {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @Override
    public void onStart(StatusEffectInstance statusEffectInstance, Entity entity) {
        if (entity instanceof PlayerEntity) {
            if (!entity.getGameType().isGlobalBubbleFreeze()) {
                entity.getGameType().setGlobalBubbleFreeze(true);
            }
        }
    }

    @Override
    public void onStop(Entity entity) {
        if (entity instanceof PlayerEntity) {
            if (entity.getGameType().isGlobalBubbleFreeze()) {
                entity.getGameType().setGlobalBubbleFreeze(false);
            }
        }
    }
}
