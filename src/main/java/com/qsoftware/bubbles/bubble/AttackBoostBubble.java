package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class AttackBoostBubble extends AbstractBubble {
//    public Color[] colors;

    public AttackBoostBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(128, 64, 32), new Color(160, 126, 92), new Color(192, 188, 152), new Color(224, 216, 208), new Color(255, 255, 255)};
        setRegistryName(ResourceLocation.fromString("qbubbles:attack_bubble"));

        setPriority(98_304);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(2.1, 3.9));
        setDefense(0.0775f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(EffectInit.ATTACK_BOOST.get(), source.getRadius() / 8, source.getRadius() / 24 + 1);
    }
}
