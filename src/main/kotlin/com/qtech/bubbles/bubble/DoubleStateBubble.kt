package com.qtech.bubbles.bubble

import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class DoubleStateBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance {
        return EffectInstance(Effects.MULTI_SCORE.get(), (source.radius / 8).toLong(), 2)
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(255, 192, 0), Color(255, 192, 0), Color(0, 0, 0, 0), Color(255, 192, 0))
        registryName = ResourceLocation.fromString("bubbleblaster:double_state_bubble")
        priority = 460000.0
        radius = IntRange(21, 55)
        speed = DoubleRange(4.0, 10.8)
        defense = 0.22f
        attack = 0.0f
        score = 2f
        hardness = 1.0
    }
}