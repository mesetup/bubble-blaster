package com.qsoftware.bubbles.effect;

import com.jhlabs.image.ContrastFilter;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.event.FilterEvent;

public class BlindnessEffect extends Effect<BlindnessEffect> {
    private long startTime;

    public BlindnessEffect() {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {
        // Do nothing.
    }

    @Override
    public void onFilter(EffectInstance effectInstance, FilterEvent evt) {
//        HSBAdjustFilter filter = new HSBAdjustFilter();
//        filter.setHFactor((float) (System.currentTimeMillis() - startTime) / 3000 % 1);
        ContrastFilter filter = new ContrastFilter();
        filter.setContrast(0.25f + 0.25f / (float) effectInstance.getStrength());
        evt.addFilter(filter);

        ContrastFilter filter1 = new ContrastFilter();
        filter1.setBrightness(0.5f / (float) effectInstance.getStrength());
        evt.addFilter(filter1);
    }

    @Override
    public void onStart(EffectInstance effectInstance, Entity entity) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onStop(EffectInstance effectInstance, Entity entity) {
        // Do nothing
    }

    @Override
    protected void updateStrength(EffectInstance effectInstance, int old, int _new) {
        // Do nothing
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }
}
