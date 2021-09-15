package com.ultreon.hydro.render;

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
    public void render(Renderer gfx, int xf, int yf, int xs, int ys) {
        gfx.paint(paint);
        switch (mode) {
            case DRAW -> gfx.rectLine(xf, yf, xs - xf, ys - yf);
            case FILL -> gfx.rect(xf, yf, xs - xf, ys - yf);
        }
    }

    enum Mode {FILL, DRAW}
}
