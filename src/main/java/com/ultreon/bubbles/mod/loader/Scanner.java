package com.ultreon.bubbles.mod.loader;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.commons.crash.CrashCategory;
import com.ultreon.commons.crash.CrashLog;
import com.ultreon.bubbles.screen.LoadScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.io.File.pathSeparator;

@SuppressWarnings("unused")
public final class Scanner {
    private final File file;
    private final ModClassLoader classLoader;
    private final boolean isQBubbles;
    private File qbubblesFile = null;
    private JarFile jarFile;
    private static final Logger logger = LogManager.getLogger("Scanner");

    public Scanner(File file, ModClassLoader classLoader) {
        this.classLoader = classLoader;
        this.file = file;

        URL location = BubbleBlaster.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            qbubblesFile = new File(location.toURI());
        } catch (URISyntaxException ignored) {

        }

        logger.info("Found QBubbles file instance: " + qbubblesFile.toString());

        isQBubbles = file.getAbsolutePath().equals(qbubblesFile.getAbsolutePath());
    }

    public ScanResult scanJar(@Nullable LoadScreen loadScreen) {
        HashMap<Class<? extends Annotation>, ArrayList<Class<?>>> classes = new HashMap<>();
        String className = null;
        JarEntry jarEntry = null;
        boolean annotationScan = false;

        try {
            Enumeration<JarEntry> e;
            File[] files;

            logger.info(file.getPath());
//            String path = file.getPath();
//            path = path.replaceAll("/", pathSeparator);
//            if (path.endsWith(pathSeparator)) {
//                path += System.getProperty(pathSeparator);
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
                if (file.isDirectory()) {
                    logger.warn("Running from game development environment (GDE)");

                    if (!isQBubbles) {
                        for (File file : Files.walk(file.toPath(), 30)
                                .map(Path::toFile)
                                .collect(Collectors.toList())) {

                            String path = this.file.getAbsolutePath();
                            path = path.replaceAll("/", pathSeparator);
                            if (path.endsWith(pathSeparator)) {
                                path += System.getProperty(pathSeparator);
                            }

                            int length = path.length();

                            String string = file.getAbsolutePath();
                            String substring = string.substring(length);
                            if (substring.startsWith("/") || substring.startsWith("\\")) {
                                substring = substring.substring(1);
                            }

                            substring = substring.replaceAll("\\\\", "/");

                            if (file.isDirectory() || !(file.getName().endsWith(".class") || file.getName().endsWith(".java"))) {
                                continue;
                            }

                            logger.debug("Checking: " + file.getPath());

                            // -6 because of .class
                            String className1 = substring.substring(0, substring.length() - 6);
                            className = className1.replace('/', '.');
                            try {
                                Class<?> aClass = Class.forName(className);
                                if (classLoader.isInternalPackage(aClass.getPackage().getName())) {
                                    throw new IllegalArgumentException("Classname already defined in classpath: " + className);
                                }
                            } catch (ClassNotFoundException ignored) {

                            }
                        }
                    }

                    classLoader.addFile(file);
                    annotationScan = true;

                    Stream<Path> walk = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS);
                    Stream<Path> walk1 = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS);
                    Stream<Path> walk2 = Files.walk(file.toPath(), 30, FileVisitOption.FOLLOW_LINKS);
                    logger.info(walk.collect(Collectors.toList()));
                    logger.info(walk1.map(Path::toFile).collect(Collectors.toList()));

                    for (File file : walk2.map(Path::toFile).collect(Collectors.toList())) {
                        if (file.isDirectory() || !(file.getName().endsWith(".class") || file.getName().endsWith(".java"))) {
                            continue;
                        }

                        String path = this.file.getAbsolutePath();
                        path = path.replaceAll("/", pathSeparator);
                        if (path.endsWith(pathSeparator)) {
                            path += System.getProperty(pathSeparator);
                        }

                        int length = path.length();

                        String string = file.getAbsolutePath();
                        String substring = string.substring(length);
                        if (substring.startsWith("/") || substring.startsWith("\\")) {
                            substring = substring.substring(1);
                        }

                        substring = substring.replaceAll("\\\\", "/");

                        logger.debug("Scanning file: " + file.getPath());

                        if (isQBubbles) {
                            if (!substring.startsWith("com/ultreon")) {
                                continue;
                            }
                        }

                        if (loadScreen != null) {
                            loadScreen.logInfo("Scanning: " + file.getPath() + "!/" + substring);
                        }
                        // -6 because of .class
                        String className1 = substring.substring(0, substring.length() - 6);
                        className = className1.replace('/', '.');
                        try {
                            Class<?> c = classLoader.loadClass(className);
                            Annotation[] annotations = c.getDeclaredAnnotations();
//                            logger.info(Arrays.toString(annotations));
                            for (Annotation annotation : annotations) {
                                if (!classes.containsKey(annotation.annotationType())) {
                                    classes.put(annotation.annotationType(), new ArrayList<>());
                                }
                                classes.get(annotation.annotationType()).add(c);
//                                logger.info(classes);
                            }
                        } catch (Throwable t) {
                            logger.debug("Couldn't load class: " + className);
                        }
//                        logger.info("Scanned: " + file.getName());
                    }
                } else if (file.isFile()) {
                    jarFile = new JarFile(file.getPath());
                    if (!isQBubbles) {
                        e = jarFile.entries();
                        while (e.hasMoreElements()) {
                            JarEntry je = e.nextElement();
                            jarEntry = je;
                            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                                continue;
                            }

                            // -6 because of .class
                            String className1 = je.getName().substring(0, je.getName().length() - 6);
                            className = className1.replace('/', '.');
                            try {
                                Class<?> aClass = Class.forName(className);
                                if (classLoader.isInternalPackage(aClass.getPackage().getName())) {
                                    throw new IllegalArgumentException("Classname already defined in classpath: " + className);
                                }
                            } catch (ClassNotFoundException ignored) {

                            }
                        }
                    }

                    classLoader.addFile(file);

                    e = jarFile.entries();

                    annotationScan = true;

                    while (e.hasMoreElements()) {
                        JarEntry je = e.nextElement();
                        jarEntry = je;
                        if (je.isDirectory() || !je.getName().endsWith(".class")) {
                            continue;
                        }

                        if (isQBubbles) {
                            if (!je.getName().startsWith("com/ultreon")) {
                                continue;
                            }
                        }

                        //noinspection StatementWithEmptyBody
                        if (loadScreen != null) {
//                        loadScene.logInfo("Scanning: " + file.getPath() + "!/" + je.getName());
                        }

                        // -6 because of .class
                        String className1 = je.getName().substring(0, je.getName().length() - 6);
                        className = className1.replace('/', '.');
                        try {
                            Class<?> c = classLoader.loadClass(className);
                            Annotation[] annotations = c.getDeclaredAnnotations();
                            for (Annotation annotation : annotations) {
                                if (!classes.containsKey(annotation.annotationType())) {
                                    classes.put(annotation.annotationType(), new ArrayList<>());
                                }
                                classes.get(annotation.annotationType()).add(c);
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
                        } catch (Throwable t) {
                            logger.error("Couldn't load class: " + className);
                            continue;
                        }
                        logger.info("Scanned: " + je.getName());
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } catch (Throwable t) {
            CrashLog crashLog = new CrashLog("Jar File being scanned", t);
            CrashCategory addonCategory = new CrashCategory("Jar Entry being scanned");
            addonCategory.add("Class Name", className);
            addonCategory.add("Entry", jarEntry != null ? jarEntry.getName() : null);
            addonCategory.add("Annotation Scan", annotationScan);
            crashLog.addCategory(addonCategory);
            throw crashLog.createCrash();
        }
        return new ScanResult(this, classes);
    }

    public File getFile() {
        return file;
    }

    @Nullable
    public JarFile getJarFile() {
        return jarFile;
    }

    public static class ScanResult {
        private final HashMap<Class<? extends Annotation>, ArrayList<Class<?>>> classes;
        private final Scanner scanner;

        public ScanResult(Scanner scanner, HashMap<Class<? extends Annotation>, ArrayList<Class<?>>> classes) {
            this.classes = classes;
            this.scanner = scanner;
        }

        @SuppressWarnings({"UnusedReturnValue"})
        public List<Class<?>> getClasses(Class<? extends Annotation> annotation) {
            if (!this.classes.containsKey(annotation)) {
//                System.out.println(this.classes.containsKey(Addon.class));
//                throw new NullPointerException(null);
                return new ArrayList<>();
            }

            return this.classes.get(annotation);
        }

        public Scanner getScanner() {
            return scanner;
        }
    }

    public File getQbubblesFile() {
        return qbubblesFile;
    }
}
