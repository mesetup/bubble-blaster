package com.qtech.bubbles.gui;

import com.qtech.bubbles.common.GraphicsProcessor;

import java.awt.*;

public class QViewport extends QContainer {
    private final Rectangle viewportRect;

    public QViewport(Rectangle viewportRect) {
        this.viewportRect = viewportRect;
        setBackgroundColor(new Color(64, 64, 64));
    }

    @Override
    public void renderComponents(GraphicsProcessor ngg) {
        for (QComponent component : components) {
            GraphicsProcessor componentGraphics = ngg.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            component.render(componentGraphics);
            componentGraphics.dispose();
        }
    }

    @Override
    public void render(GraphicsProcessor ngg) {
        this.renderComponent(ngg);

        GraphicsProcessor viewportGraphics = ngg.create(viewportRect.x, viewportRect.y, viewportRect.width, viewportRect.height);
        renderComponents(viewportGraphics);
    }

    @Override
    public void renderComponent(GraphicsProcessor ngg) {
        ngg.setColor(getBackgroundColor());
        ngg.fillRect(0, 0, getSize().width, getSize().height);
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
