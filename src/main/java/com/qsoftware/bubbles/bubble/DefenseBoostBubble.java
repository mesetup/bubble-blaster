package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class DefenseBoostBubble extends AbstractBubble {
//    public Color[] colors;

    public DefenseBoostBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 192), new Color(64, 255, 208), new Color(128, 255, 224), new Color(192, 255, 240), new Color(255, 255, 255)};
        setRegistryName(ResourceLocation.fromString("qbubbles:defense_bubble"));

        setPriority(131_072);
        setRadius(new IntRange(21, 70));
        setSpeed(new DoubleRange(2.1, 3.9));
        setDefense(0.327f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public EffectInstance getEffect(BubbleEntity source, Entity target) {
        return new EffectInstance(EffectInit.DEFENSE_BOOST.get(), source.getRadius() / 8, (byte) ((byte) source.getRadius() / 24 + 1));
    }
}
