package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;

import java.awt.*;

@Deprecated
public class TripleValueBubble extends AbstractBubble {
//    public Color[] colors;

    public TripleValueBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 255, 255), new Color(0, 255, 255), new Color(0, 255, 255)};
        setRegistryName(ResourceLocation.fromString("qbubbles:triple_bubble"));

        setPriority(4600L);
        setMinRadius(21);
        setMaxRadius(55);
        setMinSpeed(1.0d);
        setMaxSpeed(2.7d);
        setDefense(0.1f);
        setAttack(0.0f);
        setScore(3);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }
}
