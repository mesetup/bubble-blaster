package com.qtech.bubbles.addon.loader;

import com.qtech.bubbles.common.maps.SequencedHashMap;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.common.mod.ModObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddonManager {
    private static final AddonManager INSTANCE = new AddonManager();
    private static final List<AddonContainer> CONTAINERS = new ArrayList<>();
    private static final List<String> ADDON_IDS = new ArrayList<>();

    private final SequencedHashMap<String, ModObject<? extends ModInstance>> addonObjects;
    private final SequencedHashMap<String, ModInstance> addons;

    private AddonManager() {
        this.addonObjects = new SequencedHashMap<>();
        this.addons = new SequencedHashMap<>();
    }

    public static AddonManager getInstance() {
        return INSTANCE;
    }

    @Nullable
    public AddonContainer getContainerFromId(String addonId) {
        for (AddonContainer container : CONTAINERS) {
            if (container.getAddonId().equals(addonId)) {
                return container;
            }
        }
        return null;
    }

    static void registerContainer(AddonContainer container) {
        if (ADDON_IDS.contains(container.getAddonId())) {
            throw new IllegalArgumentException("Addon id already used in other addon.");
        }
        ADDON_IDS.add(container.getAddonId());
        CONTAINERS.add(container);
    }

    public List<AddonContainer> getContainers() {
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
