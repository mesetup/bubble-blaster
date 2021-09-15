package com.ultreon.hydro.screen;

import com.ultreon.commons.annotation.FieldsAreNonnullByDefault;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.common.ResourceEntry;
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
    private static ScreenManager instance;
    private static final Logger LOGGER = LogManager.getLogger("Screen-Manager");
    private final Screen startScreen;
    private final Game game;

    @Nullable
    private Screen currentScreen;
    private boolean initialized = false;

    public static ScreenManager getInstance() {
        return instance;
    }

    private ScreenManager(Screen startScreen) {
        instance = this;
        this.game = Game.getInstance();
        this.currentScreen = this.startScreen = startScreen;
    }

    public static ScreenManager create(Screen start) {
        return new ScreenManager(start);
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
                return initNextScreen(screen);
            }
            LOGGER.debug("Hiding " + currentScreen.getClass().getSimpleName() + " canceled.");
        } else {
            LOGGER.debug("Hiding <<NO-SCENE>>");
            return initNextScreen(screen);
        }
        return false;
    }

    private boolean initNextScreen(@Nullable Screen screen) {
        this.currentScreen = screen;
        if (screen != null) {
            game.getGameWindow().setCursor(screen.getDefaultCursor());
        } else {
            game.getGameWindow().setCursor(game.getDefaultCursor());
        }

        if (currentScreen != null) {
            LOGGER.debug("Showing " + currentScreen.getClass().getSimpleName());
            this.currentScreen.init();
        } else {
            LOGGER.debug("Showing <<NO-SCENE>>");
        }
        return true;
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
    public ResourceEntry getCurrentSceneKey() {
        return null;
    }

    public synchronized void start() {
        if (initialized) {
            throw new IllegalStateException("SceneManager already initialized.");
        }

        this.initialized = true;
        this.currentScreen = this.startScreen;
        LOGGER.debug("Showing " + currentScreen.getClass().getSimpleName());
        game.getGameWindow().setCursor(this.startScreen.getDefaultCursor());
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
