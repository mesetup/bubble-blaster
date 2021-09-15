package com.ultreon.bubbles.mod;

import com.ultreon.bubbles.mod.loader.ModManager;

import java.util.List;

public class ModList {
    private static final ModList instance = new ModList();
    private final ModManager modManager = ModManager.getInstance();

    private ModList() {

    }

    public static ModList get() {
        return instance;
    }

    public ModContainer getContainerById(String id) {
        return modManager.getContainerFromId(id);
    }

    public List<ModContainer> getContainers() {
        return modManager.getContainers();
    }
}
