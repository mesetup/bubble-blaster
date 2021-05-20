package com.qtech.bubbleblaster.effect;

import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.utilities.python.builtins.ValueError;

public class ParalyzeEffect extends Effect {
    public ParalyzeEffect() throws ValueError {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
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
