package com.ultreon.hydro.render;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public abstract class ImageTexture extends Texture {
    BufferedImage image;

    protected abstract byte[] loadBytes();

    @SneakyThrows
    public void load() {
        byte[] bytes = loadBytes();
        this.image = ImageIO.read(new ByteArrayInputStream(bytes));
    }

    @Override
    public void render(Renderer gfx, int xf, int yf, int xs, int ys) {
        gfx.image(image, xf, yf, xs - xf, ys - yf);
    }
}
