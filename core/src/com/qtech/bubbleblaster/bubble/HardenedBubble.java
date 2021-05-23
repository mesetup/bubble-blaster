package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.common.random.Rng;
import com.qtech.bubbleblaster.core.utils.categories.ColorUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

public class HardenedBubble extends AbstractBubble {
    public HardenedBubble() {
        colors = ColorUtils.parseColorString("#000000,#4f4f4f,#ff7f00,#ffff00");
        setRegistryName(ResourceLocation.fromString("qbubbles:hardened_bubble"));

        setPriority(387_500L);
        setRadius(new IntRange(21, 60));
        setSpeed(new DoubleRange(4.5, 7.0));
        setAttack(0.0f);
        setScore(1f);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public float getDefense(AbstractGameType gameType, Rng rng) {
        float val = gameType.getLocalDifficulty() * 4;
        return rng.getNumber(val / 4f, 3f * val / 4f, gameType.getTicks(), 1L);
    }

    @Override
    public boolean isDefenseRandom() {
        return true;
    }
}
