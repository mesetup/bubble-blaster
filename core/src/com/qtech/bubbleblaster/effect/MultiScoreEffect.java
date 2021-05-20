package com.qtech.bubbleblaster.effect;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Attribute;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.utilities.python.builtins.ValueError;

public class MultiScoreEffect extends Effect {
    public MultiScoreEffect() throws ValueError {
        super();
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setBase(Attribute.SCORE_MULTIPLIER, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }
}
