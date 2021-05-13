package com.qsoftware.bubbles;

import com.qsoftware.bubbles.addon.loader.AddonManager;
import com.qsoftware.bubbles.common.References;
import com.qsoftware.bubbles.common.Timer;
import com.qsoftware.bubbles.common.crash.CrashReport;
import com.qsoftware.bubbles.common.crash.ReportedException;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.common.scene.SceneManager;
import com.qsoftware.bubbles.common.streams.CustomOutputStream;
import com.qsoftware.bubbles.core.InstrumentHook;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.core.utils.categories.FileUtils;
import com.qsoftware.bubbles.entity.player.PlayerController;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.environment.EnvironmentRenderer;
import com.qsoftware.bubbles.event.ExitEvent;
import com.qsoftware.bubbles.event.FilterEvent;
import com.qsoftware.bubbles.event.PauseTickEvent;
import com.qsoftware.bubbles.event.TickEvent;
import com.qsoftware.bubbles.event.bus.QBubblesEventBus;
import com.qsoftware.bubbles.graphics.FilterApplier;
import com.qsoftware.bubbles.graphics.GraphicsEngine;
import com.qsoftware.bubbles.gui.Window;
import com.qsoftware.bubbles.media.AudioPlayer;
import com.qsoftware.bubbles.scene.CrashScene;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.settings.GameSettings;
import com.qsoftware.bubbles.util.Util;
import com.qsoftware.preloader.PreClassLoader;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import org.jdesktop.swingx.JXFrame;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The QBubbles game main class.
 *
 * @since 0.0.1-indev1
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "RedundantSuppression"})
public final class QBubbles extends Canvas {
    private static final int TPS = 20;

    // Initial game information / types.
    @Getter
    private static ClassLoader mainClassLoader;
    @Getter
    private static AudioPlayer audioPlayer;
    @Getter
    private static File gameDir = null;
    @Getter
    private static boolean debug;
    @Getter
    private static boolean devMode;

    // Number values.
    @Getter
    private static int fps;

    // Event-bus.
    private final QBubblesEventBus eventBus = new QBubblesEventBus();
    private final Instrumentation instrumentation;
    private final BufferedImage background = null;
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

    // Cursors
    @Getter
    private final Cursor blankCursor;
    @Getter
    private final Cursor defaultCursor;
    @Getter
    private final Cursor pointerCursor;
    @Getter
    private final Cursor textCursor;

    // Font names.
    @Getter
    private final String fontName;

    // Graphics / GUI.
    @Getter
    private final GraphicsEngine graphicsEngine;
    @Getter
    private final Window window;

    // Managers.
    @Getter
    private final AddonManager addonManager = AddonManager.getInstance();
    @Getter
    private final SceneManager sceneManager = SceneManager.getInstance();

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

    // GUI getters.
    public JXFrame getFrame() {
        return getWindow().getFrame();
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
    private static QBubbles instance;

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
    public QBubbles() throws IOException {
        // Assign instance.
        QBubbles.instance = this;

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

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Polygon polygon = new Polygon(new int[]{0, 10, 5, 0}, new int[]{0, 12, 12, 16}, 4);

        Graphics2D gg = cursorImg1.createGraphics();
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setColor(Color.black);
        gg.fillPolygon(polygon);
        gg.setColor(Color.white);
        gg.drawPolygon(polygon);
        gg.dispose();

        // Create a new blank cursor.
        defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg1, new Point(1, 1), "default cursor");

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Polygon polygon2 = new Polygon(new int[]{10, 20, 15, 10}, new int[]{10, 22, 22, 26}, 4);

        Graphics2D gg2 = cursorImg2.createGraphics();
        gg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg2.setColor(Color.white);
        gg2.drawOval(0, 0, 20, 20);
        gg2.setColor(Color.white);
        gg2.drawOval(2, 2, 16, 16);
        gg2.setColor(Color.black);
        gg2.fillPolygon(polygon2);
        gg2.setColor(Color.white);
        gg2.drawPolygon(polygon2);
        gg2.setColor(Color.black);
        gg2.drawOval(1, 1, 18, 18);
        gg2.dispose();

        // Create a new blank cursor.
        pointerCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg2, new Point(11, 11), "pointer cursor");

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg3 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg3 = cursorImg3.createGraphics();
        gg3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg3.setColor(Color.white);
        gg3.drawLine(0, 1, 0, 24);
        gg3.setColor(Color.white);
        gg3.drawLine(1, 0, 1, 25);
        gg3.setColor(Color.white);
        gg3.drawLine(2, 1, 2, 24);
        gg3.setColor(Color.black);
        gg3.drawLine(1, 1, 1, 24);
        gg3.dispose();

