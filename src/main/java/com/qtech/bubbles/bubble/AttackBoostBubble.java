package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class AttackBoostBubble extends AbstractBubble {
//    public Color[] colors;

    public AttackBoostBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(128, 64, 32), new Color(160, 126, 92), new Color(192, 188, 152), new Color(224, 216, 208), new Color(255, 255, 255)};
        setRegistryName(ResourceEntry.fromString("qbubbles:attack_bubble"));

        setPriority(98_304);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(8.4, 15.6));
        setDefense(0.0775f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.ATTACK_BOOST.get(), source.getRadius() / 8, source.getRadius() / 24 + 1);
    }
}
