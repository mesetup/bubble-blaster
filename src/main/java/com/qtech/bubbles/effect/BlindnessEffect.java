package com.qtech.bubbles.effect;

import com.jhlabs.image.ContrastFilter;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.event.FilterEvent;

public class BlindnessEffect extends StatusEffect {
    private long startTime;

    public BlindnessEffect() {
        super();
    }

    @Override
    public void onFilter(StatusEffectInstance statusEffectInstance, FilterEvent evt) {
//        HSBAdjustFilter filter = new HSBAdjustFilter();
//        filter.setHFactor((float) (System.currentTimeMillis() - startTime) / 3000 % 1);
        ContrastFilter filter = new ContrastFilter();
        filter.setContrast(0.25f + 0.25f / (float) statusEffectInstance.getStrength());
        evt.addFilter(filter);

        ContrastFilter filter1 = new ContrastFilter();
        filter1.setBrightness(0.5f / (float) statusEffectInstance.getStrength());
        evt.addFilter(filter1);
    }

    @Override
    public void onStart(StatusEffectInstance statusEffectInstance, Entity entity) {
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
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @SuppressWarnings("unused")
    public long getStartTime() {
        return startTime;
    }
}
