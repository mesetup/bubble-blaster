package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.common.AttributeMap;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.commons.utilities.python.builtins.ValueError;

@SuppressWarnings("GrazieInspection")
public class SpeedBoostEffect extends StatusEffect {
    public SpeedBoostEffect() throws ValueError {
        super();
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.setBase(Attribute.SPEED, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute(Entity entity, StatusEffectInstance statusEffectInstance) {
        return false;
    }

//    @Override
//    protected void updateStrength(EffectInstance effectInstance,  int old, int _new) {
//        Entity entity = effectInstance.getEntity();
//        if (entity instanceof PlayerEntity) {
//            PlayerEntity player = (PlayerEntity) entity;
//            player.setScoreModifier(player.getScoreModifier() - old + _new);
//        }
//    }
}
