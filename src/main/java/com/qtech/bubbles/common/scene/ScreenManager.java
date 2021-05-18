package com.qtech.bubbles.common.scene;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qtech.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.scene.LoadScreen;
import com.qtech.bubbles.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Scene manager, used for change between scenes.
 *
 * @see Screen
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class ScreenManager {
    private static final ScreenManager instance = new ScreenManager(new LoadScreen());
    private static final Logger LOGGER = LogManager.getLogger("QB:Screen:Manager");
    private final Screen startScreen;

    @Nullable
    private Screen currentScreen;
    private boolean initialized = false;

    public static ScreenManager getInstance() {
        return instance;
    }

    private ScreenManager(Screen startScreen) {
        this.currentScreen = this.startScreen = startScreen;
    }

    /**
     * Display a new scene.
     *
     * @param scene the scene to display
     * @return if changing the scene was successful.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean displayScreen(@Nullable Screen scene) {
        return displayScreen(scene, false);
    }

    /**
     * Display a new scene.
     *
     * @param screen the scene to display
     * @return if changing the scene was successful.
     */
    public boolean displayScreen(@Nullable Screen screen, boolean force) {
        if (currentScreen != null) {
            LOGGER.debug("Hiding " + currentScreen.getClass().getSimpleName());
            if (currentScreen.onClose(screen) || force) {
                this.currentScreen = screen;
                if (screen != null) {
                    Util.setCursor(screen.getDefaultCursor());
                } else {
                    Util.setCursor(QBubbles.getInstance().getDefaultCursor());
                }

                if (currentScreen != null) {
                    LOGGER.debug("Showing " + currentScreen.getClass().getSimpleName());
                    this.currentScreen.init();
                } else {
                    LOGGER.debug("Showing <<NO-SCENE>>");
                }
                return true;
            }
            LOGGER.debug("Hiding " + currentScreen.getClass().getSimpleName() + " canceled.");
        } else {
            LOGGER.debug("Hiding <<NO-SCENE>>");
            this.currentScreen = screen;
            if (screen != null) {
                Util.setCursor(screen.getDefaultCursor());
            } else {
                Util.setCursor(QBubbles.getInstance().getDefaultCursor());
            }

            if (currentScreen != null) {
                LOGGER.debug("Showing " + currentScreen.getClass().getSimpleName());
                this.currentScreen.init();
            } else {
                LOGGER.debug("Showing <<NO-SCENE>>");
            }
            return true;
        }
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
        this.currentScreen = this.startScreen;
        LOGGER.debug("Showing " + currentScreen.getClass().getSimpleName());
        Util.setCursor(this.startScreen.getDefaultCursor());
        this.startScreen.init();
    }

    @Nullable
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
