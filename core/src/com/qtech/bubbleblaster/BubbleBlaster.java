package com.qtech.bubbleblaster;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.qtech.bubbleblaster.addon.loader.AddonManager;
import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.common.References;
import com.qtech.bubbleblaster.common.Timer;
import com.qtech.bubbleblaster.common.crash.CrashReport;
import com.qtech.bubbleblaster.common.crash.ReportedException;
import com.qtech.bubbleblaster.common.scene.ScreenManager;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.common.streams.CustomOutputStream;
import com.qtech.bubbleblaster.core.InstrumentHook;
import com.qtech.bubbleblaster.core.common.SavedGame;
import com.qtech.bubbleblaster.core.utils.categories.FileUtils;
import com.qtech.bubbleblaster.entity.player.PlayerController;
import com.qtech.bubbleblaster.environment.Environment;
import com.qtech.bubbleblaster.environment.EnvironmentRenderer;
import com.qtech.bubbleblaster.event.ExitEvent;
import com.qtech.bubbleblaster.event.PauseTickEvent;
import com.qtech.bubbleblaster.event.TickEvent;
import com.qtech.bubbleblaster.event.bus.QBubblesEventBus;
import com.qtech.bubbleblaster.graphics.GraphicsEngine;
import com.qtech.bubbleblaster.init.GameTypes;
import com.qtech.bubbleblaster.media.AudioPlayer;
import com.qtech.bubbleblaster.screen.CrashScreen;
import com.qtech.bubbleblaster.screen.EnvLoadScreen;
import com.qtech.bubbleblaster.settings.GameSettings;
import com.qtech.bubbleblaster.util.Util;
import com.qtech.preloader.PreClassLoader;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Nullable;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The QBubbles game main class.
 *
 * @since 0.0.1-indev1
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "RedundantSuppression"})
public final class BubbleBlaster extends ApplicationAdapter {
    public static final int TPS = 20;

    // Initial game information / types.
    @Getter
    private static ClassLoader mainClassLoader;
    @Getter
    private static AudioPlayer audioPlayer;
    @Getter
    private static File gameDir = null;
    @Getter
    private static boolean debugMode;
    @Getter
    private static boolean devMode;

    // Number values.
    @Getter
    private static int fps;

    // Event-bus.
    private final QBubblesEventBus eventBus = new QBubblesEventBus();
    private final Instrumentation instrumentation;
    private final BufferedImage background = null;
    private final SpriteBatch batch;
    private final ShapeDrawer shapeRenderer;
    private final TextureRegion whitePixel;
    private final VfxManager vfxManager;
    private final FilmGrainEffect vfxEffect;
    @Nullable
    public Environment environment;
    private final PlayerController playerController = new PlayerController();

    // Fonts.
    @Getter
    private Font sansFont;
    @Getter
    private Font monospaceFont;
    @Getter
    private Font pixelFont;
    @Getter
    private Font gameFont;

    // Font names.
    @Getter
    private final String fontName;

    // Managers.
    @Getter
    private final AddonManager addonManager = AddonManager.getInstance();
    @Getter
    private final ScreenManager screenManager = ScreenManager.getInstance();

    // Logger.
    @Getter
    private static final Logger logger = LogManager.getLogger("QB:Generic");

    // Running value.
    @Getter
    private volatile boolean running = false;

    // Randomizers.
    private final Random random = new Random();
    private final EnvironmentRenderer environmentRenderer = new EnvironmentRenderer();
    private final List<Runnable> tasks = new CopyOnWriteArrayList<>();
    private LoadedGame loadedGame;
    private static long ticks = 0L;
    private int width;
    private int height;
    private final GraphicsEngine graphicsEngine = new GraphicsEngine();
    private double time;
    private double unprocessed;
    private double frameTime;
    private final double tickCap;
    private int frames;
    private Cursor defaultCursor;
    private Cursor blankCursor;

    // Graphical getters.
    public static double getMiddleX() {
        return (double) getInstance().getWidth() / 2;
    }

    public static double getMiddleY() {
        return (double) getInstance().getHeight() / 2;
    }

    public static Point2D getMiddlePoint() {
        return new Point2D.Double(getMiddleX(), getMiddleY());
    }

    public static long getTicks() {
        return ticks;
    }

    public static byte reduceTicks2Secs(byte value, byte seconds) {
        return (byte) ((double) value / ((double) TPS * seconds));
    }

    public static short reduceTicks2Secs(short value, short seconds) {
        return (short) ((double) value / ((double) TPS * seconds));
    }

    public static int reduceTicks2Secs(int value, int seconds) {
        return (int) ((double) value / ((double) TPS * seconds));
    }

