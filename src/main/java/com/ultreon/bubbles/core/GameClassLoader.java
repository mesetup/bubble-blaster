package com.ultreon.bubbles.core;

import com.google.gson.*;
import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.mod.loader.Scanner;
import com.ultreon.bubbles.screen.LoadScreen;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GameClassLoader extends URLClassLoader {
    private final Map<String, ModClassLoader> modClassLoaders = new HashMap<>();
    private final Map<String, Scanner.Result> scans = new HashMap<>();
    private static final Gson gson = new Gson();

    private final HashMap<String, String> fileIdMap = new HashMap<>();
    private final HashMap<String, File> fileMap = new HashMap<>();
    private static GameClassLoader instance = new GameClassLoader();
    private final File gameFile;
    private Scanner.Result scanResult;

    @SneakyThrows
    public GameClassLoader() {
        super(new URL[]{BubbleBlaster.class.getProtectionDomain().getCodeSource().getLocation()}, BubbleBlaster.class.getClassLoader());
        this.gameFile = new File(BubbleBlaster.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (instance != null) {
            throw new IllegalStateException("Game class loader already initialized.");
        }
        instance = this;
    }

    public static GameClassLoader get() {
        return instance;
    }

    @SneakyThrows
    public ModClassLoader addMod(File file) {
        JarFile jarFile = new JarFile(file);
        JarEntry jarEntry = jarFile.getJarEntry("META-INF/modFile.json");
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        JsonObject modFileData = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);

        String fileId;
        if (modFileData.has("fileId")) {
            JsonElement jsonElement = modFileData.get("");
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (asJsonPrimitive.isString()) {
                    fileId = asJsonPrimitive.getAsString();
                } else throw new JsonParseException("Expected to find json string at member 'fileId'.");
            } else throw new JsonParseException("Expected to find json string (primitive) at member 'fileId'");
        } else {
            throw new JsonParseException("Expected to find member 'fileId' but got nothing.");
        }

        ModClassLoader modClassLoader = new ModClassLoader(this, file, fileId);

        this.fileIdMap.put(file.getAbsolutePath(), fileId);
        this.fileMap.put(fileId, file);
        this.modClassLoaders.put(file.getAbsolutePath(), modClassLoader);

        return modClassLoader;
    }

    public void scan(String modFileId) {
        ModClassLoader loader = this.modClassLoaders.get(modFileId);
        Scanner.Result scanResult = loader.scan();
        this.scans.put(modFileId, scanResult);
    }

    public Scanner.Result scan() {
        Scanner scanner = new Scanner(gameFile, this);
        return this.scanResult = scanner.scanJar(LoadScreen.get());
    }

    public String getModFileId(File modFile) {
        return this.fileIdMap.get(modFile.getAbsolutePath());
    }

    public Class<?> getClass(String file, String name) throws ClassNotFoundException {
        ModClassLoader modClassLoader = this.modClassLoaders.get(file);
        return modClassLoader.findClass(name);
    }

    public Class<?> getClassOrNull(String file, String name) {
        ModClassLoader modClassLoader = this.modClassLoaders.get(file);
        try {
            return modClassLoader.findClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> findClass(String moduleName, String name) {
        return super.findClass(moduleName, name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    public void getModClassLoader(String modFileId) {
        this.modClassLoaders.get(fileMap.get(modFileId).getAbsolutePath());
    }

    public Scanner.Result getResult(String modFileId) {
        return scans.get(modFileId);
    }

    public Scanner.Result getScanResult() {
        return scanResult;
    }

    public File getGameFile() {
        return gameFile;
    }
}
