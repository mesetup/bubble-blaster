package com.ultreon.hydro;

import com.ultreon.commons.crash.CrashReport;
import com.ultreon.commons.crash.GameCrash;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.commons.lang.LoggableProgress;
import com.ultreon.hydro.entity.player.IPlayer;
import com.ultreon.hydro.entity.player.PlayerController;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.render.FilterApplier;
import com.ultreon.hydro.render.RenderSettings;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.resources.ResourceManager;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    static Game instance;
    private static boolean debugMode;
    private static boolean devMode;
    private final GameWindow gameWindow;
    private static int fps;
    private ScreenManager screenManager;
    private final RenderSettings renderSettings;
    public IPlayer iPlayer;
    private PlayerController playerController;
    private boolean loaded;
    private ResourceManager resourceManager;

    public static Game getInstance() {
        return instance;
    }

    public Game(GameWindow.Properties windowProperties) {
        // Assign instance.
        instance = this;

        renderSettings = new RenderSettings();

        // Setup game window.
        this.gameWindow = new GameWindow(windowProperties);

        // Prepare for loading.
        this.prepare();
//        this.gameWindow.init();

        // Load game with loading screen.
        this.load(new LoggableProgress(new InfoTransporter(this::log), 1000));
        this.screenManager = createScreenManager();
    }

    public static void initEngine(boolean debugMode, boolean devMode) {
        Game.debugMode = debugMode;
        Game.devMode = devMode;
    }

    private void log(String text) {
        // Todo: implement
    }

    /**
     * @return list of buffered image operations.
     * @implNote should be overridden for enabling / disabling render filters.
     */
    @NotNull
    public List<BufferedImageOp> getCurrentFilters() {
        return new ArrayList<>();
    }

    @SuppressWarnings("CommentedOutCode")
    void render0(Renderer renderer, int fps) {
        FilterApplier filterApplier = new FilterApplier(getBounds().getSize(), this.getObserver());
        Renderer filterRenderer = filterApplier.getRenderer();

        if (this.renderSettings.isAntialiasingEnabled() && this.isTextAntialiasEnabled())
            filterRenderer.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (this.renderSettings.isAntialiasingEnabled() && this.isAntialiasEnabled())
            filterRenderer.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<BufferedImageOp> filters =  Game.instance.getCurrentFilters();

        this.renderGame(filterRenderer);

        // Set filter gotten from filter event-handlers.
        filterApplier.setFilters(filters);

        // Draw filtered image.
        renderer.image(filterApplier.done(), 0, 0);

        // Disable Antialias
        // Todo: check performance.
//        if (renderSettings.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
//            renderer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//        if (renderSettings.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
//            renderer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        Game.fps = fps;
    }

    private void renderGame(Renderer renderer) {
        // Call to game environment rendering.
        renderGameEnv(renderer);

        // Get screen.
        @Nullable Screen screen = this.screenManager.getCurrentScreen();

        // Null check for screen variable.
        if (screen != null) {
            // Render the screen.
            screen.render(this, renderer);
            screen.renderGUI(this, renderer);
        }
    }

    public void tick0() {
        @Nullable Screen currentScreen = screenManager.getCurrentScreen();
        if (currentScreen != null) {
            currentScreen.tick(); // Todo: remove support someday.
        }

        this.tick();

//        if (environment != null) {
//            environment.tick();
//        }

        if (playerController != null) {
            playerController.tick();
        }

        // Call tick event.
        if (isLoaded() && (currentScreen == null || !currentScreen.doesPauseGame())) {
            GameEventBus.get().post(new TickEvent(this));
        }
    }

    /**
     * Handler for game crash.
     *
     * @param crash the game crash.
     */
    public void crash(GameCrash crash) {
        CrashReport crashReport = crash.getCrashReport();
        crashReport.defaultSave();
    }

    public abstract ScreenManager createScreenManager();

    /**
     * Todo: allow for adding multiple loading progresses.
     *
     * @implNote should always be overridden as protected.
     * @param mainProgress main loading progress.
     */
    protected abstract void load(LoggableProgress mainProgress);

    protected abstract void prepare();

    protected abstract void preparePlayer();

    protected abstract IPlayer createPlayer();

    protected abstract void renderGameEnv(Renderer renderer);

    protected abstract void tick();

    protected abstract void loadEnvironment();

    /////////////////////
    //     Getters     //
    /////////////////////
    public boolean isAntialiasEnabled() {
        return true;
    }

    public boolean isTextAntialiasEnabled() {
        return isAntialiasEnabled();
    }

    public final GameWindow getGameWindow() {
        return this.gameWindow;
    }

    public ImageObserver getObserver() {
        return gameWindow.observer;
    }

    public Rectangle getBounds() {
        return new Rectangle(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
    }

    public final int getWidth() {
        return gameWindow.getWidth();
    }

    public final int getHeight() {
        return gameWindow.getHeight();
    }

    public static int getFps() {
        return fps;
    }

    public Cursor getDefaultCursor() {
        return Cursor.getDefaultCursor();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public static boolean isDebugMode() {
        return Game.debugMode;
    }

    public static boolean isDevMode() {
        return Game.devMode;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void markLoaded() {
        this.loaded = true;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public RenderSettings getRenderSettings() {
        return renderSettings;
    }
}
