package com.ultreon.hydro.graphics;

import com.ultreon.hydro.render.Renderer;

public interface ITexture {
    void render(Renderer gg);

    int width();

    int height();
}