    public static long reduceTicks2Secs(long value, long seconds) {
        return (long) ((double) value / ((double) TPS * seconds));
    }

    public static float reduceTicks2Secs(float value, float seconds) {
        return (float) ((double) value / ((double) TPS * seconds));
    }

    public static double reduceTicks2Secs(double value, double seconds) {
        return value / ((double) TPS * seconds);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPaused() {
        Screen currentScreen = getInstance().getCurrentScreen();
        return currentScreen != null && currentScreen.doesPauseGame();
    }

    // Font-getters.
    public String getGameFontName() {
        return getGameFont().getFontName();
    }

    public String getPixelFontName() {
        return getPixelFont().getFontName();
    }

    public String getMonospaceFontName() {
        return getMonospaceFont().getFontName();
    }

    public String getSansFontName() {
        return getSansFont().getFontName();
    }

    // Threads
    @Getter
    private Thread renderThread;
    @Getter
    private Thread mainThread;
    @Getter
    private Thread gcThread;

    // Instance property.
    @Getter
    private static BubbleBlaster instance;

    public static Instrumentation getInstrumentation() {
        return instance.instrumentation;
    }

    /**
     * Get event bus.
     *
     * @return The qbubbles event bus.
     */
    public static QBubblesEventBus getEventBus() {
        return getInstance().eventBus;
    }

    // Constructor.
    public BubbleBlaster() throws IOException {
        HdpiUtils.setMode(HdpiMode.Pixels);

        this.whitePixel = new TextureRegion(new Texture("resources/texture/white.png"));

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeDrawer(batch, whitePixel);

        // VfxManager is a host for the effects.
        // It captures rendering into internal off-screen buffer and applies a chain of defined effects.
        // Off-screen buffers may have any pixel format, for this example we will use RGBA8888.
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        // Create and add an effect.
        // VfxEffect derivative classes serve as controllers for the effects.
        // They provide public properties to configure and control them.
        vfxEffect = new FilmGrainEffect();
        vfxManager.addEffect(vfxEffect);

        tickCap = 1d / (double) TPS;
        frameTime = 0d;
        frames = 0;

        time = Timer.getTime();
        unprocessed = 0;

        // Assign instance.
        BubbleBlaster.instance = this;

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().equals("JavaFX Application Thread")) {
                thread.setName("QBubblesApp");
            }
            if (thread.getName().equals("AWT-EventQueue-0")) {
                thread.setName("CoreEventBus");
            }
        }

        // Add ansi color compatibility in console.
        AnsiConsole.systemInstall();
        FileUtils.setCwd(References.QBUBBLES_DIR);
//
//        // Transparent 16 x 16 pixel cursor image.
//        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
//
//        // Create a new blank cursor.
//        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg, new Point(0, 0), "blank cursor");
//
//        // Transparent 16 x 16 pixel cursor image.
//        BufferedImage cursorImg1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        Polygon polygon = new Polygon(new int[]{0, 10, 5, 0}, new int[]{0, 12, 12, 16}, 4);
//
//        GraphicsProcessor gg = cursorImg1.createGraphics();
//        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        gg.setColor(Color.black);
//        gg.fillPolygon(polygon);
//        gg.setColor(Color.white);
//        gg.drawPolygon(polygon);
//        gg.dispose();
//
//        // Create a new blank cursor.
//        defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg1, new Point(1, 1), "default cursor");
//
//        // Transparent 16 x 16 pixel cursor image.
//        BufferedImage cursorImg2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        Polygon polygon2 = new Polygon(new int[]{10, 20, 15, 10}, new int[]{10, 22, 22, 26}, 4);
//
//        GraphicsProcessor gg2 = cursorImg2.createGraphics();
//        gg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        gg2.setColor(Color.white);
//        gg2.drawOval(0, 0, 20, 20);
//        gg2.setColor(Color.white);
//        gg2.drawOval(2, 2, 16, 16);
//        gg2.setColor(Color.black);
//        gg2.fillPolygon(polygon2);
//        gg2.setColor(Color.white);
//        gg2.drawPolygon(polygon2);
//        gg2.setColor(Color.black);
//        gg2.drawOval(1, 1, 18, 18);
//        gg2.dispose();
//
//        // Create a new blank cursor.
//        pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg2, new Point(11, 11), "pointer cursor");
//
//        // Transparent 16 x 16 pixel cursor image.
//        BufferedImage cursorImg3 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
//        GraphicsProcessor gg3 = cursorImg3.createGraphics();
//        gg3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        gg3.setColor(Color.white);
//        gg3.drawLine(0, 1, 0, 24);
//        gg3.setColor(Color.white);
//        gg3.drawLine(1, 0, 1, 25);
//        gg3.setColor(Color.white);
//        gg3.drawLine(2, 1, 2, 24);
//        gg3.setColor(Color.black);
//        gg3.drawLine(1, 1, 1, 24);
//        gg3.dispose();
//
//        // Create a new blank cursor.
//        textCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//                cursorImg3, new Point(1, 12), "text cursor");

