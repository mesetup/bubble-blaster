package com.ultreon.bubbles.mod.loader;

import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.commons.map.SequencedHashMap;
import com.ultreon.hydro.core.AntiMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@AntiMod
public class ModManager {
    private static final ModManager instance = new ModManager();

    private static final List<ModContainer> containers = new ArrayList<>();
    private static final List<String> modIds = new ArrayList<>();

    private static final SequencedHashMap<String, ModObject<? extends ModInstance>> modObjects = new SequencedHashMap<>();
    private static final SequencedHashMap<String, ModInstance> mods = new SequencedHashMap<>();

    private ModManager() {

    }

    public static ModManager instance() {
        return instance;
    }

    @Nullable
    public ModContainer getContainerFromId(String modId) {
        for (ModContainer container : containers) {
            if (container.getModId().equals(modId)) {
                return container;
            }
        }
        return null;
    }

    static void registerContainer(ModContainer container) {
        if (modIds.contains(container.getModId())) {
            throw new IllegalArgumentException("Mod id already used in other mod.");
        }
        modIds.add(container.getModId());
        containers.add(container);
    }

    public List<ModContainer> getContainers() {
        return containers;
    }

    public ModObject<? extends ModInstance> getModObject(String id) {
        if (!modObjects.containsKey(id)) {
            return null;
        }

        return modObjects.get(id);
    }

    @Nullable
    public ModInstance getMod(String id) {
        if (!mods.containsKey(id)) {
            return null;
        }

        return mods.get(id);
    }

    public void registerModObject(ModObject<? extends ModInstance> modObject) {
        modObjects.put(modObject.getNamespace(), modObject);
    }

    public void registerMod(ModInstance mod) {
        mods.put(mod.getModId(), mod);
    }

    public Collection<ModObject<? extends ModInstance>> getModObjects() {
        return modObjects.values();
    }

    public Collection<ModInstance> getMods() {
        return mods.values();
    }

    public List<String> getModIds() {
        return modIds;
    }
}
