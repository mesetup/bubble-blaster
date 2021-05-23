package com.qtech.bubbles.graphics;

import java.awt.*;

public interface ITexture {
    void render(Graphics2D gg);

    int width();

    int height();
}
