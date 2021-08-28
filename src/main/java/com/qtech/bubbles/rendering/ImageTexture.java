package com.qtech.bubbles.rendering;

import com.qtech.bubbles.BubbleBlaster;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public abstract class ImageTexture extends Texture {
    BufferedImage image;

    protected abstract byte[] loadBytes();

    @SneakyThrows
    public void load() {
        byte[] bytes = loadBytes();

        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(bytes));
        this.image = bufImage;
    }

    @Override
    public void render(Graphics2D gfx, int xf, int yf, int xs, int ys) {
        gfx.drawImage(image, xf, yf, xs - xf, ys - yf, BubbleBlaster.getInstance());
    }
}
