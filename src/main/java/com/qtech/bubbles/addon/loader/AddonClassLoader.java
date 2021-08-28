package com.qtech.bubbles.addon.loader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.qtech.bubbles.BubbleBlaster;
import com.qtech.preloader.PreClassLoader;

import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * A simple delegating class loader used to load addons into the system
 *
 * @author cpw
 */
public class AddonClassLoader extends URLClassLoader {
    private static final List<String> STANDARD_LIBRARIES = ImmutableList.of("jinput.jar", "lwjgl.jar", "lwjgl_util.jar", "rt.jar");
    private PreClassLoader mainClassLoader;
    @SuppressWarnings("FieldMayBeFinal")
    private final List<File> sources;

    public AddonClassLoader(ClassLoader parent) {
        super(new URL[0], null);
        if (parent instanceof PreClassLoader) {
            this.mainClassLoader = (PreClassLoader) parent;
        }
        this.sources = Lists.newArrayList();
    }

    public void addFile(File addonFile) throws MalformedURLException {
        URL url = addonFile.toURI().toURL();
        mainClassLoader.addURL(url);
        this.sources.add(addonFile);
    }

    public boolean isInternalPackage(String pkg) {
        return mainClassLoader.isInternalPackage(pkg);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return mainClassLoader.loadClass(name);
    }

    public File[] getParentSources() throws LoaderException {
        try {
            List<File> files = new ArrayList<>();
            for (URL url : mainClassLoader.getSources()) {
                URI uri = url.toURI();
                if (uri.getScheme().equals("file")) {
                    files.add(new File(uri));
                }
            }
            return files.toArray(new File[]{});
        } catch (URISyntaxException e) {
            BubbleBlaster.getLogger().error("Unable to process our input to locate the qbubbles code", e);
            throw new LoaderException(e);
        }
    }

    public List<String> getDefaultLibraries() {
        return STANDARD_LIBRARIES;
    }

    public boolean isDefaultLibrary(File file) {
        String home = System.getProperty("java.home"); // Nullcheck just in case some JVM decides to be stupid
        if (home != null && file.getAbsolutePath().startsWith(home)) return true;
        // Should really pull this from the json somehow, but we dont have that at runtime.
        String name = file.getName();
        if (!name.endsWith(".jar")) return false;
        String[] prefixes =
                {
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
                };
        for (String s : prefixes) {
            if (name.startsWith(s)) return true;
        }
        return false;
    }

    public void clearNegativeCacheFor(Set<String> classList) {
        mainClassLoader.clearInvalidDataCache(classList);
    }

//    public AddonAPITransformer addAddonAPITransformer(ASMDataTable dataTable) {
//        mainClassLoader.registerTransformer("net.minecraftforge.fml.common.asm.transformers.AddonAPITransformer");
//        List<IClassTransformer> transformers = mainClassLoader.getTransformers();
//        AddonAPITransformer addonAPI = (AddonAPITransformer) transformers.get(transformers.size()-1);
//        addonAPI.initTable(dataTable);
//        return addonAPI;
//    }

    List<URL> parentURLs = null;

    public boolean containsSource(File source) {
        if (parentURLs == null) {
            parentURLs = Arrays.asList(mainClassLoader.getURLs());
        }
        try {
            return parentURLs.contains(source.toURI().toURL());
        } catch (MalformedURLException e) {
            // shouldn't happen
            return false;
        }
    }
}