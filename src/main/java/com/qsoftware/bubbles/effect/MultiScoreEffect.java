package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.utilities.python.builtins.ValueError;

public class MultiScoreEffect extends Effect<MultiScoreEffect> {
    public MultiScoreEffect() throws ValueError {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {
        // Do nothing.
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.set(Attribute.SCORE_MULTIPLIER, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }
}
