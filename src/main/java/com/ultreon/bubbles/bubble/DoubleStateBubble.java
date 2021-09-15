package com.ultreon.bubbles.bubble;

import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.bubbles.effect.StatusEffectInstance;
import com.ultreon.bubbles.entity.BubbleEntity;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class DoubleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public DoubleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(255, 192, 0), new Color(255, 192, 0), new Color(0, 0, 0, 0), new Color(255, 192, 0)};
        setRegistryName(ResourceEntry.fromString("qbubbles:double_state_bubble"));

        setPriority(460000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(4.0, 10.8));
        setDefense(0.22f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.MULTI_SCORE.get(), source.getRadius() / 8, 2);
    }
}
