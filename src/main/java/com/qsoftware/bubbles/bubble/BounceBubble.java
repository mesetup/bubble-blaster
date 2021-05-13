package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.init.BubbleInit;

import java.awt.*;

/**
 * @deprecated used in {@link BubbleInit}
 */
@Deprecated
public class BounceBubble extends AbstractBubble {
//    private double bounceAmount = 1;
//    public Color[] colors;

    public BounceBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ff0000");
        colors = new Color[]{Color.decode("#ff0000"), Color.decode("#ff3f00"), Color.decode("#ff7f00"), Color.decode("#ffaf00")};
        setRegistryName(ResourceLocation.fromString("qbubbles:bounce_bubble"));

//        BubbleInit.BUBBLES.add(this);

        setPriority(150000);
        setMinRadius(21);
        setMaxRadius(60);
        setMinSpeed(0.875f);
        setMaxSpeed(2.5f);
        setDefense(1.0f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0f);
        setBounceAmount(5.0f);
    }
}
