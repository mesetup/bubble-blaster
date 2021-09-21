package com.ultreon.bubbles.registry;

import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.registry.AbstractRegistry;

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
@Deprecated
public class ModManager extends AbstractRegistry<String, ModObject<? extends ModInstance>> {
    protected static ModManager INSTANCE;

    public static ModManager instance() {
        return INSTANCE;
    }

    public ModManager() {
        this.checkInstance(ModManager.INSTANCE);

        ModManager.INSTANCE = this;
    }

    @Override
    public ModObject<? extends ModInstance> get(String id) {
        return this.registry.get(id);
    }

    public ModInstance getMod(String id) {
        return get(id).getMod();
    }

    public ModContainer getContainer(String id) {
        return get(id).getContainer();
    }

    public void register(ModObject<?> object) {
        this.register(object.getNamespace(), object);
    }

    public void register(ModInstance mod) {
        this.register(mod.getModId(), mod.getModObject());
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
