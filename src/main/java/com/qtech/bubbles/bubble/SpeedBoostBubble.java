package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class SpeedBoostBubble extends AbstractBubble {
//    public Color[] colors;

    public SpeedBoostBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 128, 255), new Color(64, 160, 255), new Color(128, 192, 255), new Color(192, 224, 255), new Color(255, 255, 255)};
        setRegistryName(ResourceEntry.fromString("qbubbles:speed_boost_bubble"));

        setPriority(460000L);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(16.2, 24.8));
        setDefense(0.1f);
        setAttack(0.0f);
        setScore(0);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public StatusEffectInstance getEffect(BubbleEntity source, Entity target) {
        return new StatusEffectInstance(Effects.SPEED_BOOST.get(), source.getRadius() / 8, (byte) (source.getSpeed() / 3.2d));
    }
}
