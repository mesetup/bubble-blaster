package com.qtech.bubbles.event;

import com.qtech.bubbles.graphics.TextureCollection;

public class TextureRenderEvent extends Event {
    private final TextureCollection textureCollection;

    public TextureRenderEvent(TextureCollection textureCollection) {
        this.textureCollection = textureCollection;
    }

    public TextureCollection getTextureCollection() {
        return textureCollection;
    }
}
