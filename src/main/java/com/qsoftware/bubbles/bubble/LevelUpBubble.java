package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.utils.categories.ColorUtils;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
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
        setSpeed(new DoubleRange(0.875, 2.5));
        setDefense(0.0001f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0d);
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        super.onCollision(source, target);

        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (!(currentScene instanceof GameScene)) return;
        GameScene gameScene = (GameScene) currentScene;

        // Check target is a player/
        if (target instanceof PlayerEntity) {

            // Get Player and remove Bubble.
            PlayerEntity player = (PlayerEntity) target;
            gameScene.getGameType().removeBubble(source);

            // Player level-up.
            player.levelUp();
        }
    }

    @SuppressWarnings("ConstantConditions")
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
