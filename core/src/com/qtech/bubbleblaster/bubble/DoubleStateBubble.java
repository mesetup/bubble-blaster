package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.effect.EffectInstance;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.entity.BubbleEntity;
import com.qtech.bubbleblaster.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;


public class DoubleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public DoubleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(255, 192, 0), new Color(255, 192, 0), new Color(0, 0, 0, 0), new Color(255, 192, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:double_state_bubble"));

        setPriority(460000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(4.0, 10.8));
        setDefense(0.22f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(Effects.MULTI_SCORE.get(), source.getRadius() / 8, 2);
    }
}