package com.ultreon.bubbles.render.gui;

import com.ultreon.hydro.render.Renderer;

public class QPanel extends QContainer {
    @Override
    public void renderComponents(Renderer ngg) {
        components.forEach(c -> c.render(ngg));
    }

    @Override
    public void render(Renderer ngg) {
        Renderer ngg2 = ngg.create(getX(), getY(), getWidth(), getHeight());
        renderComponent(ngg2);
        ngg2.dispose();

        Renderer ngg3 = ngg.create(getX(), getY(), getWidth(), getHeight());
        renderComponents(ngg3);
        ngg3.dispose();
    }

    @Override
    public void renderComponent(Renderer ngg) {
        ngg.color(getBackgroundColor());
        ngg.fill(getBounds());
    }

    @Override
    public void tick() {

    }
}
