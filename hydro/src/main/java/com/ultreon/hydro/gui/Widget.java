package com.ultreon.hydro.gui;

import com.ultreon.hydro.render.Renderer;

public abstract class Widget {

    public abstract void paint(Renderer g);

    public abstract void destroy();
}
