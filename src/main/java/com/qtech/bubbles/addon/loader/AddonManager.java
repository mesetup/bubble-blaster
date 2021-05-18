package com.qtech.bubbles.addon.loader;

import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.common.maps.SequencedHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddonManager {
    private static final AddonManager INSTANCE = new AddonManager();
    private static final List<AddonContainer> CONTAINERS = new ArrayList<>();
    private static final List<String> ADDON_IDS = new ArrayList<>();

    private final SequencedHashMap<String, AddonObject<? extends QBubblesAddon>> addonObjects;
    private final SequencedHashMap<String, QBubblesAddon> addons;

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

    public AddonObject<? extends QBubblesAddon> getAddonObject(String id) {
        if (!addonObjects.containsKey(id)) {
            return null;
        }

        return addonObjects.get(id);
    }

    @Nullable
    public QBubblesAddon getAddon(String id) {
        if (!addons.containsKey(id)) {
            return null;
        }

        return addons.get(id);
    }

    public void registerAddonObject(AddonObject<? extends QBubblesAddon> addonObject) {
        this.addonObjects.put(addonObject.getNamespace(), addonObject);
    }

    public void registerAddon(QBubblesAddon addon) {
        this.addons.put(addon.getAddonId(), addon);
    }

    public Collection<AddonObject<? extends QBubblesAddon>> getAddonObjects() {
        return addonObjects.values();
    }

    public Collection<QBubblesAddon> getAddons() {
        return addons.values();
    }
}
