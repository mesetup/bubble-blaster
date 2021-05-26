package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class TripleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public TripleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 255), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255)};
        setRegistryName(ResourceLocation.fromString("bubbleblaster:triple_state_bubble"));

        setPriority(115000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(4.1, 10.4));
        setDefense(0.335f);
        setAttack(0.0f);
        setScore(3);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(Effects.MULTI_SCORE.get(), source.getRadius() / 8, 3);
    }
}
