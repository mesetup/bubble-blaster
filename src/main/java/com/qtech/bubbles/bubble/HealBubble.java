package com.qtech.bubbles.bubble;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.core.utils.categories.ColorUtils;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class HealBubble extends AbstractBubble {
//    public Color[] colors;

    public HealBubble() {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 192, 0), new Color(0, 0, 0, 0), new Color(0, 192, 0), new Color(0, 192, 0)};
        setRegistryName(ResourceLocation.fromString("bubbleblaster:heal_bubble"));

        setPriority(4000000);
        setRadius(new IntRange(17, 70));
        setSpeed(new DoubleRange(10.2d, 18.6d));
        setDefense(0.3f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        super.onCollision(source, target);

        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            player.restoreDamage(1.8f * (source.getGameType().getLocalDifficulty() / 20.0f + (1.8f / 20.0f)));
        }
    }
}
