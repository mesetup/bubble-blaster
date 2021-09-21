package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.command.*;
import com.ultreon.bubbles.data.GlobalSaveData;
import com.ultreon.bubbles.entity.bubble.BubbleSystem;
import com.ultreon.bubbles.event.load.LoadCompleteEvent;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.loader.ModLoader;
import com.ultreon.bubbles.mod.loader.Scanner;
import com.ultreon.bubbles.registry.ModManager;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.bubbles.util.Util;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.commons.lang.LoggableProgress;
import com.ultreon.commons.lang.Pair;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.event.CollectTexturesEvent;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.event.bus.GameEvents;
import com.ultreon.hydro.event.input.XInputEventThread;
import com.ultreon.hydro.event.registry.RegistryEvent;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.hydro.registry.object.ObjectHolder;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.render.TextureCollection;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;
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
    private static final Logger LOGGER = LogManager.getLogger("Game-Loader");
    private static LoadScreen instance = null;
    private final List<Pair<String, Long>> messages = new CopyOnWriteArrayList<>();
    private Thread loadThread;
    private final String title = "";
    private final String description = "";
    private ModLoader modLoader = null;
    private AbstractEvents.AbstractSubscription binding;
    private static boolean done;
    private LoggableProgress mainProgress = null;
    private LoggableProgress subProgress = null;
    private final InfoTransporter mainLogger = new InfoTransporter(this::logMain);
    private final InfoTransporter subLogger = new InfoTransporter(this::logSub1);
    private String mainMessage = "";
    private String subMessage1 = "";

    public LoadScreen() {
        instance = this;
    }

    private static ModLoader getModLoader() {
        if (instance == null) {
            return null;
        }

        return instance.modLoader;
    }

    public static LoadScreen get() {
        return done ? null : instance;
    }

    @Override
    public Cursor getDefaultCursor() {
        return BubbleBlaster.instance().getBlankCursor();
    }

    @Override
    public void init() {
        LOGGER.info("Showing LoadScene");

        BubbleBlaster.getEventBus().subscribe(this);
        BubbleBlaster.instance().startLoading();

        BubbleBlaster.instance().getGameWindow().setCursor(BubbleBlaster.instance().getDefaultCursor());

        run();
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public void tick() {
        messages.removeIf((pair) -> pair.getSecond() + 2000 < System.currentTimeMillis());
    }

    @Override
    public boolean onClose(Screen to) {
        if (!isDone()) {
            return false;
        }
        BubbleBlaster.getEventBus().unsubscribe(this);
        return true;
    }

    @Override
    public void render(Game game, Renderer gg) {
        BubbleBlaster game1 = (BubbleBlaster) game;

        gg.color(new Color(72, 72, 72));
        gg.rect(0, 0, BubbleBlaster.instance().getWidth(), BubbleBlaster.instance().getHeight());

        int i = 0;

        if (mainProgress != null) {
            {
                int progress = mainProgress.getProgress();
                int max = mainProgress.getMax();

                if (mainMessage != null) {
                    gg.color(new Color(128, 128, 128));
                    GraphicsUtils.drawCenteredString(gg, mainMessage, new Rectangle2D.Double(0, (double) BubbleBlaster.instance().getHeight() / 2 - 15, BubbleBlaster.instance().getWidth(), 30), new Font(game1.getSansFontName(), Font.PLAIN, 20));
                }

                gg.color(new Color(128, 128, 128));
                gg.rect(BubbleBlaster.instance().getWidth() / 2 - 150, BubbleBlaster.instance().getHeight() / 2 + 15, 300, 3);

                func_i1(gg);
                gg.rect(BubbleBlaster.instance().getWidth() / 2 - 150, BubbleBlaster.instance().getHeight() / 2 + 15, (int) (300d * (double) progress / (double) max), 3);
            }

            if (subProgress != null) {
                int progress = subProgress.getProgress();
                int max = subProgress.getMax();

                if (subMessage1 != null) {
                    gg.color(new Color(128, 128, 128));
                    GraphicsUtils.drawCenteredString(gg, subMessage1, new Rectangle2D.Double(0, (double) BubbleBlaster.instance().getHeight() / 2 + 60, BubbleBlaster.instance().getWidth(), 30), new Font(game1.getSansFontName(), Font.PLAIN, 20));
                }

                gg.color(new Color(128, 128, 128));
                gg.rect(BubbleBlaster.instance().getWidth() / 2 - 150, BubbleBlaster.instance().getHeight() / 2 + 90, 300, 3);

                func_i1(gg);
                gg.rect(BubbleBlaster.instance().getWidth() / 2 - 150, BubbleBlaster.instance().getHeight() / 2 + 90, (int) (300d * (double) progress / (double) max), 3);
            }
        }

        gg.color(new Color(127, 127, 127));
    }

    private void func_i1(Renderer gg) {
        gg.color(new Color(0, 192, 255));
        GradientPaint p = new GradientPaint(0, (float) BubbleBlaster.instance().getWidth() / 2 - 150, new Color(0, 192, 255), (float) BubbleBlaster.instance().getWidth() / 2 + 150, 0f, new Color(0, 255, 192));
        gg.paint(p);
    }

    @Override
    public void renderGUI(Game game, Renderer gg) {

    }

    @Override
    public void run() {
        this.mainProgress = new LoggableProgress(mainLogger, 10);

        // Get game directory in Java's File format.
        File gameDir = BubbleBlaster.getGameDir();

        // Check game directory exists, if not, create it!
        if (!gameDir.exists()) {
            if (!gameDir.mkdirs()) {
                throw new IllegalStateException("Game Directory isn't created!");
            }
        }

        // Initialize registries.
        new ModManager();

        LOGGER.info("Loading mods...");
        this.mainProgress.log("Loading mods...");
        this.mainProgress.increment();
        this.modLoader = new ModLoader(this);
        this.subProgress = null;

        LOGGER.info("Constructing mods...");
        this.mainProgress.log("Constructing mods...");
        this.mainProgress.increment();
        this.modLoader.constructMods();
        this.subProgress = null;

        // Loading object holders
        this.mainProgress.log("Loading object-holders...");
        this.mainProgress.increment();
        loadObjectHolders();
        this.subProgress = null;

        this.mainProgress.log("Initializing Bubble Blaster");
        this.mainProgress.increment();
        initialize();
        this.subProgress = null;

        LOGGER.info("Setup mods...");
        this.mainProgress.log("Setup Mods");
        this.mainProgress.increment();
        this.modLoader.modSetup();
        this.subProgress = null;

        // GameScene and ClassicType initialization.
        this.mainProgress.log("Registering...");
        this.mainProgress.increment();

        this.subProgress = new LoggableProgress(subLogger, 9);
        this.subProgress.log("Effects");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.EFFECTS));
        this.subProgress.log("Abilities");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.ABILITIES));
        this.subProgress.log("Ammo Types");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.AMMO_TYPES));
        this.subProgress.log("Entities");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.ENTITIES));
        this.subProgress.log("Bubbles");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.BUBBLES));
        this.subProgress.log("Game States");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.GAME_EVENTS));
        this.subProgress.log("Game Types");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.GAME_TYPES));
        this.subProgress.log("Cursors");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.CURSORS));
        this.subProgress.log("Texture Collections");
        this.subProgress.increment();
        GameEvents.get().publish(new RegistryEvent.Register<>(Registers.TEXTURE_COLLECTIONS));
        this.subProgress = null;

        this.mainProgress.log("");
        this.mainProgress.increment();
        Collection<TextureCollection> values = Registers.TEXTURE_COLLECTIONS.values();
        this.subProgress = new LoggableProgress(this.subLogger, values.size());
        for (TextureCollection collection : values) {
            GameEvents.get().publish(new CollectTexturesEvent(collection));
            this.subProgress.increment();
        }

        // BubbleSystem
        this.mainProgress.log("Initialize bubble system...");
        this.mainProgress.increment();
        BubbleSystem.init();

        // Load complete.
        this.mainProgress.log("Load Complete!");
        this.mainProgress.increment();
        BubbleBlaster.getEventBus().publish(new LoadCompleteEvent(this.modLoader));

        // Registry dump.
        this.mainProgress.log("Registry Dump.");
        this.mainProgress.increment();
        Registry.dump();
        BubbleBlaster.getEventBus().publish(new RegistryEvent.Dump());

        LoadScreen.done = true;

        Util.getGame().getScreenManager().displayScreen(new TitleScreen());
    }

    public static boolean isDone() {
        return LoadScreen.done;
    }

    @SuppressWarnings({"unchecked", "DuplicatedCode"})
    private void loadObjectHolders() {
        List<ModContainer> containers = com.ultreon.bubbles.mod.loader.ModManager.instance().getContainers();

        // Loop mods.
        for (ModContainer container : containers) {
            // Get scan result.
            Scanner.Result result = modLoader.getScanResult(container.getSource());
            List<Class<?>> classes = result.getClasses(ObjectHolder.class);

            // Loop classes to register constants in object-holder annotated classes.
            for (Class<?> clazz : classes) {

                // Check if class have object-holder annotation.
                if (!clazz.isAnnotationPresent(ObjectHolder.class)) continue;

                // Get annotation, to get the mod id from it.
                ObjectHolder holder = clazz.getDeclaredAnnotation(ObjectHolder.class);
                String modId;
                Class<?> type;

                // Check if modId is present, if true, the mod id variable will be assigned.
                if (holder != null) {
                    modId = holder.modId();
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
                                object.setRegistryName(new ResourceEntry(modId, field.getName().toLowerCase()));

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
                            object.setRegistryName(new ResourceEntry(modId, field.getName().toLowerCase()));

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

    public void initialize() {
        BubbleBlaster main = Util.getGame();

        // Log
        this.logInfo("Initializing Bubble Blaster...");

        // Add key listener.
        this.logInfo("Loading input event listeners / threads...");

        // Request focus.
        game.getGameWindow().requestFocus();

        // XInput event listener thread.
        XInputEventThread.instance().start();

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
