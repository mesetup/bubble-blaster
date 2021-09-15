package com.ultreon.bubbles.registry;

import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.hydro.registry.AbstractRegistry;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Bubble registry.
 *
 * @see AbstractRegistry
 * @see AbstractBubble
 * @see ResourceEntry
 */
public class AddonManager extends AbstractRegistry<String, ModObject<? extends ModInstance>> {
    protected static AddonManager INSTANCE;

    public static AddonManager instance() {
        return INSTANCE;
    }

    public AddonManager() {
        this.checkInstance(AddonManager.INSTANCE);

        AddonManager.INSTANCE = this;
    }

    @Override
    public ModObject<? extends ModInstance> get(String id) {
        return this.registry.get(id);
    }

    public ModInstance getAddon(String id) {
        return get(id).getAddon();
    }

    public ModContainer getContainer(String id) {
        return get(id).getContainer();
    }

    public void register(ModObject<?> object) {
        this.register(object.getNamespace(), object);
    }

    public void register(ModInstance addon) {
        this.register(addon.getAddonId(), addon.getAddonObject());
    }

    public void register(ModContainer container) {
        this.register(container.getModId(), container.getModObject());
    }

    @Override
    public void register(String id, ModObject<?> object) {
        if (registry.containsKey(id)) throw new KeyAlreadyExistsException("Key '" + id + "' already exists!");

        this.registry.put(id, object);
    }

    @Override
    public Collection<ModObject<?>> values() {
        return Collections.unmodifiableCollection(registry.values());
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    @Override
    public Set<Map.Entry<String, ModObject<?>>> entries() {
        return Collections.unmodifiableSet(registry.entrySet());
    }
}
