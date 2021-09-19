package com.ultreon.bubbles;

import com.google.common.annotations.Beta;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.mod.ModList;
import com.ultreon.bubbles.mod.loader.ModManager;
import com.ultreon.bubbles.common.References;
import com.ultreon.bubbles.entity.player.PlayerEntity;
import com.ultreon.bubbles.screen.LoadScreen;
import com.ultreon.commons.crash.CrashLog;
import com.ultreon.bubbles.common.streams.CustomOutputStream;
import com.ultreon.bubbles.core.InstrumentHook;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.commons.util.FileUtils;
import com.ultreon.commons.lang.LoggableProgress;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.GameWindow;
import com.ultreon.hydro.player.IPlayer;
import com.ultreon.hydro.player.PlayerController;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.environment.EnvironmentRenderer;
import com.ultreon.hydro.event.ExitEvent;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.screen.gui.Window;
import com.ultreon.bubbles.init.GameTypes;
import com.ultreon.bubbles.media.AudioPlayer;
import com.ultreon.hydro.render.RenderSettings;
import com.ultreon.hydro.render.TextureManager;
import com.ultreon.hydro.resources.ResourceManager;
import com.ultreon.bubbles.screen.MessageScreen;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.screen.ScreenManager;
import com.ultreon.preloader.PreClassLoader;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import org.jdesktop.swingx.JXFrame;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import com.ultreon.hydro.render.Renderer;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.util.Objects;
import java.util.Random;

/**
 * The QBubbles game main class.
 *
 * @since 0.0.1-indev1
 */
@ParametersAreNonnullByDefault
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "RedundantSuppression"})
public final class BubbleBlaster extends Game {
    public static final int TPS = 20;

    // Initial game information / types.
    private static ClassLoader mainClassLoader;
    private static AudioPlayer audioPlayer;
    private static File gameDir = null;
    private static boolean debugMode;
    private static boolean devMode;

    // Number values.
    private static long ticks = 0L;

    // Environment
    @Nullable public Environment environment;

    // Player
    private final PlayerController playerController = new PlayerController(playerInterface);

    // Fonts.
    private Font sansFont;
    private Font monospaceFont;
    private Font pixelFont;
    private Font gameFont;

    // Cursors
    private final Cursor blankCursor;
    private final Cursor defaultCursor;
    private final Cursor pointerCursor;
    private final Cursor textCursor;

    // Font names.
    private final String fontName;

    // GraphicsProcessor / GUI.
    private final RenderSettings renderSettings;

    // Managers.
    private final ModManager modManager = ModManager.getInstance();
    private final TextureManager textureManager = TextureManager.getInstance();
    private final ResourceManager resourceManager = new ResourceManager();

    // Logger.
    private static final Logger logger = LogManager.getLogger("Generic");

    // Running value.
    private volatile boolean running = false;

    // Randomizers.
    private final Random random = new Random();

    // Rendering
    private final EnvironmentRenderer environmentRenderer = new EnvironmentRenderer();

    // Loaded game.
    private LoadedGame loadedGame;

    // Misc
    private final Instrumentation instrumentation;
    private final BufferedImage background = null;

