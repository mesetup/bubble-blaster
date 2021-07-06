package qtech.bubbles.bubble

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.entity.Entity
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.core.utils.categories.ColorUtils
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.entity.player.PlayerEntity
import org.apache.commons.lang.math.DoubleRange
import org.apache.commons.lang.math.IntRange

/**
 * Levelup Bubble
 *
 * @author Quinten Jungblut
 * @since 1.0.0
 */
class LevelUpBubble : AbstractBubble() {
    override fun onCollision(source: BubbleEntity, target: Entity) {
        super.onCollision(source, target)

        // Check target is a player/
        if (target is PlayerEntity) {

            // Get Player and remove Bubble.
            source.delete()

            // Player level-up.
            target.levelUp()
        }
    }

    override fun canSpawn(gameMode: AbstractGameMode): Boolean {
        // If player is not spawned yet, the player cannot have any change. So return false.
        if (gameMode.player == null) return false

        // Calculate the maximum level for the player's score.
        val maxLevelup = Math.round(gameMode.player!!.score).toInt() / 50000 + 1

        // Check for existing level-up bubble entities.
        return if (gameMode.environment!!.entities.stream().filter { entity: Entity? -> entity is BubbleEntity } // Filter for bubble entities.
                .map { entity: Entity? -> entity as BubbleEntity? } // Cast to bubble entities.
                .anyMatch { bubbleEntity: BubbleEntity? -> bubbleEntity!!.bubbleType === this }) { // Check for level-up bubbles of the type of this class.
            false // Then it can't spawn.
        } else maxLevelup > gameMode.player!!.level

        // Return flag for ‘if the maximum level for score is greater than the player's current level’
    }

    init {
        // Color & key.
        colors = ColorUtils.parseColorString("#ffff00,#ffffff,#ff9f00")
        registryName = ResourceLocation.fromString("bubbleblaster:level_up_bubble")

        // Set initial data values.
        priority = 128000000.0
        radius = IntRange(21, 60)
        speed = DoubleRange(6.4, 19.2)
        defense = 0.000001f
        attack = 0.0f
        score = 1f
        hardness = 1.0
    }
}