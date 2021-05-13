package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.core.utils.categories.ColorUtils;

import java.awt.*;

@Deprecated
public class DoubleValueBubble extends AbstractBubble {
//    public Color[] colors;

    public DoubleValueBubble() {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(255, 192, 0), new Color(255, 192, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:double_bubble"));

        setPriority(230000L);
        setMinRadius(21);
        setMaxRadius(55);
        setMinSpeed(1.0d);
        setMaxSpeed(2.7d);
        setDefense(0.1f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }
}
