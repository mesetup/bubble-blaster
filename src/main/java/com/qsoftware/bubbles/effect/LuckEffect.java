package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Attribute;

public class LuckEffect extends Effect<LuckEffect> {
    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.set(Attribute.LUCK, 2.0f);
        return map;
    }
}
