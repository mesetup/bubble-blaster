package com.qtech.bubbles.addon.loader

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.addon.Scanner
import com.qtech.bubbles.addon.Scanner.ScanResult
import com.qtech.bubbles.common.References
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.addon.Addon
import com.qtech.bubbles.common.addon.AddonObject
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.common.crash.CrashCategory
import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.common.text.translation.LanguageMap
import com.qtech.bubbles.event.BBAddonSetupEvent
import com.qtech.bubbles.registry.LocaleManager
import com.qtech.bubbles.screen.LoadScreen
import com.qtech.bubbles.settings.GameSettings
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URISyntaxException
import java.util.*
import java.util.jar.JarFile

open class AddonLoader(private val loadScreen: LoadScreen) {
    val addons = ArrayList<Any>()
    val addonClassLoader: AddonClassLoader
    private val scanResults = HashMap<File, ScanResult>()
    fun constructAddons() {
        if (AddonManager.instance.namespaces.isEmpty()) {
            return
        }
        val addonManager: AddonManager = AddonManager.instance
        logger.info("Constructing Mods: ${StringUtils.join(AddonManager.instance.namespaces, ", ")}")
        for (obj in AddonManager.instance.objects) {
            logger.info("Constructing Addon: ${obj.namespace}")
            loadScreen.logInfo(obj.namespace)
            try {
                // Create eventbus and logger for addon.
                val logger: Logger = LogManager.getLogger(obj.annotation.namespace)

                // Create java addon instance.
                val addonClass = obj.addonClass
                val constructor = addonClass.getConstructor(Logger::class.java, String::class.java, AddonObject::class.java)
                constructor.isAccessible = true
                val addon = constructor.newInstance(logger, obj.annotation.namespace, obj)

                // Register java addon.
                addonManager.register(obj.namespace, addon!!)

//                InputStream inputStream = addonClassLoader.getResourceAsStream("/assets/" + object.getAnnotation().addonId() + "/lang/en_uk.lang")
//                LanguageMap.inject(inputStream);
                addons.add(addon)
            } catch (t: Throwable) {
                val crashReport = CrashReport("Constructing addon", t)
                val addonCategory = CrashCategory("Addon was constructing")
                addonCategory.add("Addon ID", obj)
                addonCategory.add("File", obj.container.source.path)
                throw crashReport.reportedException
            }
        }
    }

    fun addonSetup() {
        BubbleBlaster.eventBus.post(BBAddonSetupEvent(this))
        LocaleManager.manager.register("af_za")
        LocaleManager.manager.register("el_gr")
        LocaleManager.manager.register("it_it")
        LocaleManager.manager.register("en_us")
        LocaleManager.manager.register("en_uk")
        LocaleManager.manager.register("es_es")
        LocaleManager.manager.register("nl_nl")
        LocaleManager.manager.register("nl_be")
        LocaleManager.manager.register("fy_nl")
        LocaleManager.manager.register("zh_cn")
        val locales = LocaleManager.manager.keys()
        for (locale in locales) {
            val langMap = LanguageMap(locale)
            var resourcePath = "/assets/bubbleblaster/lang/" + locale.toString().lowercase() + ".lang"
            var stream = javaClass.getResourceAsStream(resourcePath)
            if (stream == null) {
                continue
            } else {
                langMap.injectInst(stream)
            }
            for (`object` in AddonManager.instance.objects) {
                val addonId: String = `object`.annotation.namespace
                resourcePath = "/assets/" + addonId + "/lang/" + locale.toString().lowercase() + ".lang"
                stream = `object`.addonClass.getResourceAsStream(resourcePath)
                if (stream == null) {
//                    logger.warn(String.format("Cannot find language file: %s", resourcePath));
                    continue
                }
                langMap.injectInst(stream)
            }
        }
        var resourcePath = "/assets/bubbleblaster/lang/" + GameSettings.instance().language + ".lang"
        var stream = javaClass.getResourceAsStream(resourcePath)
        if (stream == null) {
//            Game.getLogger().warn(String.format("Cannot find language file: %s", resourcePath));
        } else {
            LanguageMap.inject(stream)
        }
        for (`object` in AddonManager.instance.objects) {
            val addonId: String = `object`.annotation.namespace
            resourcePath = "/assets/" + addonId + "/lang/" + GameSettings.instance().language + ".lang"
            stream = `object`.addonClass.getResourceAsStream(resourcePath)
            if (stream == null) {
//                Game.getLogger().warn(String.format("Cannot find language file: %s", resourcePath));
                continue
            }
            LanguageMap.inject(stream)
        }
    }

