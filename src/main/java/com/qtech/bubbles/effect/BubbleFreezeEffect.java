package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.utilities.python.builtins.ValueError;

public class BubbleFreezeEffect extends Effect<BubbleFreezeEffect> {
    public BubbleFreezeEffect() throws ValueError {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {

    }

    @Override
    protected boolean canExecute() {
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
