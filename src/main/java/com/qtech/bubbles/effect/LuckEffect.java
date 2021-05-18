package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.entity.Attribute;

public class LuckEffect extends Effect<LuckEffect> {
    @Override
    protected boolean canExecute() {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.set(Attribute.LUCK, 2.0f);
        return map;
    }
}
