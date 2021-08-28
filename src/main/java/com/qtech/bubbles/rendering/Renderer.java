package com.qtech.bubbles.rendering;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.ResourceEntry;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Renderer {
    private final Graphics2D gfx;
    private Texture texture;

    public Renderer() {
        AtomicReference<Graphics2D> ref = new AtomicReference<>();
        this.createRenderer(ref);
        this.gfx = ref.get();
    }

    public void bindTexture(ResourceEntry textureRes) {
        TextureManager textureManager = BubbleBlaster.getTextureManager();
        this.texture = textureManager.getTexture(textureRes);
    }

    public void color(int r, int g, int b) {
        if (this.texture instanceof PaintTexture texture) {
            texture.modify(new Color(r, g, b));
        } else {
            this.texture = new PaintTexture(new Color(r, g, b));
        }
    }

    public void draw(int xf, int yf, int xs, int ys) {
        this.texture.render(this.gfx, xf, yf, xs, ys);
    }

    protected abstract void createRenderer(AtomicReference<Graphics2D> reference);
}
