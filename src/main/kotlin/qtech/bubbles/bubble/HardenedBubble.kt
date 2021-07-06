package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.random.Rng
import qtech.bubbles.core.utils.categories.ColorUtils
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange

class HardenedBubble : AbstractBubble() {
    override fun getDefense(gameMode: AbstractGameMode, rng: Rng): Float {
        val `val` = gameMode.localDifficulty * 4
        return rng.getNumber(`val` / 4f, 3f * `val` / 4f, gameMode.ticks, 1L)
    }

    override val isDefenseRandom: Boolean
        get() = true

    init {
        colors = ColorUtils.parseColorString("#000000,#4f4f4f,#ff7f00,#ffff00")
        registryName = ResourceLocation.fromString("bubbleblaster:hardened_bubble")
        priority = 387500.0
        radius = IntRange(21, 60)
        speed = DoubleRange(4.5, 7.0)
        attack = 0.0f
        score = 1f
        hardness = 1.0

//        BubbleInit.BUBBLES.add(this);
    }
}