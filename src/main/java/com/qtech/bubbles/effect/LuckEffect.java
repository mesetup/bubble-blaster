package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.attribute.Attribute;

public class LuckEffect extends StatusEffect {
    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.LUCK, 2.0f);
        return map;
    }
}
