package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class DoubleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public DoubleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(255, 192, 0), new Color(255, 192, 0), new Color(0, 0, 0, 0), new Color(255, 192, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:double_state_bubble"));

        setPriority(460000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(1.0, 2.7));
        setDefense(0.22f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(EffectInit.MULTI_SCORE.get(), source.getRadius() / 8, 2);
    }
}
