package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.utilities.python.builtins.ValueError;

public class AttackBoostEffect extends Effect {
    public AttackBoostEffect() throws ValueError {
        super();
    }

    @Override
    public void execute(Entity entity, EffectInstance effectInstance) {

    }

    @Override
    protected void updateStrength() {

    }

    @Override
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributes = new AttributeMap();
        attributes.setBase(Attribute.ATTACK, 1f);

        return attributes;
    }
}
