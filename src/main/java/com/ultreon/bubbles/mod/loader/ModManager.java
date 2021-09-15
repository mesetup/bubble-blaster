package com.ultreon.bubbles.mod.loader;

import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.commons.map.SequencedHashMap;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModManager {
    private static final ModManager INSTANCE = new ModManager();
    private static final List<ModContainer> CONTAINERS = new ArrayList<>();
    private static final List<String> ADDON_IDS = new ArrayList<>();

    private final SequencedHashMap<String, ModObject<? extends ModInstance>> addonObjects;
    private final SequencedHashMap<String, ModInstance> addons;

    private ModManager() {
        this.addonObjects = new SequencedHashMap<>();
        this.addons = new SequencedHashMap<>();
    }

    public static ModManager getInstance() {
        return INSTANCE;
    }

    @Nullable
    public ModContainer getContainerFromId(String addonId) {
        for (ModContainer container : CONTAINERS) {
            if (container.getModId().equals(addonId)) {
                return container;
            }
        }
        return null;
    }

    static void registerContainer(ModContainer container) {
        if (ADDON_IDS.contains(container.getModId())) {
            throw new IllegalArgumentException("Addon id already used in other addon.");
        }
        ADDON_IDS.add(container.getModId());
        CONTAINERS.add(container);
    }

    public List<ModContainer> getContainers() {
        return CONTAINERS;
    }

    public ModObject<? extends ModInstance> getAddonObject(String id) {
        if (!addonObjects.containsKey(id)) {
            return null;
        }

        return addonObjects.get(id);
    }

    @Nullable
    public ModInstance getAddon(String id) {
        if (!addons.containsKey(id)) {
            return null;
        }

        return addons.get(id);
    }

    public void registerAddonObject(ModObject<? extends ModInstance> modObject) {
        this.addonObjects.put(modObject.getNamespace(), modObject);
    }

    public void registerAddon(ModInstance addon) {
        this.addons.put(addon.getAddonId(), addon);
    }

    public Collection<ModObject<? extends ModInstance>> getAddonObjects() {
        return addonObjects.values();
    }

    public Collection<ModInstance> getAddons() {
        return addons.values();
    }
}
