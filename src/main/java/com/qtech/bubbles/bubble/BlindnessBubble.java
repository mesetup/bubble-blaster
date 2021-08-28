package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class BlindnessBubble extends AbstractBubble {
//    public Color[] colors;

    public BlindnessBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 0, 0), new Color(32, 32, 32), new Color(64, 64, 64), new Color(96, 96, 96), new Color(128, 128, 128)};
        setRegistryName(ResourceEntry.fromString("qbubbles:blindness_bubble"));

        setPriority(640_000L);
//        setPriority(640_000_000L);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(7.4, 12.6));
        setDefense(0.2369f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.BLINDNESS.get(), source.getRadius() / 8, (byte) ((byte) source.getRadius() / 24 + 1));
    }
}
