package com.qtech.bubbles.effect;

import com.jhlabs.image.ContrastFilter;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.event.FilterEvent;

public class BlindnessEffect extends Effect {
    private long startTime;

    public BlindnessEffect() {
        super();
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

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onStop(Entity entity) {
        // Do nothing
    }

    @Override
    protected void updateStrength() {
        // Do nothing
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    public long getStartTime() {
        return startTime;
    }
}
