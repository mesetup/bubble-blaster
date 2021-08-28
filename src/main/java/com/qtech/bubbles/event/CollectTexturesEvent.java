package com.qtech.bubbles.event;

import com.qtech.bubbles.graphics.TextureCollection;

public class CollectTexturesEvent extends Event {
    private final TextureCollection textureCollection;

    public CollectTexturesEvent(TextureCollection textureCollection) {
        this.textureCollection = textureCollection;
    }

    public TextureCollection getTextureCollection() {
        return textureCollection;
    }
}
