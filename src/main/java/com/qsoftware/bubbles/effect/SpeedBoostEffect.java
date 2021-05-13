package com.qsoftware.bubbles.effect;

import com.qsoftware.bubbles.common.AttributeMap;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.utilities.python.builtins.ValueError;

public class SpeedBoostEffect extends Effect<SpeedBoostEffect> {
    public SpeedBoostEffect() throws ValueError {
        super();
    }

    @Override
    public void tick(Entity evt, EffectInstance effectInstance) {
        // Do nothing.
    }

    @Override
    public AttributeMap getAttributeModifiers() {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.set(Attribute.SPEED, 1f);
        return attributeMap;
    }

    @Override
    protected boolean canExecute(EffectInstance effectInstance) {
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
