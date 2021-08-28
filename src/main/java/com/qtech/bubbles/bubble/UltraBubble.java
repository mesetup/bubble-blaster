package com.qtech.bubbles.bubble;

import com.jhlabs.image.HSBAdjustFilter;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.core.utils.categories.ColorUtils;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.init.Effects;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.util.ArrayList;

public class UltraBubble extends AbstractBubble {
    public UltraBubble() {
        colors = ColorUtils.parseColorString("#007fff,#0000ff,#7f00ff,#ff00ff,#ff007f");

        setPriority(4600d);
        setRadius(new IntRange(21, 55));
        setSpeed(new DoubleRange(19.2, 38.4));
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
            player.addEffect(new StatusEffectInstance(Effects.ATTACK_BOOST.get(), 10, 2));
            player.addEffect(new StatusEffectInstance(Effects.DEFENSE_BOOST.get(), 10, 2));
            player.addEffect(new StatusEffectInstance(Effects.MULTI_SCORE.get(), 12, 2));
            player.addEffect(new StatusEffectInstance(Effects.BUBBLE_FREEZE.get(), 8, 1));
            player.addEffect(new StatusEffectInstance(Effects.LUCK.get(), 8, 1));
        }
    }
}
