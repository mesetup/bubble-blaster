package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.entity.Entity;

public class DefenseBoostEffect extends Effect {
    public DefenseBoostEffect() {
        super();
    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    @Override
    public void execute(Entity entity, EffectInstance effectInstance) {

    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.DEFENSE, 1f);
        return map;
    }
}
