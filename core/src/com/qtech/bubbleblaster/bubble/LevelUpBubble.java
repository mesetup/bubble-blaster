package com.qtech.bubbleblaster.bubble;

import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.core.utils.categories.ColorUtils;
import com.qtech.bubbleblaster.entity.BubbleEntity;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

/**
 * Levelup Bubble
 *
 * @author Quinten Jungblut
 * @since 1.0.0
 */
public class LevelUpBubble extends AbstractBubble {
    public LevelUpBubble() {
        // Color & key.
        colors = ColorUtils.parseColorString("#ffff00,#ffffff,#ff9f00");
        setRegistryName(ResourceLocation.fromString("qbubbles:level_up_bubble"));

        // Set initial data values.
        setPriority(128000000L);
        setRadius(new IntRange(21, 60));
        setSpeed(new DoubleRange(6.4, 19.2));
        setDefense(0.0001f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0d);
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        super.onCollision(source, target);

        // Check target is a player/
        if (target instanceof PlayerEntity) {

            // Get Player and remove Bubble.
            PlayerEntity player = (PlayerEntity) target;
            source.delete();

            // Player level-up.
            player.levelUp();
        }
    }

    @Override
    public boolean canSpawn(AbstractGameType gameType) {
        // If player is not spawned yet, the player cannot have any change. So return false.
        if (gameType.getPlayer() == null) return false;

        // Calculate the maximum level for the player's score.
        int maxLevelup = (int) Math.round(gameType.getPlayer().getScore()) / 50_000 + 1;

        // Check for existing level-up bubble entities.
        if (gameType.getEnvironment().getEntities().stream().
                filter((entity) -> entity instanceof BubbleEntity) // Filter for bubble entities.
                .map((entity) -> (BubbleEntity) entity) // Cast to bubble entities.
                .anyMatch(bubbleEntity -> bubbleEntity.getBubbleType() == this)) { // Check for level-up bubbles of the type of this class.
            return false; // Then it can't spawn.
        }

        // Return flag for ‘if the maximum level for score is greater than the player's current level’
        return maxLevelup > gameType.getPlayer().getLevel();
    }
}
