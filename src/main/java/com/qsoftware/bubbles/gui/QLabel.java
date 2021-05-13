package com.qsoftware.bubbles.gui;

import com.qsoftware.bubbles.common.GraphicsProcessor;

import java.awt.*;

public class QLabel extends QComponent {
    private String text;
    private boolean wrapped;

    protected Color foregroundColor;

    public QLabel(String text) {
        this.text = text;

        backgroundColor = new Color(0, 0, 0, 0);
        foregroundColor = Color.white;
    }

    @Override
    public void render(GraphicsProcessor ngg) {
        GraphicsProcessor ngg1 = ngg.create(getX(), getY(), getWidth(), getHeight());
        renderComponent(ngg1);
        ngg1.dispose();
    }

    @Override
    public void renderComponent(GraphicsProcessor ngg) {
        ngg.setColor(backgroundColor);
        ngg.fill(getBounds());

        ngg.setColor(foregroundColor);

        if (wrapped) ngg.drawWrappedString(text, 0, 0, getWidth());
        else ngg.drawMultiLineString(text, 0, 0);
    }

    @Override
    public void tick(GraphicsProcessor ngg) {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isWrapped() {
        return wrapped;
    }

    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
}
