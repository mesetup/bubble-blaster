package com.qtech.bubbles.gui;

import com.qtech.bubbles.common.GraphicsProcessor;

public class QPanel extends QContainer {
    @Override
    public void renderComponents(GraphicsProcessor ngg) {
        components.forEach(c -> c.render(ngg));
    }

    @Override
    public void render(GraphicsProcessor ngg) {
        GraphicsProcessor ngg2 = ngg.create(getX(), getY(), getWidth(), getHeight());
        renderComponent(ngg2);
        ngg2.dispose();

        GraphicsProcessor ngg3 = ngg.create(getX(), getY(), getWidth(), getHeight());
        renderComponents(ngg3);
        ngg3.dispose();
    }

    @Override
    public void renderComponent(GraphicsProcessor ngg) {
        ngg.setColor(getBackgroundColor());
        ngg.fill(getBounds());
    }

    @Override
    public void tick() {

    }
}
