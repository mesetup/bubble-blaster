package com.ultreon.hydro.screen.gui.view;

import com.ultreon.hydro.screen.gui.Widget;

import com.ultreon.hydro.render.Renderer;

public abstract class View extends Widget {
    public Renderer containerGraphics;

    public View(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
