package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.attribute.Attribute;
import com.qtech.utilities.python.builtins.ValueError;

public class MultiScoreEffect extends StatusEffect {
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
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }
}
