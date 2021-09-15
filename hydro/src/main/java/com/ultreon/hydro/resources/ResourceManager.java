package com.ultreon.hydro.resources;

import com.ultreon.commons.exceptions.DuplicateElementException;
import com.ultreon.hydro.common.ResourceEntry;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ResourceManager {
    private final Map<File, PathMapping<byte[]>> mapping = new ConcurrentHashMap<>();
    private final Map<ResourceEntry, byte[]> assets = new ConcurrentHashMap<>();

    public byte[] getResource(String path) {
        List<byte[]> collect = mapping.values().stream().map((mapping) -> mapping.get(path)).collect(Collectors.toList());
        if (collect.size() == 0) {
            return null;
        }
        return collect.get(collect.size() - 1);
    }

    public InputStream getResourceAsStream(String path) {
        return new ByteArrayInputStream(getResource(path));
    }

    public void loadResources(File file, JarFile jarFile) {
        Enumeration<JarEntry> entries = jarFile.entries();

        if (mapping.containsKey(file)) {
            throw new DuplicateElementException("File resources already loaded: " + file);
        }

        mapping.put(file, new PathMapping<>());

        @SuppressWarnings("NullableProblems")
        Iterable<JarEntry> jarEntries = entries::asIterator;

        List<Exception> exceptions = new ArrayList<>();
        for (JarEntry entry : jarEntries) {
            byte[] bytes;
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(jarFile.getInputStream(entry));
                bytes = bufferedInputStream.readAllBytes();
            } catch (IOException e) {
                exceptions.add(e);
                continue;
            }

            Pattern compile = Pattern.compile("(?:/|)(assets)/([a-z]*)/([a-zA-Z0-9_/]+)");
            Matcher matcher = compile.matcher(entry.getName());
            String type = matcher.group(1);
            String namespace = matcher.group(2);
            String path = matcher.group(3);

            if (Objects.equals(type, "assets")) {
                ResourceEntry res = new ResourceEntry(namespace, path);
                assets.put(res, bytes);
            }

            mapping.get(file).map(entry.getName(), bytes);
        }

        if (!exceptions.isEmpty()) {
            RuntimeException e = new RuntimeException("Exception" + (exceptions.size() == 1 ? "" : "s") +
                    " while reading jar-file (see suppression" + (exceptions.size() == 1 ? "" : "s") + ": " + file.getAbsolutePath());
            for (Exception e1 : exceptions) {
                e.addSuppressed(e1);
            }

            throw e;
        }
    }

    public byte[] getAsset(ResourceEntry resourceEntry) {
        return assets.get(resourceEntry);
    }

    public InputStream getAssetAsStream(ResourceEntry resourceEntry) {
        return new ByteArrayInputStream(getAsset(resourceEntry));
    }

    private static class PathMapping<T> {
        private final Map<String, T> mapping = new ConcurrentHashMap<>();

        public PathMapping() {

        }

        public T get(String path) {
            return mapping.get(path);
        }

        public void map(String path, T type) {
            mapping.put(path, type);
        }

        public List<Map.Entry<String, T>> entries() {
            return new ArrayList<>(mapping.entrySet());
        }
    }
}
