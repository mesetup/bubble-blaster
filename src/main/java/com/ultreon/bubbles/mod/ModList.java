package com.ultreon.bubbles.mod;

import com.ultreon.bubbles.mod.loader.ModManager;

import java.util.List;

public class ModList {
    private static final ModList instance = new ModList();
    private final ModManager modManager = ModManager.instance();

    private ModList() {

    }

    /**
     * Receive mod list instance.
     *
     * @return the mod list instance.
     */
    public static ModList get() {
        return instance;
    }

    /**
     * Receive mod container by using an id.
     * This uses the {@link ModManager}.
     *
     * @param id the id of the mod.
     * @return the container of the mod.
     * @see ModManager
     */
    public ModContainer getContainerById(String id) {
        return modManager.getContainerFromId(id);
    }

    /**
     * Receive all mod containers that are registered in the mod manager.
     * This uses the {@link ModManager}.
     *
     * @return a list of mod containers.
     * @see ModManager
     */
    public List<ModContainer> getContainers() {
        return modManager.getContainers();
    }
}
