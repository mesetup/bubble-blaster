package com.qsoftware.bubbles.registry;

import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.ResourceLocation;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bubble registry.
 *
 * @see AbstractRegistry
 * @see AbstractBubble
 * @see ResourceLocation
 * @deprecated use {@link Registry < Bubble >} instead.
 */
@Deprecated
public class BubbleRegistry extends AbstractRegistry<ResourceLocation, AbstractBubble> {
    protected static BubbleRegistry INSTANCE;

    public static BubbleRegistry instance() {
        return INSTANCE;
    }

    public HashMap<ResourceLocation, AbstractBubble> registry = new HashMap<>();

    public BubbleRegistry() {
        this.checkInstance(BubbleRegistry.INSTANCE);

        BubbleRegistry.INSTANCE = this;
    }

    @Override
    public AbstractBubble get(ResourceLocation obj) {
        return this.registry.get(obj);
    }

    public AbstractBubble get(String key) {
        return this.registry.get(ResourceLocation.fromString(key));
    }

    @Override
    public void register(ResourceLocation key, AbstractBubble val) {
        if (registry.containsKey(key)) throw new KeyAlreadyExistsException("Key '" + key + "' already exists!");

        this.registry.put(key, val);
    }

    @Override
    public Collection<AbstractBubble> values() {
        return registry.values();
    }

    @Override
    public Set<ResourceLocation> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<ResourceLocation, AbstractBubble>> entries() throws IllegalAccessException {
        return null;
    }
}
