package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;

public class DefenseBoostEffect extends StatusEffect {
    public DefenseBoostEffect() {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @Override
    public void execute(Entity entity, StatusEffectInstance statusEffectInstance) {

    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.DEFENSE, 1f);
        return map;
    }
}
