package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.graphics.TextureCollection;

public class TextureRenderEvent extends Event {
    private final TextureCollection textureCollection;

    public TextureRenderEvent(TextureCollection textureCollection) {
        this.textureCollection = textureCollection;
    }

    public TextureCollection getTextureCollection() {
        return textureCollection;
    }
}
