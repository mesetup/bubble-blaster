package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.utilities.python.builtins.ValueError;

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
        attributeMap.setBase(Attribute.SCORE_MULTIPLIER, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute() {
        return false;
    }
}
