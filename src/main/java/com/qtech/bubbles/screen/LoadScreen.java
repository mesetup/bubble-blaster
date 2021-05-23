package com.qtech.bubbles.screen;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.Scanner;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.command.*;
import com.qtech.bubbles.common.*;
import com.qtech.bubbles.common.bubble.BubbleSystem;
import com.qtech.bubbles.common.command.CommandConstructor;
import com.qtech.bubbles.common.screen.Screen;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.core.controllers.MouseController;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.data.GlobalSaveData;
import com.qtech.bubbles.event.Bus;
import com.qtech.bubbles.event.LoaderLoadCompleteEvent;
import com.qtech.bubbles.event.TextureRenderEvent;
import com.qtech.bubbles.event.XInputEventThread;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.event.registry.RegistryEvent;
import com.qtech.bubbles.graphics.TextureCollection;
import com.qtech.bubbles.registry.AddonManager;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.registry.object.ObjectHolder;
import com.qtech.bubbles.util.Util;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public final class LoadScreen extends Screen implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("QB:GameLoader");
    private static LoadScreen instance = null;
    private final List<Pair<String, Long>> messages = new CopyOnWriteArrayList<>();
    private Thread loadThread;
    private final String title = "";
    private final String description = "";
    private AddonLoader addonLoader = null;
    private EventBus.Handler binding;
    private static boolean done;
    private LoggableProgress mainProgress = null;
    private LoggableProgress subProgress1 = null;
    private final InfoTransporter mainLogger = new InfoTransporter(this::logMain);
    private final InfoTransporter subLogger1 = new InfoTransporter(this::logSub1);
    private String mainMessage = "";
    private String subMessage1 = "";

    public LoadScreen() {
        instance = this;
    }

    public static AddonLoader getAddonLoader() {
        if (instance == null) {
            return null;
        }

        return instance.addonLoader;
    }

    public static LoadScreen get() {
        return done ? null : instance;
    }

    @Override
    public Cursor getDefaultCursor() {
        return QBubbles.getInstance().getBlankCursor();
    }

    @Override
    public void init() {
        LOGGER.info("Showing LoadScene");

        QBubbles.getEventBus().register(this);
        QBubbles.getInstance().getWindow().setVisible(true);
        run();
    }

    @Override
    public void tick() {
        messages.removeIf((pair) -> pair.getSecond() + 2000 < System.currentTimeMillis());
    }

    @Override
    public boolean onClose(Screen to) {
        if (!isDone()) {
            return false;
        }
        QBubbles.getEventBus().unregister(this);
        return true;
    }

    public void render(QBubbles game, Graphics2D gg) {
        gg.setColor(new Color(72, 72, 72));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
//        if (GameSettings.instance().isTextAntialiasEnabled())
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int i = 0;
//        for (int j = Math.min(messages.size() - 1, 7); j > -1; j--) {
//            Pair<String, Long> pair = messages.get(j);
//            String message = pair.getFirst();
//            long startTime = pair.getSecond();
//            long secondsToLive = startTime - System.currentTimeMillis() + 2000;
//            float alpha;
//            if (secondsToLive <= 0) {
//                alpha = 0f;
//            } else if (secondsToLive >= 2000) {
//                alpha = 1f;
//            } else {
//                alpha = secondsToLive / 2000f;
//            }
//
//            gg.setColor(new Color(1f, 1f, 1f, alpha));
//            GraphicsUtils.drawLeftAnchoredString(gg, message, new Point2D.Float(10, game.getHeight() - 35f - (25f * i)), 25, new Font(game.getSansFontName(), Font.PLAIN, 20));
//            i++;
//
//        }

        if (mainProgress != null) {
            {
                int progress = mainProgress.getProgress();
                int max = mainProgress.getMax();

                if (mainMessage != null) {
                    gg.setColor(new Color(128, 128, 128));
                    GraphicsUtils.drawCenteredString(gg, mainMessage, new Rectangle2D.Double(0, (double)QBubbles.getInstance().getHeight() / 2 - 15, QBubbles.getInstance().getWidth(), 30), new Font(game.getSansFontName(), Font.PLAIN, 20));
                }

                gg.setColor(new Color(128, 128, 128));
                gg.fillRect(QBubbles.getInstance().getWidth() / 2 - 150, QBubbles.getInstance().getHeight() / 2 + 15, 300, 3);

                gg.setColor(new Color(0, 192, 255));
                GradientPaint p = new GradientPaint(0, (float)QBubbles.getInstance().getWidth() / 2 - 150, new Color(0, 192, 255), (float)QBubbles.getInstance().getWidth() / 2 + 150, 0f, new Color(0, 255, 192));
                gg.setPaint(p);
                gg.fillRect(QBubbles.getInstance().getWidth() / 2 - 150, QBubbles.getInstance().getHeight() / 2 + 15, (int)(300d * (double)progress / (double) max), 3);
            }

            if (subProgress1 != null) {
                int progress = subProgress1.getProgress();
                int max = subProgress1.getMax();

                if (subMessage1 != null) {
                    gg.setColor(new Color(128, 128, 128));
                    GraphicsUtils.drawCenteredString(gg, subMessage1, new Rectangle2D.Double(0, (double)QBubbles.getInstance().getHeight() / 2 + 60, QBubbles.getInstance().getWidth(), 30), new Font(game.getSansFontName(), Font.PLAIN, 20));
                }

                gg.setColor(new Color(128, 128, 128));
                gg.fillRect(QBubbles.getInstance().getWidth() / 2 - 150, QBubbles.getInstance().getHeight() / 2 + 90, 300, 3);

                gg.setColor(new Color(0, 192, 255));
                GradientPaint p = new GradientPaint(0, (float)QBubbles.getInstance().getWidth() / 2 - 150, new Color(0, 192, 255), (float)QBubbles.getInstance().getWidth() / 2 + 150, 0f, new Color(0, 255, 192));
                gg.setPaint(p);
                gg.fillRect(QBubbles.getInstance().getWidth() / 2 - 150, QBubbles.getInstance().getHeight() / 2 + 90, (int)(300d * (double)progress / (double) max), 3);
            }
        }

        gg.setColor(new Color(127, 127, 127));
//        GraphicsUtils.drawCenteredString(gg, this.title, new Rectangle2D.Double(0, (double) QBubbles.getInstance().getHeight() / 2, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.BOLD, 50));
//        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) QBubbles.getInstance().getHeight() / 2) + 40, QBubbles.getInstance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
//        if (GameSettings.instance().isTextAntialiasEnabled())
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {

    }

    @Override
    public void run() {
        this.mainProgress = new LoggableProgress(mainLogger, 10);

        // Get game directory in Java's File format.
        File gameDir = QBubbles.getGameDir();

        // Check game directory exists, if not, create it!
        if (!gameDir.exists()) {
            if (!gameDir.mkdirs()) {
                throw new IllegalStateException("Game Directory isn't created!");
            }
        }

        // Initialize registries.
        new AddonManager();

        LOGGER.info("Loading addons...");
        mainProgress.log("Loading addons...");
        mainProgress.increment();
        this.addonLoader = new AddonLoader(this);
        this.subProgress1 = null;

        LOGGER.info("Constructing addons...");
        mainProgress.log("Constructing addons...");
        mainProgress.increment();
        this.addonLoader.constructAddons();
        this.subProgress1 = null;

        // Loading object holders
        mainProgress.log("Loading object-holders...");
        mainProgress.increment();
        loadObjectHolders();
        this.subProgress1 = null;

        mainProgress.log("Initializing Bubble Blaster");
        mainProgress.increment();
        initialize();
        this.subProgress1 = null;

        LOGGER.info("Setup addons...");
        mainProgress.log("Setup Addons");
        mainProgress.increment();
        this.addonLoader.addonSetup();
        this.subProgress1 = null;

        // GameScene and ClassicType initialization.
        mainProgress.log("Registering...");
        mainProgress.increment();

        subProgress1 = new LoggableProgress(subLogger1, 9);
        subProgress1.log("Effects");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.EFFECTS));
        subProgress1.log("Abilities");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.ABILITIES));
        subProgress1.log("Ammo Types");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.AMMO_TYPES));
        subProgress1.log("Entities");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.ENTITIES));
        subProgress1.log("Bubbles");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.BUBBLES));
        subProgress1.log("Game States");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.GAME_EVENTS));
        subProgress1.log("Game Types");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.GAME_TYPES));
        subProgress1.log("Cursors");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.CURSORS));
        subProgress1.log("Texture Collections");
        subProgress1.increment();
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.TEXTURE_COLLECTIONS));
        this.subProgress1 = null;

        mainProgress.log("");
        mainProgress.increment();
        Collection<TextureCollection> values = Registers.TEXTURE_COLLECTIONS.values();
        subProgress1 = new LoggableProgress(subLogger1, values.size());
        for (TextureCollection collection : values) {
            Bus.getQBubblesEventBus().post(new TextureRenderEvent(collection));
            subProgress1.increment();
        }

        // BubbleSystem
        mainProgress.log("Initialize bubble system...");
        mainProgress.increment();
        BubbleSystem.init();

        // Load complete.
        mainProgress.log("Load Complete!");
        mainProgress.increment();
        QBubbles.getEventBus().post(new LoaderLoadCompleteEvent(addonLoader));

        // Registry dump.
        mainProgress.log("Registry Dump.");
        mainProgress.increment();
        QBubbles.getEventBus().post(new RegistryEvent.Dump());

        done = true;

        Util.getGame().getScreenManager().displayScreen(new TitleScreen());
    }

    public static boolean isDone() {
        return done;
    }

    @SuppressWarnings("unchecked")
    protected final void loadObjectHolders() {
        List<AddonContainer> containers = com.qtech.bubbles.addon.loader.AddonManager.getInstance().getContainers();

        // Loop addons.
        for (AddonContainer container : containers) {
            // Get scan result.
            Scanner.ScanResult scanResult = addonLoader.getScanResult(container.getSource());
            List<Class<?>> classes = scanResult.getClasses(ObjectHolder.class);

            // Loop classes to register constants in object-holder annotated classes.
            for (Class<?> clazz : classes) {

                // Check if class have object-holder annotation.
                if (!clazz.isAnnotationPresent(ObjectHolder.class)) continue;

                // Get annotation, to get the addonId from it.
                ObjectHolder holder = clazz.getDeclaredAnnotation(ObjectHolder.class);
                String addonId;
                Class<?> type;

                // Check if addonId is present, if true, the addonId variable will be assigned.
                if (holder != null) {
                    addonId = holder.addonId();
                    type = holder.type();

                    if (type == ObjectUtils.Null.class) type = null;
                } else {
                    throw new NullPointerException();
                }

                // Get fields.
                Field[] fields = clazz.getDeclaredFields();

                if (type != null) {
                    // Loop fields.
                    for (Field field : fields) {
                        // Try.
                        try {
                            if (type.isAssignableFrom(field.getType())) {
                                // Get type of the register-object.
                                Class<? extends RegistryEntry> objType = (Class<? extends RegistryEntry>) field.getType();

                                // Get register-object.
                                RegistryEntry object = (RegistryEntry) field.get(null);

                                // Set key.
                                object.setRegistryName(new ResourceLocation(addonId, field.getName().toLowerCase()));

                                // Register.
                                Registry.getRegistry(objType).registrable(object.getRegistryName(), object);
                            }
                        } catch (IllegalAccessException e) {
                            // Oops, some problem occurred.
                            e.printStackTrace();
                        }
                    }
                    return;
                }
                // Loop fields.
                for (Field field : fields) {
                    // Try.
                    try {
                        // Is the field a RegistryObject?
                        if (RegistryEntry.class.isAssignableFrom(field.getType())) {
                            // Get type of the register-object.
                            Class<? extends RegistryEntry> objType = (Class<? extends RegistryEntry>) field.getType();

                            // Get register-object.
                            RegistryEntry object = (RegistryEntry) field.get(null);

                            // Set key.
                            object.setRegistryName(new ResourceLocation(addonId, field.getName().toLowerCase()));

                            // Register.
                            Registry.getRegistry(objType).registrable(object.getRegistryName(), object);
                        }
                    } catch (IllegalAccessException e) {
                        // Oops, some problem occurred.
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void initialize() {
        QBubbles main = Util.getGame();

        // Log
        this.logInfo("Initializing QBubbles...");

        // Add key listener.
        this.logInfo("Loading input event listeners / threads...");
        main.addKeyListener(KeyboardController.instance());
        main.addMouseListener(MouseController.instance());
        main.addMouseMotionListener(MouseController.instance());
        main.addMouseWheelListener(MouseController.instance());

        // Request focus.
        main.requestFocus();

        // XInput event listener thread.
        XInputEventThread.start();

        // Commands
        this.logInfo("Initializing Commands...");
        CommandConstructor.add("tp", new TeleportCommand());
        CommandConstructor.add("heal", new HealCommand());
        CommandConstructor.add("level", new LevelCommand());
        CommandConstructor.add("score", new ScoreCommand());
        CommandConstructor.add("effect", new EffectCommand());
        CommandConstructor.add("spawn", new SpawnCommand());
        CommandConstructor.add("shutdown", new ShutdownCommand());
        CommandConstructor.add("teleport", new TeleportCommand());
        CommandConstructor.add("game-over", new GameOverCommand());
        CommandConstructor.add("blood-moon", new BloodMoonCommand());

        // Game data.
        new GlobalSaveData();
    }

    public void logInfo(String description) {
        this.messages.add(new Pair<>(description, System.currentTimeMillis()));
    }

    private void logSub1(String s) {
        this.subMessage1 = s;
    }

    private void logMain(String s) {
        this.mainMessage = s;
    }
}
