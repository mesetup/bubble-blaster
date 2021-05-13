package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.utilities.python.builtins.ValueError;

public class AttackBoostEffect extends Effect<AttackBoostEffect> {
    public AttackBoostEffect() throws ValueError {
        super();
    }

    @Override
    public void execute(Entity evt, EffectInstance effectInstance) {

    }

    @Override
    protected void updateStrength(EffectInstance effectInstance, int old, int _new) {

    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
        return false;
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributes = new AttributeMap();
        attributes.set(Attribute.ATTACK, 1f);

        return attributes;
    }
}
