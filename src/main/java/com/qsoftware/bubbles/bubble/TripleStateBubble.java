package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class TripleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public TripleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 255), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255)};
        setRegistryName(ResourceLocation.fromString("qbubbles:triple_state_bubble"));

        setPriority(115000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(1.0, 2.7));
        setDefense(0.335f);
        setAttack(0.0f);
        setScore(3);
        setHardness(1.0d);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(EffectInit.MULTI_SCORE.get(), source.getRadius() / 8, 3);
    }
}
