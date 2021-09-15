package com.ultreon.bubbles.mod.loader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModInformation;
import com.ultreon.bubbles.common.References;
import com.ultreon.bubbles.common.mod.Mod;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.common.text.translation.LanguageMap;
import com.ultreon.bubbles.event.load.AddonSetupEvent;
import com.ultreon.bubbles.registry.AddonManager;
import com.ultreon.bubbles.registry.LocaleManager;
import com.ultreon.bubbles.screen.LoadScreen;
import com.ultreon.bubbles.settings.GameSettings;
import com.ultreon.commons.crash.CrashCategory;
import com.ultreon.commons.crash.CrashReport;
import com.ultreon.hydro.common.ResourceEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public class ModLoader {
    public static final File ADDONS_DIR = References.ADDONS_DIR;
    private final ArrayList<Object> addons = new ArrayList<>();
    private final LoadScreen loadScreen;
    private final ModClassLoader modClassLoader;
    private static final Logger LOGGER = LogManager.getLogger("Addon-Loader");
    private final HashMap<File, Scanner.ScanResult> scanResults = new HashMap<>();
//    private final List<AddonContainer> containers = new ArrayList<>();

    public ModLoader(LoadScreen loadScreen) {
        this.loadScreen = loadScreen;
        boolean existsBefore = true;

        modClassLoader = new ModClassLoader(BubbleBlaster.getMainClassLoader());

        if (!ADDONS_DIR.exists()) {
            existsBefore = false;
            boolean flag = ADDONS_DIR.mkdirs();
            if (!flag) {
                throw new IllegalStateException("Addons directory wasn't created. (" + ADDONS_DIR.getPath() + ")");
            }
        } else if (ADDONS_DIR.isFile()) {
            existsBefore = false;
            boolean flag1 = ADDONS_DIR.delete();
            boolean flag2 = ADDONS_DIR.mkdir();

            if (!flag1) {
                throw new IllegalStateException("Addons directory wasn't deleted. (" + ADDONS_DIR.getPath() + ")");
            }
            if (!flag2) {
                throw new IllegalStateException("Addons directory wasn't created. (" + ADDONS_DIR.getPath() + ")");
            }
        }

        URL location = BubbleBlaster.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            File file = new File(location.toURI());

            boolean flag = loadJar(file.getPath(), loadScreen);
            if (!flag) {
                LOGGER.info("No flag.");
                loadScreen.logInfo(String.format("Found non-addon file: %s", file.getName()));
                LOGGER.info(String.format("Found non-addon file: %s", file.getName()));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (existsBefore) {
            for (File file : Objects.requireNonNull(ADDONS_DIR.listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    LOGGER.debug("Loading jar file: " + file.getName());
                    boolean flag = loadJar(file.getPath(), loadScreen);
                    if (!flag) {
                        LOGGER.info("No flag.");
                        loadScreen.logInfo(String.format("Found non-addon file: %s", file.getName()));
                        LOGGER.info(String.format("Found non-addon file: %s", file.getName()));
                    }
                } else {
                    loadScreen.logInfo(String.format("Found non-addon file: %s", file.getName()));
                    LOGGER.info(String.format("Found non-addon file: %s", file.getName()));
                }
            }
        }
    }

    public void constructAddons() {
        if (AddonManager.instance().keys().isEmpty()) {
            return;
        }

        ModManager modManager = ModManager.getInstance();

        LOGGER.info(String.format("Constructing Mods: %s", StringUtils.join(AddonManager.instance().keys(), ", ")));

        ModObject<?> modObject;
        for (ModObject<?> object : AddonManager.instance().values()) {
            loadScreen.logInfo(object.getNamespace());
            try {
                // Create eventbus and logger for addon.
                Logger logger = LogManager.getLogger(object.getAnnotation().addonId());

                // Register addon object.
                modManager.registerAddonObject(object);

                // Create java addon instance.
                Class<? extends ModInstance> addonClass = object.getAddonClass();
                Constructor<? extends ModInstance> constructor = addonClass.getConstructor(Logger.class, String.class, ModObject.class);
                constructor.setAccessible(true);
                ModInstance addon = constructor.newInstance(logger, object.getAnnotation().addonId(), object);

                // Register java addon.
                modManager.registerAddon(addon);

                addons.add(addon);
            } catch (Throwable t) {
                CrashReport crashReport = new CrashReport("Constructing addon", t);
                CrashCategory addonCategory = new CrashCategory("Addon was constructing");
                addonCategory.add("Addon ID", object);
                addonCategory.add("File", object.getContainer().getSource().getPath());
                throw crashReport.getReportedException();
            }
        }
    }

    public void addonSetup() {
        BubbleBlaster.getEventBus().post(new AddonSetupEvent(this));

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

            String resourcePath = "/assets/qbubbles/lang/" + locale.toString().toLowerCase() + ".lang";

            InputStream stream = getClass().getResourceAsStream(resourcePath);
            if (stream == null) {
                continue;
            } else {
                langMap.injectInst(stream);
            }

            for (ModObject<?> object : AddonManager.instance().values()) {
                String addonId = object.getAnnotation().addonId();

                resourcePath = "/assets/" + addonId + "/lang/" + locale.toString().toLowerCase() + ".lang";

                stream = object.getAddonClass().getResourceAsStream(resourcePath);
                if (stream == null) {
//                    LOGGER.warn(String.format("Cannot find language file: %s", resourcePath));
                    continue;
                }

                langMap.injectInst(stream);
            }
        }

        String resourcePath = "/assets/qbubbles/lang/" + GameSettings.instance().getLanguage() + ".lang";

        InputStream stream = getClass().getResourceAsStream(resourcePath);
        //noinspection StatementWithEmptyBody
        if (stream == null) {
//            Game.getLogger().warn(String.format("Cannot find language file: %s", resourcePath));
        } else {
            LanguageMap.inject(stream);
        }
        for (ModObject<?> object : AddonManager.instance().values()) {
            String addonId = object.getAnnotation().addonId();

            resourcePath = "/assets/" + addonId + "/lang/" + GameSettings.instance().getLanguage() + ".lang";

            stream = object.getAddonClass().getResourceAsStream(resourcePath);
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
            Enumeration<JarEntry> e;
            int internalAddons = 0;

            File file = new File(pathToJar);

            Scanner scanner = new Scanner(file, modClassLoader);
            if (loadScreen != null) {
                loadScreen.logInfo("Scanning jar file: " + file.getPath());
            }
            Scanner.ScanResult scanResult = scanner.scanJar(loadScreen);
            JarFile jarFile = scanResult.getScanner().getJarFile();
            File dir = scanResult.getScanner().getFile();
            InputStream inputStream;
            if (jarFile == null) {
                File file1 = new File(dir, "../../../resources/main/META-INF/addons.json").getCanonicalFile();
                inputStream = new FileInputStream(file1);
            } else {
                JarEntry addonMetaEntry = jarFile.getJarEntry("META-INF/addons.json");
                inputStream = jarFile.getInputStream(addonMetaEntry);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            JsonObject jsonObject = new Gson().fromJson(jsonReader, JsonObject.class);
            JsonArray addons = jsonObject.getAsJsonArray("addons");
            if (addons == null) {
                throw new IllegalStateException("No addons were not found in addon metadata of " + file.getAbsolutePath());
            }

            HashSet<String> fromJson = new HashSet<>();
            HashSet<String> missing = new HashSet<>();
            HashSet<String> fromScan = new HashSet<>();
            HashSet<String> current = new HashSet<>();
            HashMap<String, JsonObject> addonJsons = new HashMap<>();

            for (JsonElement element : addons) {
                if (element.isJsonObject()) {
                    JsonObject addonJson = element.getAsJsonObject();
                    String addonId = addonJson.getAsJsonPrimitive("addonId").getAsString();
                    ResourceEntry.testNamespace(addonId);

                    missing.add(addonId);
                    fromJson.add(addonId);

                    addonJsons.put(addonId, addonJson);
                }
            }

            scanResults.put(file, scanResult);

            List<Class<?>> classes = scanResult.getClasses(Mod.class);
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
                        throw new IllegalArgumentException("Addon has no constructor with 3 parameters. (" + clazz.getName() + ")");
                    }

                    ResourceEntry.testNamespace(mod.addonId());

                    internalAddons++;

                    ModContainer modContainer = new ModContainer() {
                        private final ModInformation addonInfo = new ModInformation(this);
                        private final ModObject<ModInstance> addonObject;
                        private final JsonObject addonJson = addonJsons.get(mod.addonId());

                        {
                            if (ModInstance.class.isAssignableFrom(clazz)) {
                                addonObject = new ModObject<>(mod.addonId(), this, mod, (Class<ModInstance>) clazz);
                            } else {
                                throw new ClassCastException("Tried to cast invalid class for addon: " + getModId());
                            }
                        }

                        @Override
                        public String getModId() {
                            return mod.addonId();
                        }

                        @Override
                        public JsonObject getModProperties() {
                            return addonJson;
                        }

                        @Override
                        public File getSource() {
                            return file;
                        }

                        @Override
                        public JarFile getJarFile() {
                            return jarFile;
                        }

                        @Override
                        public ModInformation getModInfo() {
                            return addonInfo;
                        }

                        @Override
                        public Class<?> getModClass() {
                            return clazz;
                        }

                        @Override
                        public ModObject<? extends ModInstance> getModObject() {
                            return addonObject;
                        }

                        @Override
                        public ModInstance getModInstance() {
                            return getModObject().getAddon();
                        }
                    };

                    ModManager.registerContainer(modContainer);
                    AddonManager.instance().register(mod.addonId(), modContainer.getModObject());

                    if (!fromJson.contains(mod.addonId())) {
                        throw new IllegalArgumentException("Missing addon with ID " + mod.addonId() + " in the addon metadata.");
                    }
                    missing.remove(mod.addonId());
                }
            }

            StringBuilder sb = new StringBuilder();
            for (String s : missing) {
                sb.append(s);
                sb.append(", ");
            }

            if (sb.length() > 0) {
                throw new IllegalStateException("Missing addon classes with ids: " + sb.substring(0, sb.length() - 2));
            }

            if (internalAddons == 0) {
                LOGGER.warn("Addon has no annotated classes.");
            }

            return internalAddons != 0;
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Loading addons", t);
            CrashCategory addonCategory = new CrashCategory("Addon Loading");
            addonCategory.add("File", pathToJar);
            crashReport.addCategory(addonCategory);
            throw crashReport.getReportedException();
        }
    }

    public Scanner.ScanResult getScanResult(File file) {
        return scanResults.get(file);
    }

    public static File getAddonsDir() {
        return ADDONS_DIR;
    }

    public ArrayList<Object> getAddons() {
        return addons;
    }

    public ModClassLoader getAddonClassLoader() {
        return modClassLoader;
    }
}
