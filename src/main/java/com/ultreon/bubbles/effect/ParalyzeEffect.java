package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.commons.utilities.python.builtins.ValueError;

public class ParalyzeEffect extends StatusEffect {
    public ParalyzeEffect() throws ValueError {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @Override
    public void onStart(StatusEffectInstance statusEffectInstance, Entity entity) {
        if (entity instanceof PlayerEntity) {
            entity.setMotionEnabled(false);
        }
    }

    @Override
    public void onStop(Entity entity) {
        if (entity instanceof PlayerEntity) {
            entity.setMotionEnabled(true);
        }
    }

    @Override
    protected void updateStrength() {
        // Do nothing.
    }
}
