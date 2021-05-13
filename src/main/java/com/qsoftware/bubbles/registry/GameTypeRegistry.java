package com.qsoftware.bubbles.registry;

import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Game-type registry.
 *
 * @see AbstractRegistry
 * @see AbstractBubble
 * @see ResourceLocation
 * @deprecated use {@link Registry <GameType>} instead.
 */
@Deprecated
public class GameTypeRegistry extends AbstractRegistry<ResourceLocation, AbstractGameType> {
    protected static GameTypeRegistry INSTANCE;

    public static GameTypeRegistry instance() {
        return INSTANCE;
    }

    public HashMap<ResourceLocation, AbstractGameType> registry = new HashMap<>();

    public GameTypeRegistry() {
        this.checkInstance(GameTypeRegistry.INSTANCE);

        GameTypeRegistry.INSTANCE = this;
    }

    @Override
    public AbstractGameType get(ResourceLocation obj) {
        return this.registry.get(obj);
    }

    public AbstractGameType get(String key) {
        return this.registry.get(ResourceLocation.fromString(key));
    }

    @Override
    public void register(ResourceLocation key, AbstractGameType val) {
        this.registry.put(key, val);
    }

    @Override
    public Collection<AbstractGameType> values() {
        return registry.values();
    }

    @Override
    public Set<ResourceLocation> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<ResourceLocation, AbstractGameType>> entries() throws IllegalAccessException {
        return null;
    }
}
