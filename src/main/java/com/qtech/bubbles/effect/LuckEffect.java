package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.entity.Entity;

public class LuckEffect extends Effect {
    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.LUCK, 2.0f);
        return map;
    }
}
