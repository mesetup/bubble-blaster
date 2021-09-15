package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;

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
