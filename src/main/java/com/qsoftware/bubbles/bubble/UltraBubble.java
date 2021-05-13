package com.qsoftware.bubbles.bubble;

import com.jhlabs.image.HSBAdjustFilter;
import com.qsoftware.bubbles.common.effect.EffectInstance;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.init.EffectInit;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;
import java.util.ArrayList;

public class UltraBubble extends AbstractBubble {
    public UltraBubble() {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(255, 0, 0), new Color(255, 128, 0), new Color(255, 255, 0), new Color(128, 255, 0), new Color(0, 255, 0)};

        setPriority(4600d);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(1.0, 2.7));
        setDefense(0.573f);
        setAttack(0.0f);
        setScore(2);
        setHardness(1.0d);
    }

    @Override
    public ArrayList<Object> getFilters(BubbleEntity bubbleEntity) {
        ArrayList<Object> filters = new ArrayList<>();

        HSBAdjustFilter filter = new HSBAdjustFilter();
        filter.setHFactor((float) (System.currentTimeMillis() / 3) % 1);
        filters.add(filter);

        return filters;
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        super.onCollision(source, target);
        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            player.addEffect(new EffectInstance(EffectInit.ATTACK_BOOST.get(), 10, 2));
            player.addEffect(new EffectInstance(EffectInit.DEFENSE_BOOST.get(), 10, 2));
            player.addEffect(new EffectInstance(EffectInit.MULTI_SCORE.get(), 12, 2));
            player.addEffect(new EffectInstance(EffectInit.BUBBLE_FREEZE.get(), 8, 1));
            player.addEffect(new EffectInstance(EffectInit.LUCK.get(), 8, 1));
        }
    }
}
