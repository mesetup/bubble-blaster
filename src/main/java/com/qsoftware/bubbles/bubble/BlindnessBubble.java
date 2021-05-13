package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class BlindnessBubble extends AbstractBubble {
//    public Color[] colors;

    public BlindnessBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 0, 0), new Color(32, 32, 32), new Color(64, 64, 64), new Color(96, 96, 96), new Color(128, 128, 128)};
        setRegistryName(ResourceLocation.fromString("qbubbles:blindness_bubble"));

        setPriority(640_000L);
//        setPriority(640_000_000L);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(2.1, 3.9));
        setDefense(0.2369f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(EffectInit.BLINDNESS.get(), source.getRadius() / 8, (byte) ((byte) source.getRadius() / 24 + 1));
    }
}
