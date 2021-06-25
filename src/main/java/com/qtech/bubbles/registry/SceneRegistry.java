package com.qtech.bubbles.registry;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.scene.Scene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Scene registry.
 *
 * @see AbstractRegistry
 * @see Scene
 * @see ResourceLocation
 * @deprecated use {@link Registry <Scene>} instead.
 */
@Deprecated
public class SceneRegistry extends AbstractRegistry<ResourceLocation, Scene> {
    protected static SceneRegistry INSTANCE;

    public static SceneRegistry instance() {
        return INSTANCE;
    }

    public final HashMap<ResourceLocation, Scene> registry = new HashMap<>();

    public SceneRegistry() {
        this.checkInstance(SceneRegistry.INSTANCE);

        SceneRegistry.INSTANCE = this;
    }

    @Override
    public Scene get(ResourceLocation obj) {
        return this.registry.get(obj);
    }

    public Scene get(String key) {
        return this.registry.get(ResourceLocation.fromString(key));
    }

    @Override
    public void register(ResourceLocation key, Scene val) {
        this.registry.put(key, val);
    }

    @Override
    public Collection<Scene> values() {
        return registry.values();
    }

    @Override
    public Set<ResourceLocation> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<ResourceLocation, Scene>> entries() {
        return null;
    }
}
