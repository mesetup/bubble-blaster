package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class SpeedBoostBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance? {
        return EffectInstance(Effects.SPEED_BOOST.get(), (source.radius / 8).toLong(), (source.speed / 3.2).toInt())
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(0, 128, 255), Color(64, 160, 255), Color(128, 192, 255), Color(192, 224, 255), Color(255, 255, 255))
        registryName = ResourceLocation.fromString("bubbleblaster:speed_boost_bubble")
        priority = 460000.0
        radius = IntRange(21, 55)
        speed = DoubleRange(16.2, 24.8)
        defense = 0.1f
        attack = 0.0f
        score = 0f
        hardness = 1.0

//        BubbleInit.BUBBLES.add(this);
    }
}