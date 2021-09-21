package com.ultreon.hydro.render;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.resources.ResourceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TextureManager {
    private static final TextureManager instance = new TextureManager();
    private final Map<ResourceEntry, Texture> textureMap = new ConcurrentHashMap<>();

    public static TextureManager instance() {
        return instance;
    }

    private TextureManager() {

    }

    public Texture getTexture(ResourceEntry entry) {
        textureMap.get(entry);
        return textureMap.get(entry);
    }

    public Texture getOrLoadTexture(ResourceEntry entry) {
        if (textureMap.containsKey(entry)) {
            return textureMap.get(entry);
        }

        return loadTexture(entry, new TextureSource() {
            @Override
            public Texture create() {
                return new ImageTexture() {
                    @Override
                    protected byte[] loadBytes() {
                        ResourceManager resourceManager = Game.getInstance().getResourceManager();
                        return resourceManager.getResource(entry.namespace());
                    }
                };
            }
        });
    }

    public Texture loadTexture(ResourceEntry entry, TextureSource source) {
        Texture texture = source.create();
        textureMap.put(entry, texture);
        return texture;
    }
}
