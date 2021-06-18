package com.qtech.bubbles.addon.loader

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.qtech.bubbles.BubbleBlaster
import com.qtech.preloader.PreClassLoader
import java.io.File
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.net.URLClassLoader
import java.util.*

/**
 * A simple delegating class loader used to load addons into the system
 *
 * @author cpw
 */
class AddonClassLoader(parent: ClassLoader?) : URLClassLoader(arrayOfNulls(0), null) {
    private var mainClassLoader: PreClassLoader? = null
    private val sources: MutableList<File>
    @Throws(MalformedURLException::class)
    fun addFile(addonFile: File) {
        val url = addonFile.toURI().toURL()
        mainClassLoader!!.addURL(url)
        sources.add(addonFile)
    }

    fun isInternalPackage(pkg: String): Boolean {
        return mainClassLoader!!.isInternalPackage(pkg)
    }

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String): Class<*> {
        return mainClassLoader!!.loadClass(name)
    }

    @get:Throws(LoaderException::class)
    val parentSources: Array<File>
        get() = try {
            val files: MutableList<File> = ArrayList()
            for (url in mainClassLoader!!.sources) {
                val uri = url!!.toURI()
                if (uri!!.scheme == "file") {
                    files.add(File(uri))
                }
            }
            files.toTypedArray()
        } catch (e: URISyntaxException) {
            BubbleBlaster.logger.error("Unable to process our input to locate the bubbleblaster code", e)
            throw LoaderException(e)
        }

    fun isDefaultLibrary(file: File): Boolean {
        val home = System.getProperty("java.home") // Nullcheck just in case some JVM decides to be stupid
        if (home != null && file.absolutePath.startsWith(home)) return true
        // Should really pull this from the json somehow, but we dont have that at runtime.
        val name = file.name
        if (!name.endsWith(".jar")) return false
        val prefixes = arrayOf(
            "com.qtech.launchwrapper-",
            "asm-all-",
            "akka-actor_2.11-",
            "config-",
            "scala-",
            "jopt-simple-",
            "lzma-",
            "realms-",
            "httpclient-",
            "httpcore-",
            "vecmath-",
            "trove4j-",
            "icu4j-core-mojang-",
            "codecjorbis-",
            "codecwav-",
            "libraryjavawound-",
            "librarylwjglopenal-",
            "soundsystem-",
            "netty-all-",
            "guava-",
            "commons-lang3-",
            "commons-compress-",
            "commons-logging-",
            "commons-io-",
            "commons-codec-",
            "jinput-",
            "jutils-",
            "gson-",
            "authlib-",
            "log4j-api-",
            "log4j-core-",
            "lwjgl-",
            "lwjgl_util-",
            "twitch-",
            "jline-",
            "jna-",
            "platform-",
            "oshi-core-",
            "netty-",
            "libraryjavasound-",
            "fastutil-"
        )
        for (s in prefixes) {
            if (name.startsWith(s)) return true
        }
        return false
    }

    fun clearNegativeCacheFor(classList: Set<String>?) {
        mainClassLoader!!.clearInvalidDataCache(classList)
    }

    //    public AddonAPITransformer addAddonAPITransformer(ASMDataTable dataTable) {
    //        mainClassLoader.registerTransformer("net.minecraftforge.fml.common.asm.transformers.AddonAPITransformer");
    //        List<IClassTransformer> transformers = mainClassLoader.getTransformers();
    //        AddonAPITransformer addonAPI = (AddonAPITransformer) transformers.get(transformers.size()-1);
    //        addonAPI.initTable(dataTable);
    //        return addonAPI;
    //    }
    var parentURLs: List<URL>? = null
    fun containsSource(source: File): Boolean {
        if (parentURLs == null) {
            parentURLs = Arrays.asList(*mainClassLoader!!.urLs)
        }
        return try {
            parentURLs!!.contains(source.toURI().toURL())
        } catch (e: MalformedURLException) {
            // shouldn't happen
            false
        }
    }

    companion object {
        private val defaultLibraries: List<String> = ImmutableList.of("jinput.jar", "lwjgl.jar", "lwjgl_util.jar", "rt.jar")

        @JvmName("getDefaultLibraries1")
        fun getDefaultLibraries(): List<String> {
            return defaultLibraries
        }
    }

    init {
        if (parent is PreClassLoader) {
            mainClassLoader = parent
        }
        sources = Lists.newArrayList()
    }
}