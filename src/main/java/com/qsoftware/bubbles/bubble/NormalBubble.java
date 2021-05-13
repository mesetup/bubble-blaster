package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.core.utils.categories.ColorUtils;

import java.awt.*;

@Deprecated
public class NormalBubble extends AbstractBubble {
//    public Color[] colors;

    public NormalBubble() {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{Color.white};
        setRegistryName(ResourceLocation.fromString("qbubbles:normal_bubble"));

        setPriority(150_000_000L);
        setMinRadius(21);
        setMaxRadius(60);
        setMinSpeed(0.875d);
        setMaxSpeed(2.5d);
        setDefense(0.1f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }
}
