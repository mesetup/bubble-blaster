package com.ultreon.bubbles.bubble;

import com.ultreon.bubbles.entity.BubbleEntity;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.commons.util.ColorUtils;
import com.ultreon.hydro.common.ResourceEntry;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

public class AccelerateBubble extends AbstractBubble {
    public AccelerateBubble() {
        colors = ColorUtils.parseColorString("#00003f,#00007f,#0000af,#0000ff");
        setRegistryName(ResourceEntry.fromString("bubbleblaster:accelerate_bubble"));

//        BubbleInit.BUBBLES.add(this);

//        setPriority(244000);
        setPriority(2440000);
        setRadius(new IntRange(25, 54));
        setSpeed(new DoubleRange(6.0, 28.0));
        setDefense(1.0f);
        setAttack(0.001f);
        setScore(1);
        setHardness(0.7d);
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;

            // Calculate Velocity X and Y.
            double accelerateX = 0;
            double accelerateY = 0;
            if (player.isMotionEnabled()) {
                accelerateX += Math.cos(Math.toRadians(player.getRotation())) * 0.625d;
                accelerateY += Math.sin(Math.toRadians(player.getRotation())) * 0.625d;
            }

            // Set velocity X and Y.
            player.setAccelerateX(player.getAccelerateX() + accelerateX);
            player.setAccelerateY(player.getAccelerateY() + accelerateY);
        }
    }
}