    fun loadJar(pathToJar: String, loadScreen: LoadScreen): Boolean {
        return try {
//        Enumeration<JarEntry> e;
//        int internalAddons = 0;
//
//        JarFile jarFile = new JarFile(pathToJar);
//        e = jarFile.entries();
//
//        while (e.hasMoreElements()) {
//            JarEntry je = e.nextElement();
//            if (je.isDirectory() || !je.getName().endsWith(".class")) {
//                continue;
//            }
//
//            // -6 because of .class
//            String className = je.getName().substring(0, je.getName().length() - 6);
//            className = className.replace('/', '.');
//            try {
//                Class.forName(className);
//
//                throw new IllegalArgumentException("Classname already defined in classpath.");
//            } catch (ClassNotFoundException ignored) {
//
//            }
//        }
//
//        addonClassLoader.addFile(new File(pathToJar));
//        internalAddons = 0;
//
//        e = jarFile.entries();
//
//        while (e.hasMoreElements()) {
//            JarEntry je = e.nextElement();
//            if(je.isDirectory() || !je.getName().endsWith(".class")){
//                continue;
//            }
//
//            // -6 because of .class
//            String className = je.getName().substring(0,je.getName().length()-6);
//            className = className.replace('/', '.');
//            Class<?> c = addonClassLoader.loadClass(className);
            var internalAddons = 0
            val source = File(pathToJar)
            val scanner = Scanner(source, addonClassLoader)
            loadScreen.logInfo("Scanning jar file: " + source.path)
            val scanResult = scanner.scanJar(loadScreen)
            val jarFile = scanResult.scanner.jarFile
            val dir = scanResult.scanner.file
            val inputStream: InputStream = if (jarFile == null) {
                val file1 = File(dir, "../../../resources/main/META-INF/addons.json").canonicalFile
                FileInputStream(file1)
            } else {
                val addonMetaEntry = jarFile.getJarEntry("META-INF/addons.json")
                jarFile.getInputStream(addonMetaEntry)
            }
            val inputStreamReader = InputStreamReader(inputStream)
            val jsonReader = JsonReader(inputStreamReader)
            val jsonObject = Gson().fromJson<JsonObject>(jsonReader, JsonObject::class.java)
            val addons = jsonObject.getAsJsonArray("addons") ?: throw IllegalStateException("No addons were not found in addon metadata of " + source.absolutePath)
            val fromJson = HashSet<String>()
            val missing = HashSet<String>()
            val addonJsons = HashMap<String, JsonObject>()
            for (element in addons) {
                if (element.isJsonObject) {
                    val addonJson = element.asJsonObject
                    val addonId = addonJson.getAsJsonPrimitive("addonId").asString
                    ResourceLocation.testNamespace(addonId)
                    missing.add(addonId)
                    fromJson.add(addonId)
                    addonJsons[addonId] = addonJson
                }
            }
            scanResults[source] = scanResult
            val classes: List<Class<*>> = scanResult.getClasses(Addon::class)
            for (clazz in classes) {
                val addon = clazz.getDeclaredAnnotation(Addon::class.java)
                if (addon != null) {
                    var flag = false
                    for (constructor in clazz.constructors) {
                        if (constructor.parameterCount == 3) {
                            flag = true
                            break
                        }
                    }
                    require(flag) { "Addon has no constructor with 3 parameters. (" + clazz.name + ")" }
                    ResourceLocation.testNamespace(addon.namespace)
                    internalAddons++

                    @Suppress("UNCHECKED_CAST")
                    val addonContainer: AddonContainer = object : AddonContainer() {
                        override val info = AddonInfo(this)
                        override val json = addonJsons[addon.namespace]
                        override val source: File = source
                        override val namespace: String = addon.namespace
                        override val jarFile: JarFile? = jarFile
                        override val clazz: Class<*> = clazz
                        override val obj: AddonObject<out AddonInstance> = AddonObject(addon.namespace, this, addon, clazz as Class<out AddonInstance>)
                        override val instance: AddonInstance
                            get() = obj.addon
                    }

                    AddonManager.instance.register(addon.namespace, addonContainer)
                    require(fromJson.contains(addon.namespace)) { "Missing addon with ID " + addon.namespace + " in the addon metadata." }
                    missing.remove(addon.namespace)
                }
            }
            val sb = StringBuilder()
            for (s in missing) {
                sb.append(s)
                sb.append(", ")
            }
            check(sb.isEmpty()) { "Missing addon classes with ids: " + sb.substring(0, sb.length - 2) }
            if (internalAddons == 0) {
                logger.warn("Addon has no annotated classes.")
            }
            internalAddons != 0
        } catch (t: Throwable) {
            val crashReport = CrashReport("Loading addons", t)
            val addonCategory = CrashCategory("Addon Loading")
            addonCategory.add("File", pathToJar)
            crashReport.addCategory(addonCategory)
            throw crashReport.reportedException
        }
    }

    fun getScanResult(file: File): ScanResult? {
        return scanResults[file]
    }

    companion object {
        val addonsDir = References.ADDONS_DIR
        private val logger = LogManager.getLogger("QAL")
    }

    //    private final List<AddonContainer> containers = new ArrayList<>();
    init {
        var existsBefore = true
        addonClassLoader = AddonClassLoader(BubbleBlaster.mainClassLoader)
        if (!addonsDir.exists()) {
            existsBefore = false
            val flag = addonsDir.mkdirs()
            check(flag) { "Addons directory wasn't created. (" + addonsDir.path + ")" }
        } else if (addonsDir.isFile) {
            existsBefore = false
            val flag1 = addonsDir.delete()
            val flag2 = addonsDir.mkdir()
            check(flag1) { "Addons directory wasn't deleted. (" + addonsDir.path + ")" }
            check(flag2) { "Addons directory wasn't created. (" + addonsDir.path + ")" }
        }
        val location = BubbleBlaster::class.java.protectionDomain.codeSource.location
        try {
            val file = File(location.toURI())
            val flag = loadJar(file.path, loadScreen)
            if (!flag) {
                logger.info("No flag.")
                loadScreen.logInfo(String.format("Found non-addon file: %s", file.name))
                logger.info(String.format("Found non-addon file: %s", file.name))
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        if (existsBefore) {
            for (file in Objects.requireNonNull(addonsDir.listFiles())) {
                if (file.name.endsWith(".jar")) {
                    logger.debug("Loading jar file: " + file.name)
                    val flag = loadJar(file.path, loadScreen)
                    if (!flag) {
                        logger.info("No flag.")
                        loadScreen.logInfo(String.format("Found non-addon file: %s", file.name))
                        logger.info(String.format("Found non-addon file: %s", file.name))
                    }
                } else {
                    loadScreen.logInfo(String.format("Found non-addon file: %s", file.name))
                    logger.info(String.format("Found non-addon file: %s", file.name))
                }
            }
        }
    }
}