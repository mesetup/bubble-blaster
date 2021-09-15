package com.ultreon.bubbles.render.gui;

import com.ultreon.hydro.render.Renderer;

import java.awt.*;

public class QViewport extends QContainer {
    private final Rectangle viewportRect;

    public QViewport(Rectangle viewportRect) {
        this.viewportRect = viewportRect;
        setBackgroundColor(new Color(64, 64, 64));
    }

    @Override
    public void renderComponents(Renderer ngg) {
        for (QComponent component : components) {
            Renderer componentGraphics = ngg.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            component.render(componentGraphics);
            componentGraphics.dispose();
        }
    }

    @Override
    public void render(Renderer ngg) {
        this.renderComponent(ngg);

        Renderer viewportGraphics = ngg.create(viewportRect.x, viewportRect.y, viewportRect.width, viewportRect.height);
        renderComponents(viewportGraphics);
    }

    @Override
    public void renderComponent(Renderer ngg) {
        ngg.color(getBackgroundColor());
        ngg.rect(0, 0, getSize().width, getSize().height);
    }

    @Override
    public void tick() {

    }

    public void setViewportSize(Dimension size) {
        this.viewportRect.setSize(size);
    }

    public Dimension getViewportSize() {
        return viewportRect.getSize();
    }

    public void setViewportLocation(Point location) {
        this.viewportRect.setLocation(location);
    }

    public Point getViewportLocation() {
        return viewportRect.getLocation();
    }
}
