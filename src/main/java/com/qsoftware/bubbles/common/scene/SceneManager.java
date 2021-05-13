package com.qsoftware.bubbles.common.scene;

import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.scene.LoadScene;
import com.qsoftware.bubbles.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Scene manager, used for change between scenes.
 *
 * @see Scene
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class SceneManager {
    private static final SceneManager instance = new SceneManager(new LoadScene());
    private static final Logger LOGGER = LogManager.getLogger("QB:Scene:Manager");
    private final Scene startScene;

    private Scene currentScene;
    private boolean initialized = false;

    public static SceneManager getInstance() {
        return instance;
    }

    private SceneManager(Scene startScene) {
        this.currentScene = this.startScene = startScene;
    }

    /**
     * Display a new scene.
     *
     * @param scene the scene to display
     * @return if changing the scene was successful.
     */
    public boolean displayScene(Scene scene) {
        return displayScene(scene, false);
    }

    /**
     * Display a new scene.
     *
     * @param scene the scene to display
     * @return if changing the scene was successful.
     */
    public boolean displayScene(Scene scene, boolean force) {
        LOGGER.debug("Hiding " + currentScene.getClass().getSimpleName());
        if (currentScene.hideScene(scene) || force) {
            this.currentScene = scene;
            Util.setCursor(scene.getDefaultCursor());
            LOGGER.debug("Showing " + currentScene.getClass().getSimpleName());
            this.currentScene.showScene();
            return true;
        }

        LOGGER.debug("Hiding " + currentScene.getClass().getSimpleName() + " canceled.");
        return false;
    }

    /**
     * DEPRECATED
     *
     * @return the current scene resource location.
     * @deprecated since 1.0.???
     */
    @Contract("->null")
    @Deprecated
    @Nullable
    public ResourceLocation getCurrentSceneKey() {
        return null;
    }

    public synchronized void start() {
        if (initialized) {
            throw new IllegalStateException("SceneManager already initialized.");
        }

        this.initialized = true;
        this.currentScene = this.startScene;
        LOGGER.debug("Showing " + currentScene.getClass().getSimpleName());
        Util.setCursor(this.startScene.getDefaultCursor());
        this.startScene.showScene();
    }

    @Nullable
    public Scene getCurrentScene() {
        return currentScene;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
