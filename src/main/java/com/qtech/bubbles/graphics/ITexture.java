package com.qtech.bubbles.graphics;

import com.qtech.bubbles.common.GraphicsProcessor;

import java.awt.*;

public interface ITexture {
    void render(GraphicsProcessor gg);

    int width();

    int height();
}
