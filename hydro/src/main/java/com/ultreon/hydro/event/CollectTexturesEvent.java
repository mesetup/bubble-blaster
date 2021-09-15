package com.ultreon.hydro.event;

import com.ultreon.hydro.render.TextureCollection;

public class CollectTexturesEvent extends Event {
    private final TextureCollection textureCollection;

    public CollectTexturesEvent(TextureCollection textureCollection) {
        this.textureCollection = textureCollection;
    }

    public TextureCollection getTextureCollection() {
        return textureCollection;
    }
}
