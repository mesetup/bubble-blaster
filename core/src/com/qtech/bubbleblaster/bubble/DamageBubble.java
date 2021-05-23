package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.ResourceLocation;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;


public class DamageBubble extends AbstractBubble {
//    public Color[] colors;

    public DamageBubble() {
        colors = new Color[]{new Color(255, 0, 0), new Color(255, 96, 0), new Color(255, 0, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:damage_bubble"));

        setPriority(10000000d);
        setRadius(new IntRange(17, 70));
        setSpeed(new DoubleRange(4.0d, 10.0d));
        setDefense(0.2f);
        setAttack(1.0f);
        setScore(1);
        setHardness(1.0d);
    }

    @Override
    public double getModifiedPriority(double localDifficulty) {
        return getPriority() * localDifficulty / 20d;
    }
}
