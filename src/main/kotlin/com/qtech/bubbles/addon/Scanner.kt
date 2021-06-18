package com.qtech.bubbles.addon

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.addon.loader.AddonClassLoader
import com.qtech.bubbles.common.crash.CrashCategory
import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.screen.LoadScreen
import org.apache.commons.lang.SystemUtils
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.stream.Collectors
import kotlin.reflect.KClass

class Scanner(val file: File, private val classLoader: AddonClassLoader) {
    private val isQBubbles: Boolean
    var bubbleBlasterFile: File? = null
    var jarFile: JarFile? = null
        private set

    fun scanJar(loadScreen: LoadScreen?): ScanResult {
        val classes = HashMap<KClass<out Annotation>, ArrayList<Class<*>>>()
        var className: String? = null
        var jarEntry: JarEntry? = null
        var annotationScan = false
        try {
            var e: Enumeration<JarEntry>
            logger.info(file.path)
            //            String path = file.getPath();
//            path = path.replaceAll("/", PATH_SEPARATOR);
//            if (path.endsWith(PATH_SEPARATOR)) {
//                path += System.getProperty(PATH_SEPARATOR);
//            }
//
//            int length = path.length();
//
//            String string = new File(file, "hello/hello.class").getPath();
//            String substring = string.substring(length);
//            if (substring.startsWith("/") || substring.startsWith("\\")) {
//                substring = substring.substring(1);
//            }
//
//            logger.info(substring);
            try {
                if (file.isDirectory) {
                    logger.warn("Running from game development environment (GDE)")
                    if (!isQBubbles) {
                        for (file in Files.walk(file.toPath(), 30)
                            .map { obj: Path -> obj.toFile() }
                            .collect(Collectors.toList())) {
                            var path = this.file.absolutePath
                            path = path.replace("/".toRegex(), SystemUtils.PATH_SEPARATOR)
                            if (path.endsWith(SystemUtils.PATH_SEPARATOR)) {
                                path += System.getProperty(SystemUtils.PATH_SEPARATOR)
                            }
                            val length = path.length
                            val string = file.absolutePath
                            var substring = string.substring(length)
                            if (substring.startsWith("/") || substring.startsWith("\\")) {
                                substring = substring.substring(1)
                            }
                            substring = substring.replace("\\\\".toRegex(), "/")
                            if (file.isDirectory || !(file.name.endsWith(".class") || file.name.endsWith(".java"))) {
                                continue
                            }
                            logger.debug("Checking: " + file.path)

                            // -6 because of .class
                            val className1 = substring.substring(0, substring.length - 6)
                            className = className1.replace('/', '.')
                            try {
                                val aClass = Class.forName(className)
                                require(!classLoader.isInternalPackage(aClass.getPackage().name)) { "Classname already defined in classpath: $className" }
                            } catch (ignored: ClassNotFoundException) {
                            }
                        }
                    }
                    classLoader.addFile(file)
                    annotationScan = true
                    val walk = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS)
                    val walk1 = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS)
                    val walk2 = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS)
                    logger.info(walk.collect(Collectors.toList()))
                    logger.info(walk1.map { obj: Path -> obj.toFile() }.collect(Collectors.toList()))
                    for (file in walk2.map { obj: Path -> obj.toFile() }.collect(Collectors.toList())) {
                        if (file.isDirectory || !(file.name.endsWith(".class") || file.name.endsWith(".java"))) {
                            continue
                        }
                        var path = this.file.absolutePath
                        path = path.replace("/".toRegex(), SystemUtils.PATH_SEPARATOR)
                        if (path.endsWith(SystemUtils.PATH_SEPARATOR)) {
                            path += System.getProperty(SystemUtils.PATH_SEPARATOR)
                        }
                        val length = path.length
                        val string = file.absolutePath
                        var substring = string.substring(length)
                        if (substring.startsWith("/") || substring.startsWith("\\")) {
                            substring = substring.substring(1)
                        }
                        substring = substring.replace("\\\\".toRegex(), "/")
                        logger.debug("Scanning file: " + file.path)
                        if (isQBubbles) {
                            if (!substring.startsWith("com/qtech")) {
                                continue
                            }
                        }
                        loadScreen?.logInfo("Scanning: " + file.path + "!/" + substring)
                        // -6 because of .class
                        val className1 = substring.substring(0, substring.length - 6)
                        className = className1.replace('/', '.')
                        try {
                            val c = classLoader.loadClass(className)
                            val annotations = c.declaredAnnotations
                            //                            logger.info(Arrays.toString(annotations));
                            for (annotation in annotations) {
                                if (!classes.containsKey(annotation.annotationClass)) {
                                    classes[annotation.annotationClass] = ArrayList()
                                }
                                classes[annotation.annotationClass]!!.add(c)
                                //                                logger.info(classes);
                            }
                        } catch (t: Throwable) {
                            logger.debug("Couldn't load class: $className")
                        }
                        //                        logger.info("Scanned: " + file.getName());
                    }
                } else if (file.isFile) {
                    jarFile = JarFile(file.path)
                    if (!isQBubbles) {
                        e = jarFile!!.entries()
                        while (e.hasMoreElements()) {
                            val je = e.nextElement()
                            jarEntry = je
                            if (je.isDirectory || !je.name.endsWith(".class")) {
                                continue
                            }

                            // -6 because of .class
                            val className1 = je.name.substring(0, je.name.length - 6)
                            className = className1.replace('/', '.')
                            try {
                                val aClass = Class.forName(className)
                                require(!classLoader.isInternalPackage(aClass.getPackage().name)) { "Classname already defined in classpath: $className" }
                            } catch (ignored: ClassNotFoundException) {
                            }
                        }
                    }
                    classLoader.addFile(file)
                    e = jarFile!!.entries()
                    annotationScan = true
                    while (e.hasMoreElements()) {
                        val je = e.nextElement()
                        jarEntry = je
                        if (je.isDirectory || !je.name.endsWith(".class")) {
                            continue
                        }
                        if (isQBubbles) {
                            if (!je.name.startsWith("com/qtech")) {
                                continue
                            }
                        }
                        if (loadScreen != null) {
//                        loadScene.logInfo("Scanning: " + file.getPath() + "!/" + je.getName());
                        }

                        // -6 because of .class
                        val className1 = je.name.substring(0, je.name.length - 6)
                        className = className1.replace('/', '.')
                        try {
                            val c = classLoader.loadClass(className)
                            val annotations = c.declaredAnnotations
                            for (annotation in annotations) {
                                if (!classes.containsKey(annotation.annotationClass)) {
                                    classes[annotation.annotationClass] = ArrayList()
                                }
                                classes[annotation.annotationClass]!!.add(c)
                            }
                            //                    Annotation[] annotations1 = c.getAnnotations();
//                    logger.info(Arrays.toString(annotations1));
//                    for (Annotation annotation : annotations1) {
//                        if (!classes.containsKey(annotation.getClass())) {
//                            classes.put(annotation.getClass(), new ArrayList<>());
//                        }
//                        classes.get(annotation.getClass()).add(c);
//                        logger.info(classes)
//                    }
                        } catch (t: Throwable) {
                            logger.error("Couldn't load class: $className")
                            continue
                        }
                        logger.info("Scanned: " + je.name)
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        } catch (t: Throwable) {
            val crashReport = CrashReport("Jar File being scanned", t)
            val addonCategory = CrashCategory("Jar Entry being scanned")
            addonCategory.add("Class Name", className)
            addonCategory.add("Entry", jarEntry?.name)
            addonCategory.add("Annotation Scan", annotationScan)
            crashReport.addCategory(addonCategory)
            throw crashReport.reportedException
        }
        return ScanResult(this, classes)
    }

    class ScanResult(val scanner: Scanner, private val classes: HashMap<KClass<out Annotation>, ArrayList<Class<*>>>) {
        fun getClasses(annotation: KClass<out Annotation>): List<Class<*>> {
            return if (!classes.containsKey(annotation)) {
//                System.out.println(this.classes.containsKey(Addon.class));
//                throw new NullPointerException(null);
                ArrayList()
            } else classes[annotation]!!
        }
    }

    companion object {
        private val logger = LogManager.getLogger("Scanner")
    }

    init {
        val location = BubbleBlaster::class.java.protectionDomain.codeSource.location
        try {
            bubbleBlasterFile = File(location.toURI())
        } catch (ignored: URISyntaxException) {
        }
        logger.info("Found Bubble Blaster 2 file instance: " + bubbleBlasterFile.toString())
        isQBubbles = file.absolutePath == bubbleBlasterFile!!.absolutePath
    }
}