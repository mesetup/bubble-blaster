package com.qtech.bubbles.resources;

import com.qtech.bubbles.common.exceptions.DuplicateElementException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ResourceManager {
    private final Map<File, PathMapping<byte[]>> mapping = new ConcurrentHashMap<>();

    public byte[] getResource(String path) {
        List<byte[]> collect = mapping.values().stream().map((mapping) -> mapping.get(path)).collect(Collectors.toList());
        if (collect.size() == 0) {
            return null;
        }
        return collect.get(collect.size() - 1);
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
