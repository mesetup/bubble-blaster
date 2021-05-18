package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.random.Rng;
import com.qtech.bubbles.core.utils.categories.ColorUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

public class HardenedBubble extends AbstractBubble {
    public HardenedBubble() {
        colors = ColorUtils.parseColorString("#000000,#4f4f4f,#ff7f00,#ffff00");
        setRegistryName(ResourceLocation.fromString("qbubbles:hardened_bubble"));

        setPriority(387_500L);
        setRadius(new IntRange(21, 60));
        setSpeed(new DoubleRange(0.875, 2.5));
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