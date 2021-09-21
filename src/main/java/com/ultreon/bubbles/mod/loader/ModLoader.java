package com.ultreon.bubbles.mod.loader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.References;
import com.ultreon.bubbles.common.mod.Mod;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.common.text.translation.LanguageMap;
import com.ultreon.bubbles.core.GameClassLoader;
import com.ultreon.bubbles.core.ModClassLoader;
import com.ultreon.bubbles.event.load.ModSetupEvent;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModInformation;
import com.ultreon.bubbles.registry.LocaleManager;
import com.ultreon.bubbles.screen.LoadScreen;
import com.ultreon.bubbles.settings.GameSettings;
import com.ultreon.commons.crash.CrashCategory;
import com.ultreon.commons.crash.CrashLog;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.core.AntiMod;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@AntiMod
@SuppressWarnings("unused")
public class ModLoader {
    public static final File MODS_DIR = References.MODS_DIR;
    private final ArrayList<Object> mods = new ArrayList<>();
    private final LoadScreen loadScreen;
    private static final Logger LOGGER = LogManager.getLogger("Mod-Loader");
    private final HashMap<File, Scanner.Result> scanResults = new HashMap<>();
//    private final List<ModContainer> containers = new ArrayList<>();

    public ModLoader(LoadScreen loadScreen) {
        this.loadScreen = loadScreen;
        boolean existsBefore = true;

//        modClassLoader = new ModClassLoader(BubbleBlaster.getMainClassLoader(), );

        GameClassLoader.get().scan();

        if (!MODS_DIR.exists()) {
            existsBefore = false;
            boolean flag = MODS_DIR.mkdirs();
            if (!flag) {
                throw new IllegalStateException("Mods directory wasn't created. (" + MODS_DIR.getPath() + ")");
            }
        } else if (MODS_DIR.isFile()) {
            existsBefore = false;
            boolean flag1 = MODS_DIR.delete();
            boolean flag2 = MODS_DIR.mkdir();

            if (!flag1) {
                throw new IllegalStateException("Mods directory wasn't deleted. (" + MODS_DIR.getPath() + ")");
            }
            if (!flag2) {
                throw new IllegalStateException("Mods directory wasn't created. (" + MODS_DIR.getPath() + ")");
            }
        }

        URL location = BubbleBlaster.class.getProtectionDomain().getCodeSource().getLocation();
//        try {
//            File file = new File(location.toURI());
//
//            boolean flag = loadJar(file.getPath(), loadScreen);
//            if (!flag) {
//                LOGGER.info("No flag.");
//                loadScreen.logInfo(String.format("Found non-mod file: %s", file.getName()));
//                LOGGER.info(String.format("Found non-mod file: %s", file.getName()));
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        if (existsBefore) {
            for (File file : Objects.requireNonNull(MODS_DIR.listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    ModClassLoader modClassLoader = GameClassLoader.get().addMod(file);
                    LOGGER.debug("Loading jar file: " + file.getName());
                    boolean flag = loadJar(file.getPath(), loadScreen);
                    if (!flag) {
                        LOGGER.info("No flag.");
                        loadScreen.logInfo(String.format("Found non-mod file: %s", file.getName()));
                        LOGGER.info(String.format("Found non-mod file: %s", file.getName()));
                    }
                } else {
                    loadScreen.logInfo(String.format("Found non-mod file: %s", file.getName()));
                    LOGGER.info(String.format("Found non-mod file: %s", file.getName()));
                }
            }
        }
    }

    public void constructMods() {
        if (ModManager.instance().getModIds().isEmpty()) {
            return;
        }

        com.ultreon.bubbles.mod.loader.ModManager modManager = com.ultreon.bubbles.mod.loader.ModManager.instance();

        LOGGER.info(String.format("Constructing Mods: %s", StringUtils.join(ModManager.instance().getModIds(), ", ")));

        ModObject<?> modObject;
        for (ModObject<?> object : ModManager.instance().getModObjects()) {
            loadScreen.logInfo(object.getNamespace());
            try {
                // Create eventbus and logger for mod.
                Logger logger = LogManager.getLogger(object.getAnnotation().modId());

                // Register mod object.
                modManager.registerModObject(object);

                // Create java mod instance.
                Class<? extends ModInstance> modClass = object.getModClass();
                Constructor<? extends ModInstance> constructor = modClass.getConstructor(Logger.class, String.class, ModObject.class);
                constructor.setAccessible(true);
                ModInstance mod = constructor.newInstance(logger, object.getAnnotation().modId(), object);

                // Register java mod.
                modManager.registerMod(mod);

                mods.add(mod);
            } catch (Throwable t) {
                CrashLog crashLog = new CrashLog("Constructing mod", t);
                CrashCategory modCategory = new CrashCategory("Mod was constructing");
                modCategory.add("Mod ID", object);
                modCategory.add("File", object.getContainer().getSource().getPath());
                throw crashLog.createCrash();
            }
        }
    }

