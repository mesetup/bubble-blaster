package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.commons.utilities.python.builtins.ValueError;

public class MultiScoreEffect extends StatusEffect {
    public MultiScoreEffect() throws ValueError {
        super();
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setBase(Attribute.SCORE_MULTIPLIER, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }
}
