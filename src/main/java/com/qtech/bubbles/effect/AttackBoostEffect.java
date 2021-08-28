package com.qtech.bubbles.effect;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.effect.StatusEffect;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.attribute.Attribute;
import com.qtech.utilities.python.builtins.ValueError;

public class AttackBoostEffect extends StatusEffect {
    public AttackBoostEffect() throws ValueError {
        super();
    }

    @Override
    public void execute(Entity entity, StatusEffectInstance statusEffectInstance) {

    }

    @Override
    protected void updateStrength() {

    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributes = new AttributeMap();
        attributes.setBase(Attribute.ATTACK, 1f);

        return attributes;
    }
}
