package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.utilities.python.builtins.ValueError;

public class BubbleFreezeEffect extends Effect {
    public BubbleFreezeEffect() throws ValueError {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
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
