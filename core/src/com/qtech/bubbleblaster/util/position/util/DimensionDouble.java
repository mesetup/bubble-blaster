package com.qtech.bubbleblaster.util.position.util;


public class DimensionDouble extends Dimension2D {
    private double height;
    private double width;

    public DimensionDouble(double width, double height) {
        this.height = height;
        this.width = width;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
