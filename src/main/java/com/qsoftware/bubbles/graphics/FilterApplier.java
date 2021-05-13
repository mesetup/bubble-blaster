package com.qsoftware.bubbles.graphics;

import com.jhlabs.image.*;
import org.jdesktop.swingx.image.AbstractFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class FilterApplier {
    private final Graphics2D graphics;
    private final BufferedImage buffer;
    private final ImageObserver observer;
    private ArrayList<Object> filters;

    public FilterApplier(Dimension size, ImageObserver observer) {
        size.width = Math.max(size.width, 1);
        size.height = Math.max(size.height, 1);

        BufferedImage buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();

        this.observer = observer;
        this.graphics = g2d;
        this.buffer = buffer;
    }

    public Graphics2D getGraphics() {
        return this.graphics;
    }

    public Rectangle getBounds() {
        for (Object filter : filters) {
            if (filter instanceof GaussianFilter) {
                float radius = ((GaussianFilter) filter).getRadius();
                return new Rectangle(
                        (int) -Math.round(Math.ceil(radius)), (int) -Math.round(Math.ceil(radius)),
                        (int) Math.round(Math.ceil(buffer.getWidth() + radius * 2)), (int) Math.round(Math.ceil(buffer.getHeight() + radius * 2)));
            }
        }
        return new Rectangle(0, 0, buffer.getWidth(), buffer.getHeight());
    }

    public Rectangle getBounds(int x, int y, int w, int h) {
        return new Rectangle(x, y, w, h);
    }

    public Rectangle getBounds(Rectangle rect) {
        return getBounds(rect.x, rect.y, rect.width, rect.height);
    }

    public void setFilters(ArrayList<Object> filters) {
        this.filters = filters;
    }

    public BufferedImage done() {
        Rectangle bounds = getBounds();

        BufferedImage outputBuffer = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        outputBuffer.createGraphics().drawImage(buffer, 0, 0, observer);

        for (Object filter : filters) {
            if (filter instanceof ConvolveFilter) {
                ((ConvolveFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof PointFilter) {
                ((PointFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof TransformFilter) {
                ((TransformFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof BinaryFilter) {
                ((BinaryFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof WholeImageFilter) {
                ((WholeImageFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof ShadowFilter) {
                ((ShadowFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof ShatterFilter) {
                ((ShatterFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof BoxBlurFilter) {
                ((BoxBlurFilter) filter).filter(outputBuffer, outputBuffer);
            } else if (filter instanceof AbstractFilter) {
                ((AbstractFilter) filter).filter(outputBuffer, outputBuffer);
            } else {
                throw new NullPointerException();
            }
        }
        return outputBuffer;
    }
}
