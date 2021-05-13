package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.utilities.python.builtins.ValueError;

public class BubbleFreezeEffect extends Effect<BubbleFreezeEffect> {
    public BubbleFreezeEffect() throws ValueError {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {

    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
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
    public void onStop(EffectInstance effectInstance, Entity entity) {
        if (entity instanceof PlayerEntity) {
            if (entity.getGameType().isGlobalBubbleFreeze()) {
                entity.getGameType().setGlobalBubbleFreeze(false);
            }
        }
    }
}
