package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.core.utils.categories.ColorUtils;
import com.qtech.bubbleblaster.entity.BubbleEntity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;


public class HealBubble extends AbstractBubble {
//    public Color[] colors;

    public HealBubble() {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 192, 0), new Color(0, 0, 0, 0), new Color(0, 192, 0), new Color(0, 192, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:heal_bubble"));

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
