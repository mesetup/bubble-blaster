package com.qtech.bubbles.bubble

import com.jhlabs.image.HSBAdjustFilter
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.core.utils.categories.ColorUtils
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.entity.player.PlayerEntity
import com.qtech.bubbles.graphics.HueComposite
import com.qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Composite

class UltraBubble : AbstractBubble() {
    override fun getFilters(bubbleEntity: BubbleEntity?): ArrayList<Any> {
        val filters = ArrayList<Any>()
        val filter = HSBAdjustFilter()
        filter.setHFactor((System.currentTimeMillis() / 3).toFloat() % 1)
        filters.add(filter)
        return filters
    }

    override fun getComposite(ticksLived: Int): Composite {
        return HueComposite(1.0f, ticksLived)
    }

    override fun onCollision(source: BubbleEntity, target: Entity) {
        super.onCollision(source, target)
        if (target is PlayerEntity) {
            val player = target
            player.addEffect(EffectInstance(Effects.ATTACK_BOOST.get(), 10, 2))
            player.addEffect(EffectInstance(Effects.DEFENSE_BOOST.get(), 10, 2))
            player.addEffect(EffectInstance(Effects.MULTI_SCORE.get(), 12, 10))
            player.addEffect(EffectInstance(Effects.BUBBLE_FREEZE.get(), 8, 1))
            player.addEffect(EffectInstance(Effects.LUCK.get(), 8, 1))
        }
    }

    init {
        colors = ColorUtils.parseColorString("#007fff,#0000ff,#7f00ff,#ff00ff,#ff007f")

//        setPriority(46000000d);
        priority = 4600.0
        radius = IntRange(21, 55)
        speed = DoubleRange(19.2, 38.4)
        defense = 0.573f
        attack = 0.0f
        score = 10f
        hardness = 1.0
    }
}