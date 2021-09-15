package com.ultreon.hydro.render;

import java.awt.*;

/**
 * @author Qboi
 * @since 1.0.141
 */
public class ColorTexture extends PaintTexture {
    public ColorTexture(Color color) {
        super(color);
    }

    public ColorTexture(int r, int g, int b) {
        super(new Color(r, g, b));
    }

    public ColorTexture(float r, float g, float b) {
        super(new Color(r, g, b));
    }

    public ColorTexture(int r, int g, int b, int a) {
        super(new Color(r, g, b, a));
    }

    public ColorTexture(float r, float g, float b, float a) {
        super(new Color(r, g, b, a));
    }

    public ColorTexture(int rgb) {
        super(new Color(rgb));
    }

    public ColorTexture(int rgba, boolean includeAlpha) {
        super(new Color(rgba, includeAlpha));
    }

    /**
     * Get a color texture based off a HSB value.
     *
     * @param h the hue
     * @param s the saturation
     * @param b the brightness.
     * @return the color texture created from the HSB value.
     */
    public static ColorTexture fromHSB(float h, float s, float b) {
        return new ColorTexture(Color.getHSBColor(h, s, b));
    }
}
