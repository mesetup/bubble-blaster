package com.qsoftware.bubbles.registry;

import com.qsoftware.bubbles.addon.loader.AddonContainer;
import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.addon.AddonObject;
import com.qsoftware.bubbles.common.addon.QBubblesAddon;

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
public class AddonRegistry extends AbstractRegistry<String, AddonObject<? extends QBubblesAddon>> {
    protected static AddonRegistry INSTANCE;

    public static AddonRegistry instance() {
        return INSTANCE;
    }

    public AddonRegistry() {
        this.checkInstance(AddonRegistry.INSTANCE);

        AddonRegistry.INSTANCE = this;
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
    public Set<Map.Entry<String, AddonObject<?>>> entries() throws IllegalAccessException {
        return Collections.unmodifiableSet(registry.entrySet());
    }
}
