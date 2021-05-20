package com.qtech.bubbleblaster.gui;

import com.google.common.annotations.Beta;
import com.qtech.bubbleblaster.common.GraphicsProcessor;

@Beta
public class QScrollPane extends QContainer {
    private QViewport viewport;

    @Override
    public void renderComponents(GraphicsProcessor ngg) {

    }

    @Override
    public void render(GraphicsProcessor ngg) {

    }

    @Override
    public void renderComponent(GraphicsProcessor ngg) {

    }

    @Override
    public void tick() {

    }

    public QViewport getViewport() {
        return viewport;
    }

    public void setViewport(QViewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public QComponent add(QComponent component) {
        this.viewport.components.add(component);
        return component;
    }

    @Override
    public QComponent remove(QComponent component) {
        this.viewport.components.remove(component);
        return component;
    }
}
