package com.qtech.bubbleblaster.effect;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Attribute;
import com.qtech.bubbleblaster.common.entity.Entity;

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