    public TextureManager getTextureManager() {
        return textureManager;
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

    @Deprecated
    public JXFrame getFrame() {
        return null;
    }
    // Threads

    private Thread renderThread;
    private Thread gcThread;

    // Player entity
    public PlayerEntity player;

    // Instance
    @Getter
    private static BubbleBlaster instance;
    public static Instrumentation getInstrumentation() {
        return instance.instrumentation;
    }

    /**
     * Get event bus.
     *
     * @return The bubble blaster event bus.
     */
    public static GameEventBus getEventBus() {
        return GameEventBus.get();
    }

    // Constructor.

    public BubbleBlaster() throws IOException {
        super(new GameWindow.Properties("Bubble Blaster", 1280, 720), new BootOptions().tps(20));

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

        // Create instance field in class.

        this.renderSettings = new RenderSettings();

//        // Internal canvas.
//        this.window = new Window(this, !isDevMode());
//        this.window.setCursor(defaultCursor);

        // Request focus
        getGameWindow().requestFocus();

        // Register event bus.
        getEventBus().register(this);

        // Start scene-manager.
        try {
            Objects.requireNonNull(this.getScreenManager()).start();
        } catch (Throwable t) {
            CrashLog crashLog = new CrashLog("Oops, game crashed!", t);
            throw crashLog.createCrash();
        }
    }

    public void gcThread() {
        System.gc();
    }

    /**
     * Starts game-thread.
     */
    @Deprecated
    public void start(Window window) {

    }

    /**
     * Stops game-thread.
     */
    public synchronized void stop() {
        try {
            renderThread.join();
            mainThread.join();
            gcThread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void onClose() {
        // Shut-down game.
        getLogger().warn("QBubbles is now shutting down...");

        // Check for exit events.
        checkForExitEvents();

        // Stop thread, when fails: interrupt thread, when that also fails show error.
        try {
            // Interrupt thread.
            if (mainThread.isAlive() && Thread.currentThread() != mainThread) {
                mainThread.interrupt();
                getLogger().info("Interrupted main thread.");
            }
        } catch (SecurityException ignore) {
            getLogger().error("Cannot stop and interrupt game-thread because of security exceptions.");
        }
    }

    public int getScaledWidth() {
        return (int) (getWidth() * renderSettings.getScale());
    }

    public int getScaledHeight() {
        return (int) (getHeight() * renderSettings.getScale());
    }

    private void checkForExitEvents() {
        getEventBus().post(new ExitEvent());
    }

    private void renderScreenEnv(Renderer renderer) {
        // Get field and set local variable. For multithreaded null-safety.
        @Nullable Screen screen = screenManager.getCurrentScreen();
        @Nullable Environment environment = this.environment;
        @Nullable EnvironmentRenderer environmentRenderer = this.environmentRenderer;

        // Clear background.
        renderer.clearColor(Color.BLACK);
        renderer.clearRect(0, 0, getWidth(), getHeight());

        // Render environment.
        if (environment != null && environmentRenderer != null) {
            environmentRenderer.render(renderer);
        }

        // Render screen.
        if (screen != null) {
            screen.render(this, renderer);
            screen.renderGUI(this, renderer);
        }
    }

    /**
     * Launch method.
     * Contains argument parsing.
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

    /**
     * Todo: implement save loading.
     *
     * @param saveName ...
     */
    @SuppressWarnings("EmptyMethod")
    public void loadSave(String saveName) {
        loadGame();
    }


    /**
     * Todo: implement save loading.
     *
     * @param save save to load
     */
    public void loadSave(SavedGame save) {
        loadGame();
    }

    /**
     * Todo: implement save creation and loading..
     *
     * @param saveName ...
     */
    @Beta
    @SuppressWarnings("EmptyMethod")
    public void createAndLoadSave(String saveName) {

    }

    /**
     * @param screen the screen to switch to.
     */
    public void showScreen(@Nullable Screen screen) {
        this.screenManager.displayScreen(screen);
    }

    /**
     * @param screen the screen to switch to.
     * @param force whether to force switching or not.
     */
    public void showScreen(Screen screen, boolean force) {
        this.screenManager.displayScreen(screen, force);
    }

    public @Nullable Screen getCurrentScreen() {
        return this.screenManager.getCurrentScreen();
    }

    public void runLater(Runnable runnable) {
        this.scheduleTask(runnable);
    }

    @SuppressWarnings({"ConstantConditions", "PointlessBooleanExpression"})
    public void loadGame() {
        final boolean create = true;
        AbstractGameType gameType = GameTypes.CLASSIC_TYPE.get();

        // Start loading.
        SavedGame save = SavedGame.fromFile(new File(References.SAVES_DIR, "save"));
        MessageScreen envLoader = new MessageScreen();

        // Show environment loader screen.
        showScreen(envLoader);
        try {
            File directory = save.getDirectory();
            if (create && directory.exists()) {
                org.apache.commons.io.FileUtils.deleteDirectory(directory);
            }

            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Creating save folder failed.");
                }
            }

            if (create) {
                envLoader.setDescription("Creating save data...");
                gameType.dumpDefaultState(save, envLoader.getInfoTransporter());
                gameType.createSaveData(save, envLoader.getInfoTransporter());

                envLoader.setDescription("Loading data...");
            } else {
                envLoader.setDescription("Loading data...");
                gameType = AbstractGameType.loadState(save, envLoader.getInfoTransporter());
            }

            Environment environment = this.environment = new Environment(gameType);

            if (create) {
                gameType.init(environment, envLoader.getInfoTransporter());
                gameType.dumpSaveData(save);
            } else {
                gameType.load(environment, envLoader.getInfoTransporter());
            }

            LoadedGame loadedGame = new LoadedGame(save, this.environment);
            loadedGame.run();

            loadPlayEnvironment();

            this.loadedGame = loadedGame;
        } catch (Throwable t) {
            t.printStackTrace();
            CrashLog crashLog = new CrashLog("Save being loaded", t);
            crashLog.add("Save Directory", save.getDirectory());
            throw crashLog.createCrash();
        }

        BubbleBlaster.getInstance().showScreen(null);
    }

    public void quitLoadedGame() {
        LoadedGame loadedGame = getLoadedGame();
        if (loadedGame != null) {
            loadedGame.quit();
        }
    }

    ////////////////////////
    //     Game flags     //
    ////////////////////////
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isOnMainThread() {
        return Thread.currentThread() == mainThread;
    }
    public boolean isOnRenderThread() {
        return Thread.currentThread() == renderThread;
    }
    public boolean isGameLoaded() {
        return getLoadedGame() != null;
    }

    //////////////////////
    //     Managers     //
    //////////////////////
    public ScreenManager getScreenManager() {
        return screenManager;
    }
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /////////////////////////
    //     Loaded Game     //
    /////////////////////////
    public @Nullable LoadedGame getLoadedGame() {
        return loadedGame;
    }
    public @Nullable Environment getEnvironment() {
        return environment;
    }
    public @Nullable SavedGame getCurrentSave() {
        LoadedGame loadedGame = getLoadedGame();
        if (loadedGame != null) {
            return loadedGame.getSavedGame();
        }
        return null;
    }

    ///////////////////
    //     Fonts     //
    ///////////////////
    public Font getSansFont() {
        return sansFont;
    }
    public Font getMonospaceFont() {
        return monospaceFont;
    }
    public Font getPixelFont() {
        return pixelFont;
    }
    public Font getGameFont() {
        return gameFont;
    }

    /////////////////
    //     GUI     //
    /////////////////

    /**
     * @deprecated Use {@link #getGameWindow()} instead.
     */
    @Deprecated
    public GameWindow getWindow() {
        return getGameWindow();
    }

    /////////////////////
    //     Cursors     //
    /////////////////////
    public Cursor getBlankCursor() {
        return blankCursor;
    }
    public Cursor getTextCursor() {
        return textCursor;
    }
    public Cursor getPointerCursor() {
        return pointerCursor;
    }
    public Cursor getDefaultCursor() {
        return defaultCursor;
    }
    public String getFontName() {
        return fontName;
    }

    ///////////////////////
    //     Rendering     //
    ///////////////////////
    public RenderSettings getRenderSettings() {
        return renderSettings;
    }
    public EnvironmentRenderer getEnvironmentRenderer() {
        return environmentRenderer;
    }

    ////////////////////
    //     Player     //
    ////////////////////
    public PlayerController getPlayerController() {
        return playerController;
    }

    /////////////////////////
    //     Game values     //
    /////////////////////////
    public static long getTicks() {
        return ticks;
    }

    ///////////////////////////
    //     Value options     //
    ///////////////////////////
    public static File getGameDir() {
        return gameDir;
    }

    ////////////////////
    //     Bounds     //
    ////////////////////
    public Rectangle getGameBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    ///////////////////////////
    //     Startup Modes     //
    ///////////////////////////
    public static boolean isDebugMode() {
        return debugMode;
    }
    public static boolean isDevMode() {
        return devMode;
    }

    ///////////////////////////
    //     Class Loaders     //
    ///////////////////////////
    public static ClassLoader getMainClassLoader() {
        return mainClassLoader;
    }

    ////////////////////////
    //     Game flags     //
    ////////////////////////
    public boolean isRunning() {
        return running;
    }

    /////////////////////////////
    //     Middle position     //
    /////////////////////////////
    public static double getMiddleX() {
        return (double) getInstance().getWidth() / 2;
    }
    public static double getMiddleY() {
        return (double) getInstance().getHeight() / 2;
    }
    public static Point2D getMiddlePoint() {
        return new Point2D.Double(getMiddleX(), getMiddleY());
    }

    ///////////////////
    //     Media     //
    ///////////////////
    public static AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    /////////////////////
    //     Loggers     //
    /////////////////////
    public static Logger getLogger() {
        return logger;
    }

    /////////////////////////////////////
    //     Reduce ticks to seconds     //
    /////////////////////////////////////
    public static byte reduceTicks2Secs(byte value, byte seconds) {
        return (byte) ((double) value / ((double)TPS * seconds));
    }
    public static short reduceTicks2Secs(short value, short seconds) {
        return (short) ((double) value / ((double)TPS * seconds));
    }
    public static int reduceTicks2Secs(int value, int seconds) {
        return (int) ((double) value / ((double)TPS * seconds));
    }
    public static long reduceTicks2Secs(long value, long seconds) {
        return (long) ((double) value / ((double)TPS * seconds));
    }
    public static float reduceTicks2Secs(float value, float seconds) {
        return (float) ((double) value / ((double)TPS * seconds));
    }
    public static double reduceTicks2Secs(double value, double seconds) {
        return value / ((double) TPS * seconds);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPaused() {
        Screen currentScreen = getInstance().getCurrentScreen();
        return currentScreen != null && currentScreen.doesPauseGame();
    }

    @Override
    protected void load(LoggableProgress mainProgress) {

    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void preparePlayer() {

    }

    @Override
    protected IPlayer createPlayer() {
        if (environment != null) {
            return new PlayerEntity(environment.getGameType());
        } else {
            throw new IllegalStateException("Creating a player while environment is not loaded.");
        }
    }

    @Override
    public ScreenManager createScreenManager() {
        return ScreenManager.create(new LoadScreen());
    }

    @Override
    protected int getMainLoadingSteps() {
        return 0;
    }

    @Override
    protected void render(Renderer renderer) {
//        environmentRenderer.render(renderer);
        renderScreenEnv(renderer);
    }

    @Override
    protected void tick() {
        if (this.environment != null) this.environment.tick();

        BubbleBlaster.ticks++;
    }

    @Override
    protected void loadEnvironment() {
        preparePlayer();
    }

    public void startLoading() {
        getGameWindow().init();
    }

    @Deprecated
    public ModList getModList() {
        return ModList.get();
    }

    public Font getFont() {
        return getSansFont();
    }
}
