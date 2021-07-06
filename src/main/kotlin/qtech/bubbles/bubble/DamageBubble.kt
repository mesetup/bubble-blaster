package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class DamageBubble : AbstractBubble() {
    override fun getModifiedPriority(localDifficulty: Double): Double {
        return priority * localDifficulty / 20.0
    }

    //    public Color[] colors;
    init {
        colors = arrayOf(Color(255, 0, 0), Color(255, 96, 0), Color(255, 0, 0))
        registryName = ResourceLocation.fromString("bubbleblaster:damage_bubble")
        priority = 10000000.0
        radius = IntRange(17, 70)
        speed = DoubleRange(4.0, 10.0)
        defense = 0.2f
        attack = 1.0f
        score = 1f
        hardness = 1.0
    }
}