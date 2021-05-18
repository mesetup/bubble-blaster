package com.qtech.bubbles.scene;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.Scanner;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.command.*;
import com.qtech.bubbles.common.Pair;
import com.qtech.bubbles.common.RegistryEntry;
import com.qtech.bubbles.common.ResourceLocation;
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
import java.awt.geom.Point2D;
import java.io.File;
import java.lang.reflect.Field;
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
        gg.setColor(new Color(0, 72, 96));
        gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
//        if (GameSettings.instance().isTextAntialiasEnabled())
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int i = 0;
        for (int j = Math.min(messages.size() - 1, 7); j > -1; j--) {
            Pair<String, Long> pair = messages.get(j);
            String message = pair.getFirst();
            long startTime = pair.getSecond();
            long secondsToLive = startTime - System.currentTimeMillis() + 2000;
            float alpha;
            if (secondsToLive <= 0) {
                alpha = 0f;
            } else if (secondsToLive >= 2000) {
                alpha = 1f;
            } else {
                alpha = secondsToLive / 2000f;
            }

            gg.setColor(new Color(1f, 1f, 1f, alpha));
            GraphicsUtils.drawLeftAnchoredString(gg, message, new Point2D.Float(10, game.getHeight() - 35f - (25f * i)), 25, new Font(game.getSansFontName(), Font.PLAIN, 20));
            i++;

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

        logInfo("Loading addons...");
        LOGGER.info("Loading addons...");
        this.addonLoader = new AddonLoader(this);

        logInfo("Constructing addons...");
        LOGGER.info("Constructing addons...");
        this.addonLoader.constructAddons();

        logInfo("Pre-Initialize Addons");
        LOGGER.info("Pre-Initialize addons...");
//        this.addonLoader.preInitialize();

        initialize();
        logInfo("Initialize Addons");
        LOGGER.info("Initialize addons...");
//        this.addonLoader.initialize();

        logInfo("Post-Initialize Addons");
        LOGGER.info("Post-Initialize addons...");
//        this.addonLoader.postInitialize();

        // Loading object holders
        logInfo("Loading object-holders...");
        loadObjectHolders();

        // GameScene and ClassicType initialization.
        logInfo("Registering...");
        logInfo("Registering: Effects");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.EFFECTS));
        logInfo("Registering: Abilities");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.ABILITIES));
        logInfo("Registering: Ammo Types");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.AMMO_TYPES));
        logInfo("Registering: Entities");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.ENTITIES));
        logInfo("Registering: Bubbles");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.BUBBLES));
        logInfo("Registering: Game States");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.GAME_EVENTS));
        logInfo("Registering: Game Types");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.GAME_TYPES));
        logInfo("Registering: Cursors");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.CURSORS));
        logInfo("Registering: Texture Collections");
        Bus.getAddonEventBus().post(new RegistryEvent.Register<>(Registers.TEXTURE_COLLECTIONS));

        for (TextureCollection collection : Registers.TEXTURE_COLLECTIONS.values()) {
            Bus.getQBubblesEventBus().post(new TextureRenderEvent(collection));
        }

        // BubbleSystem
        logInfo("Initialize bubble system...");
        BubbleSystem.init();

        // Load complete.
        logInfo("Load Complete!");
        QBubbles.getEventBus().post(new LoaderLoadCompleteEvent(addonLoader));

        // Registry dump.
        logInfo("Registry Dump.");
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
}
