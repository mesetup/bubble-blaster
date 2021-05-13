package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.Entity;

public class DefenseBoostEffect extends Effect<DefenseBoostEffect> {
    public DefenseBoostEffect() {
        super();
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }

    @Override
    public void execute(Entity evt, EffectInstance effectInstance) {

    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap map = new AttributeMap();
        map.set(Attribute.DEFENSE, 1f);
        return map;
    }
}
