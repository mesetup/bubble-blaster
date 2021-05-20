package com.qtech.bubbleblaster.effect;

import com.qtech.bubbleblaster.common.AttributeMap;
import com.qtech.bubbleblaster.common.effect.Effect;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Attribute;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.utilities.python.builtins.ValueError;

public class SpeedBoostEffect extends Effect {
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
    protected boolean canExecute(Entity entity, EffectInstance effectInstance) {
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
