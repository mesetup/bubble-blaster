package com.qsoftware.bubbles.bubble;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.utils.categories.ColorUtils;
import com.qsoftware.bubbles.entity.BubbleEntity;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;

import java.awt.*;

public class HealBubble extends AbstractBubble {
//    public Color[] colors;

    public HealBubble() {
        colors = ColorUtils.multiConvertHexToRgb("#ffffff");
        colors = new Color[]{new Color(0, 192, 0), new Color(0, 0, 0, 0), new Color(0, 192, 0), new Color(0, 192, 0)};
        setRegistryName(ResourceLocation.fromString("qbubbles:heal_bubble"));

        setPriority(4000000);
        setRadius(new IntRange(17, 70));
        setSpeed(new DoubleRange(1.0d, 2.5d));
        setDefense(0.3f);
        setAttack(0.0f);
        setScore(1);
        setHardness(1.0d);

//        BubbleInit.BUBBLES.add(this);
    }

    @Override
    public void onCollision(BubbleEntity source, Entity target) {
        super.onCollision(source, target);

        Scene currentScene = Util.getSceneManager().getCurrentScene();
        if (!(currentScene instanceof GameScene)) return;
        GameScene gameScene = (GameScene) currentScene;

//        System.out.println("Collision");
        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            player.heal(1.8f * (gameScene.getGameType().getLocalDifficulty() / 20.0f + (1.8f / 20.0f)));
        }
    }
}
