package com.ultreon.hydro;

import com.ultreon.commons.crash.CrashLog;
import com.ultreon.commons.crash.ApplicationCrash;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.commons.lang.LoggableProgress;
import com.ultreon.commons.time.TimeProcessor;
import com.ultreon.hydro.event.RenderGameEvent;
import com.ultreon.hydro.event.RenderScreenEvent;
import com.ultreon.hydro.player.IPlayer;
import com.ultreon.hydro.player.PlayerController;
import com.ultreon.hydro.event.TickEvent;
import com.ultreon.hydro.event.bus.GameEvents;
import com.ultreon.hydro.render.FilterApplier;
import com.ultreon.hydro.render.RenderSettings;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.resources.ResourceManager;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Game {
    static Game instance;

    // Values
    private int fps;
    private final int tps;

    // Modes
    private static boolean debugMode;
    private static boolean devMode;

    // Utility objects.
    public IPlayer playerInterface;
    private PlayerController playerController;
    protected final ResourceManager resourceManager;
    protected final GameWindow gameWindow;
    protected final ScreenManager screenManager;
    protected final RenderSettings renderSettings;

    // Game states.
    private boolean loaded;
    private boolean crashed;
    private volatile boolean running = false;

    // Tasks
    private final List<Runnable> tasks = new CopyOnWriteArrayList<>();

    // Threads
    protected Thread mainThread;

    public static Game getInstance() {
        return instance;
    }

    public Game(GameWindow.Properties windowProperties, BootOptions bootOptions) {
        // Set default uncaught exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new GameExceptionHandler(this));

        // Assign instance.
        instance = this;

        // Set game properties.
        this.tps = bootOptions.tps;

        // Prepare for game launch
        this.resourceManager = new ResourceManager();
        this.renderSettings = new RenderSettings();

        // Setup game window.
        this.gameWindow = new GameWindow(windowProperties);

        // Prepare for loading.
        this.prepare();

//        this.gameWindow.init();

        // Load game with loading screen.
        this.load(new LoggableProgress(new InfoTransporter(this::log), 1000));
        this.screenManager = createScreenManager();
    }

    public final void scheduleTask(Runnable task) {
        this.tasks.add(task);
    }

    /**
     * Initialize the game engine.
     *
     * @param debugMode debug mode enables debugging utilities.
     * @param devMode dev mode enabled development utilities.
     */
    public static void initEngine(boolean debugMode, boolean devMode) {
        Game.debugMode = debugMode;
        Game.devMode = devMode;
    }

    private void log(String text) {
        // Todo: implement
    }

    /**
     * This method should be overridden when filters are used in the game.
     *
     * @return list of buffered image operations.
     * @implNote should be overridden for enabling / disabling render filters.
     */
    @NotNull
    public List<BufferedImageOp> getCurrentFilters() {
        return new ArrayList<>();
    }

    public abstract ScreenManager createScreenManager();

    protected abstract int getMainLoadingSteps();

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

    protected abstract void render(Renderer renderer);

    protected abstract void tick();

    protected abstract void loadEnvironment();

    public void loadPlayEnvironment() {
        this.playerInterface = createPlayer();
        this.playerController = new PlayerController(this.playerInterface);

        loadEnvironment();
    }

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

    public int getFps() {
        return fps;
    }

    public Cursor getDefaultCursor() {
        return Cursor.getDefaultCursor();
    }

    public abstract void onClose();

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

    protected void markLoaded() {
        this.loaded = true;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public RenderSettings getRenderSettings() {
        return renderSettings;
    }

    private void mainThread() {
        double tickCap = 1d / (double) tps;
        double frameTime = 0d;
        double frames = 0;

        double time = TimeProcessor.getTime();
        double unprocessed = 0;

        try {
            while (running) {
                boolean canTick = false;

                double time2 = TimeProcessor.getTime();
                double passed = time2 - time;
                unprocessed += passed;
                frameTime += passed;

                time = time2;

                while (unprocessed >= tickCap) {
                    unprocessed -= tickCap;

                    canTick = true;
                }

                if (canTick) {
                    try {
                        internalTick();
                    } catch (Throwable t) {
                        CrashLog crashLog = new CrashLog("Game being ticked.", t);
                        throw crashLog.createCrash();
                    }
                }

                if (frameTime >= 1.0d) {
                    frameTime = 0;
                    fps = (int) Math.round(frames);
                    frames = 0;
                }

                frames++;

                try {
                    filteredRender(fps);
                } catch (Throwable t) {
                    CrashLog crashLog = new CrashLog("Game being rendered.", t);
                    throw crashLog.createCrash();
                }

                for (Runnable task : tasks) {
                    task.run();
                }

                tasks.clear();
            }
        } catch (ApplicationCrash e) {
            e.printStackTrace();

            Game.instance.crash(e);
//            showScreen(new CrashScreen(e.getCrashReport()), true);
            while (running) Thread.onSpinWait();
        }

        close();
    }

    /**
     *
     */
    private void internalTick() {
        @Nullable Screen currentScreen = screenManager.getCurrentScreen();

        if (playerController != null) {
            playerController.tick();
        }

        this.tick();

        // Call tick event.
        if (isLoaded() && (currentScreen == null || !currentScreen.doesPauseGame())) {
            GameEvents.get().publish(new TickEvent(this));
        }
    }

    /**
     * Render method, for rendering window.
     * @param fps current game framerate.
     */
    private void filteredRender(int fps) {
        if (!gameWindow.isInitialized()) return;

        // Buffer strategy (triple buffering).
        BufferStrategy bs = this.gameWindow.canvas.getBufferStrategy();

        // Create buffers if not created yet.
        if (bs == null) {
            this.gameWindow.canvas.createBufferStrategy(2);
            bs = this.gameWindow.canvas.getBufferStrategy();
        }

        // Get GraphicsProcessor and GraphicsProcessor objects.
        Renderer renderer = new Renderer(bs.getDrawGraphics());

        FilterApplier filterApplier = new FilterApplier(getBounds().getSize(), this.getObserver());
        Renderer filterRenderer = filterApplier.getRenderer();

        if (this.renderSettings.isAntialiasingEnabled() && this.isTextAntialiasEnabled())
            filterRenderer.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (this.renderSettings.isAntialiasingEnabled() && this.isAntialiasEnabled())
            filterRenderer.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<BufferedImageOp> filters =  Game.instance.getCurrentFilters();

        this.filteredRender(filterRenderer);

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

        this.fps = fps;

        // Dispose and show.
        renderer.dispose();

        try {
            bs.show();
        } catch (IllegalStateException e) {
//            close();
        }
    }

    /**
     *
     * @param renderer the renderer to render the game with.
     */
    private void filteredRender(Renderer renderer) {

        // Call to game environment rendering.
        GameEvents.get().publish(new RenderGameEvent.Before(renderer));
        render(renderer);
        GameEvents.get().publish(new RenderGameEvent.After(renderer));

        // Get screen.
        @Nullable Screen screen = this.screenManager.getCurrentScreen();

        // Null check for screen variable.
        if (screen != null) {

            // Render the screen.
            GameEvents.get().publish(new RenderScreenEvent.Before(screen, renderer));
            screen.render(this, renderer);
            screen.renderGUI(this, renderer);
            GameEvents.get().publish(new RenderScreenEvent.After(screen, renderer));
        }
    }

    @SneakyThrows
    public void shutdown() {
        this.running = false;
        mainThread.join();
        close();
    }

    protected final void close() {
        onClose();
        gameWindow.close();
        if (crashed) {
            System.exit(0);
        }
    }

    /**
     * Handler for game crash.
     *
     * @param crash the game crash.
     */
    public void crash(ApplicationCrash crash) {
        CrashLog crashLog = crash.getCrashLog();
        crashed = true;

        boolean overridden = false;
        try {
            overridden = onCrash(crash);
        } catch (Throwable ignored) {

        }

        if (!overridden) {
            crashLog.defaultSave();
            crashLog.createCrash().printCrash();
        }
    }

    /**
     * Crash handler, for overriding default crash handling.
     *
     * @param crash the crash that happened.
     * @return whether the default should be overridden or not. The value {@code }
     */
    @SuppressWarnings("unused")
    public boolean onCrash(ApplicationCrash crash) {
        return false;
    }

    @SuppressWarnings("Convert2Lambda")
    void windowLoaded() {
        this.mainThread = new Thread(new Runnable() {
            public void run() {
                running = true;
                Game.this.mainThread();
            }
        }, "Main-Thread");
        this.mainThread.start();
    }

    public boolean isMainAlive() {
        return mainThread.isAlive();
    }

    public Cursor getPointerCursor() {
        return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }

    protected static class BootOptions {
        private int tps = 20;

        public BootOptions() {

        }

        public BootOptions tps(int tps) {
            this.tps = tps;
            return this;
        }
    }
}
