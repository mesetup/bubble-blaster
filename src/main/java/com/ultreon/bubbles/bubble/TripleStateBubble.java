package com.ultreon.bubbles.bubble;

import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.bubbles.effect.StatusEffectInstance;
import com.ultreon.bubbles.entity.BubbleEntity;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class TripleStateBubble extends AbstractBubble {
//    public Color[] colors;

    public TripleStateBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 255), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255), new Color(0, 0, 0, 0), new Color(0, 255, 255)};
        setRegistryName(ResourceEntry.fromString("qbubbles:triple_state_bubble"));

        setPriority(115000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(4.1, 10.4));
        setDefense(0.335f);
        setAttack(0.0f);
        setScore(3);
        setHardness(1.0d);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.MULTI_SCORE.get(), source.getRadius() / 8, 3);
    }
}
