package com.qtech.bubbleblaster.effect;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Attribute;
import com.qtech.bubbleblaster.common.entity.Entity;

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
