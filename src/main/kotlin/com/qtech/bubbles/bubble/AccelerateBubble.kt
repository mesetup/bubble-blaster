package com.qtech.bubbles.bubble

import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.core.utils.categories.ColorUtils
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.entity.player.PlayerEntity
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange

class AccelerateBubble : AbstractBubble() {
    override fun onCollision(source: BubbleEntity, target: Entity) {
        if (target is PlayerEntity) {
            val player = target

            // Calculate Velocity X and Y.
            var accelerateX = 0.0
            var accelerateY = 0.0
            if (player.isMotionEnabled) {
                accelerateX += Math.cos(Math.toRadians(player.rotation)) * 0.625
                accelerateY += Math.sin(Math.toRadians(player.rotation)) * 0.625
            }

            // Set velocity X and Y.
            player.accelerateX = player.accelerateX + accelerateX
            player.accelerateY = player.accelerateY + accelerateY
        }
    }

    init {
        colors = ColorUtils.parseColorString("#00003f,#00007f,#0000af,#0000ff")
        registryName = ResourceLocation.fromString("bubbleblaster:accelerate_bubble")

//        BubbleInit.BUBBLES.add(this);

//        setPriority(244000);
        priority = 2440000.0
        radius = IntRange(25, 54)
        speed = DoubleRange(6.0, 28.0)
        defense = 0.1f
        attack = 0.001f
        score = 1f
        hardness = 0.7
    }
}