        // Create a new blank cursor.
        textCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg3, new Point(1, 12), "text cursor");

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
            gameFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(QBubbles.class.getResourceAsStream("/assets/qbubbles/fonts/ChicleRegular-xpv5.ttf")));
            ge.registerFont(getGameFont());
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(QBubbles.class.getResourceAsStream("/assets/qbubbles/fonts/pixel/Pixel-UniCode.ttf")));
            ge.registerFont(getPixelFont());
            monospaceFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(QBubbles.class.getResourceAsStream("/assets/qbubbles/fonts/dejavu/DejaVuSansMono.ttf")));
            ge.registerFont(getMonospaceFont());
            sansFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(QBubbles.class.getResourceAsStream("/assets/qbubbles/fonts/arial-unicode-ms.ttf")));
            ge.registerFont(getSansFont());
        } catch (FontFormatException | NullPointerException e) {
            if (e instanceof NullPointerException) {
                System.err.println("Couldn't load fonts.");
            }
            e.printStackTrace();
        }

        // Create instance field in class.

        this.graphicsEngine = new GraphicsEngine();

        // Internal canvas.
        this.window = new Window(WIDTH, HEIGHT, "QBubbles", this);
        this.window.setCursor(defaultCursor);

        // Request focus
        requestFocus();

        // Set font.
        this.setFont(sansFont);

        // Register event bus.
        this.eventBus.register(this);

        // Start scene-manager.
        try {
            Objects.requireNonNull(this.getSceneManager()).start();
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Oops, game crashed!", t);
            throw crashReport.getReportedException();
        }
    }

    public void gcThread() {
        System.gc();
    }

    /**
     * Starts game-thread.
     */
    public void start(Window window) {
        running = true;

        this.setVisible(true);

        if (getBounds().getMinX() > getBounds().getMaxX() || getBounds().getMinY() > getBounds().getMaxY()) {
            setBounds(window.getBounds());
        }

        if (getBounds().getMinX() == getBounds().getMaxX() || getBounds().getMinY() == getBounds().getMaxY()) {
            setBounds(window.getBounds());
        }

        if (getBounds().getMinX() > getBounds().getMaxX() || getBounds().getMinY() > getBounds().getMaxY()) {
            throw new IllegalStateException("Game bounds is invalid: negative size");
        }

        if (getBounds().getMinX() == getBounds().getMaxX() || getBounds().getMinY() == getBounds().getMaxY()) {
            throw new IllegalStateException("Game bounds is invalid: zero size");
        }

        logger.debug(this.isVisible());

//        //noinspection Convert2Lambda,Anonymous2MethodRef,Anonymous2MethodRef
//        renderThread = new Thread(new Runnable() {public void run() {
//            QBubbles.this.renderThread();}}, "RenderThread");
//        getRenderThread().start();

        //noinspection Convert2Lambda,Anonymous2MethodRef,Anonymous2MethodRef
        mainThread = new Thread(new Runnable() {
            public void run() {
                QBubbles.this.mainThread();
            }
        }, "TickThread");
        getMainThread().start();
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
//            if (getRenderThread().isAlive() && Thread.currentThread() != getRenderThread()) {
//                getRenderThread().interrupt();
//                getLogger().info("Interrupted render thread.");
//            }
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
        double tickCap = 1d / (double) TPS;
        double frameTime = 0d;
        double frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

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
                        tick(1);
                    } catch (Throwable t) {
                        CrashReport crashReport = new CrashReport("Game being ticked.", t);
                        throw crashReport.getReportedException();
                    }
                }

                if (frameTime >= 1.0d) {
                    frameTime = 0;
                    fps = (int) Math.round(frames);
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
            displayScene(new CrashScene(e.getCrashReport()), true);
            //noinspection StatementWithEmptyBody
            while (running) ;
        }

        close();
    }

    /**
     * Update method, for updating values and doing things.
     *
     * @param delta the delta-time for calculating speed for tick/
     */
    @SuppressWarnings("SameParameterValue")
    private void tick(double delta) {
        Scene currentScene = Objects.requireNonNull(Util.getSceneManager()).getCurrentScene();
        if (currentScene != null) {
            currentScene.tick();
        }
        if (environment != null) {
            environment.tick();
        }

        playerController.tick();

        // Call tick event.
        if (currentScene instanceof GameScene && !((GameScene) currentScene).isPaused()) {
            eventBus.post(new TickEvent(this, SceneManager.getInstance().getCurrentScene(), delta));
        } else if (!(sceneManager.getCurrentScene() instanceof GameScene)) {
            eventBus.post(new TickEvent(this, SceneManager.getInstance().getCurrentScene(), delta));
        } else {
            eventBus.post(new PauseTickEvent(this, SceneManager.getInstance().getCurrentScene(), delta));
        }
    }

    private void backgroundAnimationThread() {
//        CausticsFilter filter = new CausticsFilter();
//        filter.setScale(50f);
//        BufferedImage image = new BufferedImage(Game.instance().getWidth(), Game.instance().getHeight(), BufferedImage.TYPE_INT_ARGB);
//        while (running) {
//            filter.setTime((float) ((double) System.nanoTime() / 10_000_000_000d % 3_600d));
//            filter.filter(image, image);
//
//            background = image;
//        }
    }

    /**
     * Render method, for rendering window.
     */
    @SuppressWarnings("ConstantConditions")
    private void render() {
        // Buffer strategy (triple buffering).
        BufferStrategy bs = this.getBufferStrategy();

        // Create buffers if not created yet.
        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        // Get Graphics and Graphics2D objects.
        Graphics g = bs.getDrawGraphics();
        Graphics2D gg = (Graphics2D) g;

        FilterApplier filterApplier = new FilterApplier(getBounds().getSize(), this);
        Graphics2D g2d = filterApplier.getGraphics();

        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        FilterEvent filterEvent = new FilterEvent();

        eventBus.post(filterEvent);

        renderScene(g2d);

        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Set filter gotten from filter event-handlers.
        filterApplier.setFilters(filterEvent.getFilters());

        // Draw filtered image.
        gg.drawImage(filterApplier.done(), 0, 0, this);

        // Enable Antialias
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getSceneManager().getCurrentScene() != null)
            getSceneManager().getCurrentScene().renderGUI(this, gg);

        // Disable Antialias
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isTextAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        if (graphicsEngine.isAntialiasingEnabled() && GameSettings.instance().isAntialiasEnabled())
            gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Dispose and show.
        g.dispose();
        bs.show();
    }

    private void renderScene(Graphics2D gg) {
        Scene scene = sceneManager.getCurrentScene();
        if (scene != null) {
            // Reset background.
            gg.setColor(Color.BLACK);
            gg.fillRect(0, 0, WIDTH, HEIGHT);

            // Clear background.
            gg.setBackground(new Color(0, 120, 160));
            gg.clearRect(0, 0, getWidth(), getHeight());

            if (environment != null) {
                environmentRenderer.render(gg);
            }

            scene.render(this, gg);
        } else {
            // Reset background.
            gg.setColor(new Color(64, 64, 64));
            gg.fillRect(0, 0, WIDTH, HEIGHT);

            // Clear background
            gg.setBackground(new Color(64, 64, 64));
            gg.clearRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Launch method
     */
    public static void start(String[] args, PreClassLoader mainClassLoader) {
        // Get game-directory.
        for (String arg : args) {
            if (arg.startsWith("gameDir=")) {
                QBubbles.gameDir = new File(arg.substring(8));
            }
            if (arg.equals("--debug")) {
                QBubbles.debug = true;
            }
            if (arg.equals("--dev")) {
                QBubbles.devMode = true;
            }
        }

        // Check if game-dir is assigned, if not the game-dir is not specified in the arguments.
        if (getGameDir() == null) {
            System.err.println("Game Directory is not specified!");
            System.exit(1);
        }

        QBubbles.mainClassLoader = mainClassLoader;

        // Boot the game.
        try {
            new QBubbles();
        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(1);
        }
    }

    public void loadSave(String saveName) {

    }

    public void createAndLoadSave(String saveName) {

    }

    @Nullable
    public SavedGame getCurrentSave() {
        Scene scene = sceneManager.getCurrentScene();
        if (scene instanceof GameScene) {
            return ((GameScene) scene).getSavedGame();
        }
        return null;
    }

    public void displayScene(Scene scene) {
        this.sceneManager.displayScene(scene);
    }

    public void displayScene(Scene scene, boolean force) {
        this.sceneManager.displayScene(scene, force);
    }

    public Scene getCurrentScene() {
        return this.sceneManager.getCurrentScene();
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
}
