package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.init.Effects;

import java.awt.*;

@Deprecated
public class PoisonBubble extends AbstractBubble {
//    public Color[] colors;

    public PoisonBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(128, 255, 0), new Color(160, 192, 32), new Color(192, 128, 64), new Color(224, 64, 96), new Color(255, 0, 128)};
        setRegistryName(ResourceLocation.fromString("qbubbles:poison_bubble"));

        setPriority(1_313_131L);
        setMinRadius(34);
        setMaxRadius(83);
        setMinSpeed(1.0d);
        setMaxSpeed(2.7d);
        setDefense(0.225f);
        setAttack(0.0f);
        setScore(0.5f);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(Effects.POISON.get(), source.getRadius() / 8, (int) (source.getSpeed() * 4d));
    }
}
