package com.qtech.bubbles.registry;

import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;

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
 * @see ResourceLocation
 */
public class AddonManager extends AbstractRegistry<String, AddonObject<? extends QBubblesAddon>> {
    protected static AddonManager INSTANCE;

    public static AddonManager instance() {
        return INSTANCE;
    }

    public AddonManager() {
        this.checkInstance(AddonManager.INSTANCE);

        AddonManager.INSTANCE = this;
    }

    @Override
    public AddonObject<? extends QBubblesAddon> get(String id) {
        return this.registry.get(id);
    }

    public QBubblesAddon getAddon(String id) {
        return get(id).getAddon();
    }

    public AddonContainer getContainer(String id) {
        return get(id).getContainer();
    }

    public void register(AddonObject<?> object) {
        this.register(object.getNamespace(), object);
    }

    public void register(QBubblesAddon addon) {
        this.register(addon.getAddonId(), addon.getAddonObject());
    }

    public void register(AddonContainer container) {
        this.register(container.getAddonId(), container.getAddonObject());
    }

    @Override
    public void register(String id, AddonObject<?> object) {
        if (registry.containsKey(id)) throw new KeyAlreadyExistsException("Key '" + id + "' already exists!");

        this.registry.put(id, object);
    }

    @Override
    public Collection<AddonObject<?>> values() {
        return Collections.unmodifiableCollection(registry.values());
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    @Override
    public Set<Map.Entry<String, AddonObject<?>>> entries() {
        return Collections.unmodifiableSet(registry.entrySet());
    }
}
