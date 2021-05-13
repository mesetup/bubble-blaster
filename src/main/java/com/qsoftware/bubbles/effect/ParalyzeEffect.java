package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.utilities.python.builtins.ValueError;

public class ParalyzeEffect extends Effect<ParalyzeEffect> {
    public ParalyzeEffect() throws ValueError {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {
        // Do nothing.
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
        if (entity instanceof PlayerEntity) {
            entity.setMotionEnabled(false);
        }
    }

    @Override
    public void onStop(EffectInstance effectInstance, Entity entity) {
        if (entity instanceof PlayerEntity) {
            entity.setMotionEnabled(true);
        }
    }

    @Override
    protected void updateStrength(EffectInstance effectInstance, int old, int _new) {
        // Do nothing.
    }
}