        Thread.setDefaultUncaughtExceptionHandler(new QBubblesExceptionHandler());

        // Hook output for logger.
        System.setErr(new PrintStream(new CustomOutputStream("STDERR", Level.ERROR)));
        System.setOut(new PrintStream(new CustomOutputStream("STDOUT", Level.INFO)));
        // Instrumentation Hook.
        instrumentation = InstrumentHook.getInstrumentation(); // Probably won't work

        // Logs directory creation.
        References.LOGS_DIR.mkdirs();

        // Font Name
        fontName = "Chicle Regular";

        // Register Game Font.
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            gameFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster.class.getResourceAsStream("/assets/qbubbles/fonts/ChicleRegular-xpv5.ttf")));
            ge.registerFont(getGameFont());
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster.class.getResourceAsStream("/assets/qbubbles/fonts/pixel/Pixel-UniCode.ttf")));
            ge.registerFont(getPixelFont());
            monospaceFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster.class.getResourceAsStream("/assets/qbubbles/fonts/dejavu/DejaVuSansMono.ttf")));
            ge.registerFont(getMonospaceFont());
            sansFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(BubbleBlaster.class.getResourceAsStream("/assets/qbubbles/fonts/arial-unicode-ms.ttf")));
            ge.registerFont(getSansFont());
        } catch (FontFormatException | NullPointerException e) {
            if (e instanceof NullPointerException) {
                System.err.println("Couldn't load fonts.");
            }
            e.printStackTrace();
        }

        // Register event bus.
        this.eventBus.register(this);

        start();
    }

    public void gcThread() {
        System.gc();
    }

    /**
     * Starts game-thread.
     */
    public void start() {
        // Mark the game as running.
        running = true;

        // Startup the main thread.
        //noinspection Convert2Lambda,Anonymous2MethodRef,Anonymous2MethodRef
        mainThread = new Thread(new Runnable() {
            public void run() {
                BubbleBlaster.this.mainThread();
            }
        }, "TickThread");
        getMainThread().start();

        // Start scene-manager.
        try {
            Objects.requireNonNull(this.getScreenManager()).start();
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Oops, game crashed!", t);
            throw crashReport.getReportedException();
        }
    }

    /**
     * Stops game-thread.
     */
    public synchronized void stop() {
        try {
            getRenderThread().join();
            getMainThread().join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
    }

    public void close() {
        // Shut-down game.
        getLogger().warn("QBubbles is now shutting down...");

        // Check for exit events.
        checkForExitEvents();

        // Stop thread, when fails: interrupt thread, when that also fails show error.
        try {
            // Interrupt thread.
            if (getMainThread().isAlive() && Thread.currentThread() != getMainThread()) {
                getMainThread().interrupt();
                getLogger().info("Interrupted main thread.");
            }
        } catch (SecurityException ignore) {
            getLogger().error("Cannot stop and interrupt game-thread because of security exceptions.");
        }

        System.exit(0);
    }

    private void checkForExitEvents() {
        eventBus.post(new ExitEvent());
    }

    private void mainThread() {
        try {
            while (running) {
                boolean canTick = false;

                double time2 = Timer.getTime();
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
                        tick();
                    } catch (Throwable t) {
                        CrashReport crashReport = new CrashReport("Game being ticked.", t);
                        throw crashReport.getReportedException();
                    }
                }

                if (frameTime >= 1.0d) {
                    frameTime = 0;
                    fps = Math.round(frames);
                    frames = 0;
                }

                frames++;

                try {
                    render();
                } catch (Throwable t) {
                    CrashReport crashReport = new CrashReport("Game being rendered.", t);
                    throw crashReport.getReportedException();
                }

                for (Runnable task : tasks) {
                    task.run();
                }

                tasks.clear();
            }
        } catch (ReportedException e) {
            e.printStackTrace();
            displayScene(new CrashScreen(e.getCrashReport()), true);
            //noinspection StatementWithEmptyBody
            while (running) Thread.onSpinWait();
        }

        close();
    }

    @Override
    public void render() {
        boolean canTick = false;

        double time2 = Timer.getTime();
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
                tick();
            } catch (Throwable t) {
                CrashReport crashReport = new CrashReport("Game being ticked.", t);
                throw crashReport.getReportedException();
            }
        }

        if (frameTime >= 1.0d) {
            frameTime = 0;
            fps = Math.round(frames);
            frames = 0;
        }

        frames++;

        try {
            render1();
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Game being rendered.", t);
            throw crashReport.getReportedException();
        }

        for (Runnable task : tasks) {
            task.run();
        }

        tasks.clear();
    }

    /**
     * Update method, for updating values and doing things.
     */
    @SuppressWarnings("SameParameterValue")
    private void tick() {
        ticks++;

        @Nullable Screen currentScreen = Util.getSceneManager().getCurrentScreen();
        if (currentScreen != null) {
            currentScreen.tick();
        }
        if (environment != null) {
            environment.tick();
        }

        playerController.tick();

        // Call tick event.
        if (isGameLoaded() && (currentScreen == null || !currentScreen.doesPauseGame())) {
            eventBus.post(new TickEvent(this));
        } else {
            eventBus.post(new PauseTickEvent(this));
        }
    }

    /**
     * Render method, for rendering window.
     */
    @SuppressWarnings("ConstantConditions")
    public void render1() {
        GraphicsProcessor gg = new GraphicsProcessor(batch, shapeRenderer);

        renderScreenEnv(gg);

        if (graphicsEngine.isAntialiasingEnabled()) {

        }

        if (getScreenManager().getCurrentScreen() != null)
            getScreenManager().getCurrentScreen().renderGUI(this, gg);

        // Disable Antialias
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Dispose and show.
        gg.dispose();
    }

    private void renderScreenEnv(GraphicsProcessor gg) {
        @Nullable Screen screen = screenManager.getCurrentScreen();
        if (screen != null) {
            // Clear background.
            gg.setBackground(Color.BLACK);
            gg.clearRect(0, 0, getWidth(), getHeight());

            if (environment != null) {
                environmentRenderer.render(gg);
            }

            screen.render(this, gg);
        } else {
            // Clear background
            gg.setBackground(new Color(0, 0, 0));
            gg.clearRect(0, 0, getWidth(), getHeight());

            if (environment != null) {
                environmentRenderer.render(gg);
            }
        }
    }

    /**
     * Launch method
     */
    public static void main(String[] args, PreClassLoader mainClassLoader) {
        // Get game-directory.
        for (String arg : args) {
            if (arg.startsWith("gameDir=")) {
                BubbleBlaster.gameDir = new File(arg.substring(8));
            }
            if (arg.equals("--debug")) {
                BubbleBlaster.debugMode = true;
            }
            if (arg.equals("--dev")) {
                BubbleBlaster.devMode = true;
            }
        }

        // Check if game-dir is assigned, if not the game-dir is not specified in the arguments.
        if (getGameDir() == null) {
            System.err.println("Game Directory is not specified!");
            System.exit(1);
        }

        BubbleBlaster.mainClassLoader = mainClassLoader;

        // Boot the game.
        try {
            new BubbleBlaster();
        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(1);
        }
    }

    @SuppressWarnings("EmptyMethod")
    public void loadSave(String saveName) {

    }

    @SuppressWarnings("EmptyMethod")
    public void createAndLoadSave(String saveName) {

    }

    @Nullable
    public SavedGame getCurrentSave() {
        LoadedGame loadedGame = getLoadedGame();
        if (loadedGame != null) {
            return loadedGame.getSavedGame();
        }
        return null;
    }

    public void displayScene(@Nullable Screen scene) {
        this.screenManager.displayScreen(scene);
    }

    public void displayScene(Screen scene, boolean force) {
        this.screenManager.displayScreen(scene, force);
    }

    public @Nullable Screen getCurrentScreen() {
        return this.screenManager.getCurrentScreen();
    }

    @Nullable
    public Environment getEnvironment() {
        return environment;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isOnMainThread() {
        return Thread.currentThread() == mainThread;
    }

    public boolean isOnRenderThread() {
        return Thread.currentThread() == renderThread;
    }

    public void runLater(Runnable runnable) {
        this.tasks.add(runnable);
    }

    public void loadGame() {
        AtomicReference<LoadedGame> loadedGameReference = new AtomicReference<>(loadedGame);
        displayScene(new EnvLoadScreen(SavedGame.fromFile(new File(References.SAVES_DIR, "save")), GameTypes.CLASSIC_TYPE::get, loadedGameReference));
        loadedGame = loadedGameReference.get();
    }

    public LoadedGame getLoadedGame() {
        return loadedGame;
    }

    public void quitLoadedGame() {
        LoadedGame loadedGame = getLoadedGame();
        if (loadedGame != null) {
            loadedGame.quit();
        }
    }

    @Override
    public void resize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public boolean isGameLoaded() {
        return getLoadedGame() != null;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    @Deprecated
    public Cursor getDefaultCursor() {
        return defaultCursor;
    }

    @Deprecated
    public Cursor getBlankCursor() {
        return blankCursor;
    }
}
