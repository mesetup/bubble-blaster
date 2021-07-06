package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class TripleStateBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance? {
        return EffectInstance(Effects.MULTI_SCORE.get(), (source.radius / 8).toLong(), 3)
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(0, 255, 255), Color(0, 255, 255), Color(0, 0, 0, 0), Color(0, 255, 255), Color(0, 0, 0, 0), Color(0, 255, 255))
        registryName = ResourceLocation.fromString("bubbleblaster:triple_state_bubble")
        priority = 115000.0
        radius = IntRange(21, 55)
        speed = DoubleRange(4.1, 10.4)
        defense = 0.335f
        attack = 0.0f
        score = 3f
        hardness = 1.0
    }
}