package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.utilities.python.builtins.ValueError;

public class AttackBoostEffect extends Effect<AttackBoostEffect> {
    public AttackBoostEffect() throws ValueError {
        super();
    }

    @Override
    public void execute() {

    }

    @Override
    protected void updateStrength() {

    }

    @Override
    protected boolean canExecute() {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributes = new AttributeMap();
        attributes.setBase(Attribute.ATTACK, 1f);

        return attributes;
    }
}
