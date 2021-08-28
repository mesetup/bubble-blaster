package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class DefenseBoostBubble extends AbstractBubble {
//    public Color[] colors;

    public DefenseBoostBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 192), new Color(64, 255, 208), new Color(128, 255, 224), new Color(192, 255, 240), new Color(255, 255, 255)};
        setRegistryName(ResourceEntry.fromString("qbubbles:defense_bubble"));

        setPriority(131_072);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(8.8, 16.4));
        setDefense(0.327f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.DEFENSE_BOOST.get(), source.getRadius() / 8, (byte) ((byte) source.getRadius() / 24 + 1));
    }
}
