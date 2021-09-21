package com.ultreon.bubbles.core;

import com.ultreon.bubbles.mod.loader.Scanner;
import com.ultreon.bubbles.screen.LoadScreen;
import com.ultreon.hydro.core.AntiMod;
import com.ultreon.preloader.PreClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * A simple delegating class loader used to load mods into the system
 *
 * @author cpw
 */
public class ModClassLoader extends URLClassLoader {
    private PreClassLoader mainClassLoader;
    private GameClassLoader gameClassLoader;
    @SuppressWarnings("FieldMayBeFinal")

    private final List<String> sources;
    private final File modSource;

    // Classes
    private final Map<String, Class<?>> classes = new HashMap<>();

    // File id.
    private final String fileId;

    public ModClassLoader(ClassLoader parent, File file, String fileId) {
        super(new URL[0], null);
        if (parent instanceof PreClassLoader) {
            this.mainClassLoader = (PreClassLoader) parent;
        }
        this.sources = new ArrayList<>();
        this.fileId = fileId;
        this.modSource = file;
    }

    public void addFile(File modFile) throws MalformedURLException {
        String fileId = gameClassLoader.getModFileId(modFile);
    }

    public void addMod(String modFileId) throws MalformedURLException {
        this.sources.add(modFileId);
    }

    public boolean isInternalPackage(String pkg) {
        return mainClassLoader.isInternalPackage(pkg);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (Objects.equals(name, this.getClass().getName())) {
            throw new ClassNotFoundException("Class not found in current's classloader for mod: " + this.fileId);
        }

        if (classes.containsKey(name)) {
            return classes.get(name);
        }

        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (Objects.equals(name, this.getClass().getName())) {
            throw new ClassNotFoundException("Class not found in current's classloader for mod: " + this.fileId);
        }

        if (name.startsWith("com.ultreon.bubbles.core.")) {
            throw new ClassNotFoundException("Class not found in current's classloader for mod: " + this.fileId);
        }

        if (classes.containsKey(name)) {
            return classes.get(name);
        }

        Class<?> aClass = null;
        try {
            aClass = gameClassLoader.findClass(name);
        } catch (ClassNotFoundException e) {
            try {
                return super.findClass(name);
            } catch (ClassNotFoundException e1) {
                for (String file : sources) {
                    Class<?> classOrNull = gameClassLoader.getClassOrNull(file, name);
                    if (classOrNull != null) {
                        return classOrNull;
                    }
                }
            }
        }
        if (aClass == null || aClass.isAnnotationPresent(AntiMod.class)) {
            throw new ClassNotFoundException("Class not found in current's classloader for mod: " + this.fileId);
        }

        return aClass;
    }

    Scanner.Result scan() {
        Scanner scanner = new Scanner(modSource, this);
        return scanner.scanJar(LoadScreen.get());
    }

    public File getModSource() {
        return modSource;
    }
}