    public void modSetup() {
        BubbleBlaster.getEventBus().publish(new ModSetupEvent(this));

        LocaleManager.getManager().register("af_za");
        LocaleManager.getManager().register("el_gr");
        LocaleManager.getManager().register("it_it");
        LocaleManager.getManager().register("en_us");
        LocaleManager.getManager().register("en_uk");
        LocaleManager.getManager().register("es_es");
        LocaleManager.getManager().register("nl_nl");
        LocaleManager.getManager().register("nl_be");
        LocaleManager.getManager().register("fy_nl");
        LocaleManager.getManager().register("zh_cn");

        Set<Locale> locales = LocaleManager.getManager().keys();
        for (Locale locale : locales) {
            LanguageMap langMap = new LanguageMap(locale);

            String resourcePath = "/assets/bubbleblaster/lang/" + locale.toString().toLowerCase() + ".lang";

            InputStream stream = getClass().getResourceAsStream(resourcePath);
            if (stream == null) {
                continue;
            } else {
                langMap.injectInst(stream);
            }

            for (ModObject<?> object : ModManager.instance().getModObjects()) {
                String modId = object.getAnnotation().modId();

                resourcePath = "/assets/" + modId + "/lang/" + locale.toString().toLowerCase() + ".lang";

                stream = object.getModClass().getResourceAsStream(resourcePath);
                if (stream == null) {
//                    LOGGER.warn(String.format("Cannot find language file: %s", resourcePath));
                    continue;
                }

                langMap.injectInst(stream);
            }
        }

        String resourcePath = "/assets/bubbleblaster/lang/" + GameSettings.instance().getLanguage() + ".lang";

        InputStream stream = getClass().getResourceAsStream(resourcePath);
        //noinspection StatementWithEmptyBody
        if (stream == null) {
//            Game.getLogger().warn(String.format("Cannot find language file: %s", resourcePath));
        } else {
            LanguageMap.inject(stream);
        }
        for (ModObject<?> object : ModManager.instance().getModObjects()) {
            String modId = object.getAnnotation().modId();

            resourcePath = "/assets/" + modId + "/lang/" + GameSettings.instance().getLanguage() + ".lang";

            stream = object.getModClass().getResourceAsStream(resourcePath);
            if (stream == null) {
//                Game.getLogger().warn(String.format("Cannot find language file: %s", resourcePath));
                continue;
            }

            LanguageMap.inject(stream);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadJar(String pathToJar, @Nullable LoadScreen loadScreen) {
        try {
            Enumeration<JarEntry> entryEnumeration;
            int internalMods = 0;

            File modFile = new File(pathToJar);

//            Scanner scanner = new Scanner(modFile, modClassLoader);
//            if (loadScreen != null) {
//                loadScreen.logInfo("Scanning jar file: " + modFile.getPath());
//            }
//            Scanner.Result result = scanner.scanJar(loadScreen);
//            JarFile jarFile = result.getScanner().getJarFile();
//            File dir = result.getScanner().getFile();

            GameClassLoader loader = GameClassLoader.get();
            String modFileId = loader.getModFileId(modFile);
            LOGGER.info("Loading mod file with id: {}", modFileId);
            loader.scan(modFileId);

            JarFile jarFile = null;
            File dir = null;
            if (modFile.isFile()) {
                jarFile = new JarFile(modFile);
            } else {
                dir = modFile;
            }

            InputStream inputStream;
            if (jarFile == null) {
                File file1 = new File(dir, "../../../resources/main/META-INF/mods.json").getCanonicalFile();
                inputStream = new FileInputStream(file1);
            } else {
                JarEntry modMetaEntry = jarFile.getJarEntry("META-INF/mods.json");
                inputStream = jarFile.getInputStream(modMetaEntry);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            JsonObject jsonObject = new Gson().fromJson(jsonReader, JsonObject.class);
            JsonArray mods = jsonObject.getAsJsonArray("mods");
            if (mods == null) {
                throw new IllegalStateException("No mods were not found in mod metadata of " + modFile.getAbsolutePath());
            }

            HashSet<String> fromJson = new HashSet<>();
            HashSet<String> missing = new HashSet<>();
            HashSet<String> fromScan = new HashSet<>();
            HashSet<String> current = new HashSet<>();
            HashMap<String, JsonObject> modJsons = new HashMap<>();

            for (JsonElement element : mods) {
                if (element.isJsonObject()) {
                    JsonObject modJson = element.getAsJsonObject();
                    String modId = modJson.getAsJsonPrimitive("modId").getAsString();
                    ResourceEntry.testNamespace(modId);

                    missing.add(modId);
                    fromJson.add(modId);

                    modJsons.put(modId, modJson);
                }
            }

            Scanner.Result result = loader.getResult(modFileId);
            scanResults.put(modFile, result);

            List<Class<?>> classes = result.getClasses(Mod.class);
            for (Class<?> clazz : classes) {
                Mod mod = clazz.getDeclaredAnnotation(Mod.class);
                if (mod != null) {
                    boolean flag = false;
                    for (Constructor<?> constructor : clazz.getConstructors()) {
                        if (constructor.getParameterCount() == 3) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        throw new IllegalArgumentException("Mod has no constructor with 3 parameters. (" + clazz.getName() + ")");
                    }

                    ResourceEntry.testNamespace(mod.modId());

                    internalMods++;

                    JarFile finalJarFile = jarFile;
                    ModContainer modContainer = new ModContainer() {
                        private final ModInformation modInfo = new ModInformation(this);
                        private final ModObject<ModInstance> modObject;
                        private final JsonObject modJson = modJsons.get(mod.modId());

                        {
                            if (ModInstance.class.isAssignableFrom(clazz)) {
                                modObject = new ModObject<>(mod.modId(), this, mod, (Class<ModInstance>) clazz);
                            } else {
                                throw new ClassCastException("Tried to cast invalid class for mod: " + getModId());
                            }
                        }

                        @Override
                        public String getModId() {
                            return mod.modId();
                        }

                        @Override
                        public String getModFileId() {
                            return modFileId;
                        }

                        @Override
                        public JsonObject getModProperties() {
                            return modJson;
                        }

                        @Override
                        public File getSource() {
                            return modFile;
                        }

                        @Nullable
                        @Override
                        public JarFile getJarFile() {
                            return finalJarFile;
                        }

                        @Override
                        public ModInformation getModInfo() {
                            return modInfo;
                        }

                        @Override
                        public Class<?> getModClass() {
                            return clazz;
                        }

                        @Override
                        public ModObject<? extends ModInstance> getModObject() {
                            return modObject;
                        }

                        @Override
                        public ModInstance getModInstance() {
                            return getModObject().getMod();
                        }
                    };

                    com.ultreon.bubbles.mod.loader.ModManager.registerContainer(modContainer);
                    ModManager.instance().registerModObject(modContainer.getModObject());

                    if (!fromJson.contains(mod.modId())) {
                        throw new IllegalArgumentException("Missing mod with ID " + mod.modId() + " in the mod metadata.");
                    }
                    missing.remove(mod.modId());
                }
            }

            StringBuilder sb = new StringBuilder();
            for (String s : missing) {
                sb.append(s);
                sb.append(", ");
            }

            if (sb.length() > 0) {
                throw new IllegalStateException("Missing mod classes with ids: " + sb.substring(0, sb.length() - 2));
            }

            if (internalMods == 0) {
                LOGGER.warn("Mod has no annotated classes.");
            }

            return internalMods != 0;
        } catch (Throwable t) {
            CrashLog crashLog = new CrashLog("Loading mods", t);
            CrashCategory modCategory = new CrashCategory("Mod Loading");
            modCategory.add("File", pathToJar);
            crashLog.addCategory(modCategory);
            throw crashLog.createCrash();
        }
    }

    public Scanner.Result getScanResult(File file) {
        return scanResults.get(file);
    }

    public static File getModsDir() {
        return MODS_DIR;
    }

    public ArrayList<Object> getMods() {
        return mods;
    }

    @Deprecated
    public ModClassLoader getModClassLoader() {
        return null;
    }
}
