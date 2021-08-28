package com.qtech.bubbles.rendering;

import java.awt.*;

public class PaintTexture extends Texture {
    private Paint paint;
    private final Mode mode = Mode.FILL;

    public void modify(Paint paint) {
        this.paint = paint;
    }

    public PaintTexture(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void render(Graphics2D gfx, int xf, int yf, int xs, int ys) {
        gfx.setPaint(paint);
        switch (mode) {
            case DRAW -> gfx.drawRect(xf, yf, xs - xf, ys - yf);
            case FILL -> gfx.fillRect(xf, yf, xs - xf, ys - yf);
        }
    }

    enum Mode {FILL, DRAW}
}
