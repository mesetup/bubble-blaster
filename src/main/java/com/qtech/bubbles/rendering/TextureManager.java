package com.qtech.bubbles.rendering;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.addon.loader.AddonManager;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.resources.ResourceManager;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class TextureManager {
    private static final TextureManager instance = new TextureManager();
    private final Map<ResourceEntry, Texture> textureMap = new ConcurrentHashMap<>();

    public static TextureManager getInstance() {
        return instance;
    }

    private TextureManager() {

    }

    public Texture getTexture(ResourceEntry entry) {
        textureMap.get(entry);
        return textureMap.get(entry);
    }

    public Texture getOrLoadTexRes(ResourceEntry entry) {
        loadTexture(entry, new TextureSource() {
            @Override
            public Texture create() {
                return new ImageTexture() {
                    @Override
                    protected byte[] loadBytes() {
                        ResourceManager resourceManager = BubbleBlaster.getInstance().getResourceManager();
                        resourceManager.getResource(entry.namespace());
                        ModInstance addon = AddonManager.getInstance().getAddon(entry.namespace());
                        Objects.requireNonNull(addon).getClass().getResourceAsStream("assets/" + entry.namespace() + "/textures/" + entry.path());
                        return new byte[0];
                    }
                };
            }
        });
        textureMap.get(entry);
        return textureMap.get(entry);
    }

    public void loadTexture(ResourceEntry entry, TextureSource source) {
        Texture texture = source.create();
        textureMap.put(entry, texture);
    }
}
