package com.qtech.bubbles.bubble

import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class AttackBoostBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance {
        return EffectInstance(Effects.ATTACK_BOOST.get(), ((source.radius / 8).toLong()), (source.radius / 24 + 1))
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(128, 64, 32), Color(160, 126, 92), Color(192, 188, 152), Color(224, 216, 208), Color(255, 255, 255))
        registryName = ResourceLocation.fromString("bubbleblaster:attack_bubble")
        priority = 98304.0
        radius = IntRange(21, 70)
        speed = DoubleRange(8.4, 15.6)
        defense = 0.775f
        attack = 0.0f
        score = 2f
        hardness = 1.0
    }
}