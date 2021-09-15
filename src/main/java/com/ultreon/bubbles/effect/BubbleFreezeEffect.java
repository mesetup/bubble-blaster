package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.commons.utilities.python.builtins.ValueError;

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
