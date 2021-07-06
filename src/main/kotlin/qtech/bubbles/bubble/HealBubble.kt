package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.entity.Entity
import qtech.bubbles.core.utils.categories.ColorUtils
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.entity.player.PlayerEntity
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange
import java.awt.Color

class HealBubble : AbstractBubble() {
    override fun onCollision(source: BubbleEntity, target: Entity) {
        super.onCollision(source, target)
        if (target is PlayerEntity) {
            target.restoreDamage(1.8f * (source.gameMode.localDifficulty / 20.0f + 1.8f / 20.0f))
        }
    }

    //    public Color[] colors;
    init {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff")
        colors = arrayOf(Color(0, 192, 0), Color(0, 0, 0, 0), Color(0, 192, 0), Color(0, 192, 0))
        registryName = ResourceLocation.fromString("bubbleblaster:heal_bubble")
        priority = 4000000.0
        radius = IntRange(17, 70)
        speed = DoubleRange(10.2, 18.6)
        defense = 0.3f
        attack = 0.0f
        score = 1f
        hardness = 1.0

//        BubbleInit.BUBBLES.add(this);
    }
}