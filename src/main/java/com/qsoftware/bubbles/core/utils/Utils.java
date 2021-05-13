package com.qsoftware.bubbles.core.utils;

import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class Utils {
    @Nullable
    public static GameScene getGameScene() {
        Scene currentScene = Objects.requireNonNull(Util.getSceneManager()).getCurrentScene();
        if (!(currentScene instanceof GameScene)) return null;

        return (GameScene) currentScene;
    }
}
