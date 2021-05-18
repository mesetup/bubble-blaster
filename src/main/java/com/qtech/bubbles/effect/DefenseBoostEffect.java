package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.entity.Attribute;

public class DefenseBoostEffect extends Effect<DefenseBoostEffect> {
    public DefenseBoostEffect() {
        super();
    }

    @Override
    protected boolean canExecute() {
        return false;
    }

    @Override
    public void execute() {

    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.setBase(Attribute.DEFENSE, 1f);
        return map;
    }
}
