package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.commons.utilities.python.builtins.ValueError;

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
