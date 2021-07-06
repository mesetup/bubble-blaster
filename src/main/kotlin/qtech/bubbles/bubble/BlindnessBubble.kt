package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.effect.EffectInstance
import qtech.bubbles.entity.Entity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.init.Effects
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class BlindnessBubble : AbstractBubble() {
    override fun getEffect(source: BubbleEntity, target: Entity): EffectInstance {
        return EffectInstance(Effects.BLINDNESS.get(), (source.radius / 8).toLong(), (source.radius.toByte() / 24 + 1))
    }

    //    public Color[] colors;
    init {
//        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = arrayOf(Color(0, 0, 0), Color(32, 32, 32), Color(64, 64, 64), Color(96, 96, 96), Color(128, 128, 128))
        registryName = ResourceLocation.fromString("bubbleblaster:blindness_bubble")
        priority = 640000.0
        //        setPriority(640_000_000L);
        radius = IntRange(21, 70)
        speed = DoubleRange(7.4, 12.6)
        defense = 0.2369f
        attack = 0.0f
        score = 2f
        hardness = 1.0

//        BubbleInit.BUBBLES.add(this);
    }
}