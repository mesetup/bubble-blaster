package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class DefenseBoostBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance {
        return EffectInstance(Effects.DEFENSE_BOOST.get(), (source.radius / 8).toLong(), (source.radius.toByte() / 24 + 1).toByte().toInt())
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(0, 255, 192), Color(64, 255, 208), Color(128, 255, 224), Color(192, 255, 240), Color(255, 255, 255))
        registryName = ResourceLocation.fromString("bubbleblaster:defense_bubble")
        priority = 131072.0
        radius = IntRange(21, 70)
        speed = DoubleRange(8.8, 16.4)
        defense = 0.327f
        attack = 0.0f
        score = 2f
        hardness = 1.0

//        BubbleInit.BUBBLES.add(this);
    }
}