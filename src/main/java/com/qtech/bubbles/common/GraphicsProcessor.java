package com.qtech.bubbles.common;

import com.qtech.bubbles.core.utils.categories.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class GraphicsProcessor extends Graphics2D {
    private final Graphics2D gg;
    private Font fallbackFont;

    public GraphicsProcessor(Graphics2D g2d) {
        this.gg = g2d;
    }

    @Override
    public void draw(Shape s) {
        gg.draw(s);
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return gg.drawImage(img, xform, obs);
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        gg.drawImage(img, op, x, y);
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        gg.drawRenderedImage(img, xform);
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        gg.drawRenderableImage(img, xform);
    }

    @Override
    public void drawString(String str, int x, int y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    @Override
    public void drawString(String str, float x, float y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        gg.drawString(iterator, x, y);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return gg.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return gg.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, x, y, backgroundColor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, x, y, width, height, backgroundColor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, backgroundColor, observer);
    }

    @Override
    public void dispose() {
        gg.dispose();
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        gg.drawString(iterator, x, y);
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        gg.drawGlyphVector(g, x, y);
    }

    @Override
    public void fill(Shape s) {
        gg.fill(s);
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return gg.hit(rect, s, onStroke);
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return gg.getDeviceConfiguration();
    }

    @Override
    public void setComposite(Composite comp) {
        gg.setComposite(comp);
    }

    @Override
    public void setPaint(Paint paint) {
        gg.setPaint(paint);
    }

    @Override
    public void setStroke(Stroke s) {
        gg.setStroke(s);
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        gg.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return gg.getRenderingHint(hintKey);
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        gg.setRenderingHints(hints);
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        gg.addRenderingHints(hints);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return gg.getRenderingHints();
    }

    @Override
    public Graphics create() {
        return gg.create();
    }

    @Override
    public void translate(int x, int y) {
        gg.translate(x, y);
    }

    @Override
    public Color getColor() {
        return gg.getColor();
    }

    @Override
    public void setColor(Color c) {
        gg.setColor(c);
    }

    @Override
    public void setPaintMode() {
        gg.setPaintMode();
    }

    @Override
    public void setXORMode(Color c1) {
        gg.setXORMode(c1);
    }

    @Override
    public Font getFont() {
        return gg.getFont();
    }

    @Override
    public void setFont(Font font) {
        gg.setFont(font);
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return gg.getFontMetrics(f);
    }

    @Override
    public Rectangle getClipBounds() {
        return gg.getClipBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        gg.clipRect(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        gg.setClip(x, y, width, height);
    }

    @Override
    public Shape getClip() {
        return gg.getClip();
    }

    @Override
    public void setClip(Shape clip) {
        gg.setClip(clip);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        gg.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        gg.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        gg.fillRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        gg.clearRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        gg.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        gg.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gg.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void translate(double tx, double ty) {
        gg.translate(tx, ty);
    }

    @Override
    public void rotate(double theta) {
        gg.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        gg.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        gg.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy) {
        gg.shear(shx, shy);
    }

    @Override
    public void transform(AffineTransform Tx) {
        gg.transform(Tx);
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        gg.setTransform(Tx);
    }

    @Override
    public AffineTransform getTransform() {
        return gg.getTransform();
    }

    @Override
    public Paint getPaint() {
        return gg.getPaint();
    }

    @Override
    public Composite getComposite() {
        return gg.getComposite();
    }

    @Override
    public void setBackground(Color color) {
        gg.setBackground(color);
    }

    @Override
    public Color getBackground() {
        return gg.getBackground();
    }

    @Override
    public Stroke getStroke() {
        return gg.getStroke();
    }

    @Override
    public void clip(Shape s) {
        gg.clip(s);
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return gg.getFontRenderContext();
    }

    public void setFallbackFont(Font font) {
        fallbackFont = font;
    }

    public Font getFallbackFont() {
        return fallbackFont;
    }

    public void drawMultiLineString(String str, int x, int y) {
        y -= gg.getFontMetrics().getHeight();

        for (String line : str.split("\n"))
            drawString(line, x, y += gg.getFontMetrics().getHeight());
    }

    public void drawWrappedString(String str, int x, int y, int maxWidth) {
        List<String> lines = StringUtils.wrap(str, getFontMetrics(getFont()), maxWidth);
        String joined = org.apache.commons.lang3.StringUtils.join(lines.toArray(new String[]{}), '\n');
//        System.out.println(joined.charAt(0));
        drawMultiLineString(joined, x, y);
    }

    public void drawTabString(String str, int x, int y) {
        for (String line : str.split("\t"))
            drawString(line, x += gg.getFontMetrics().getHeight(), y);
    }

    @Override
    public GraphicsProcessor create(int x, int y, int width, int height) {
        return new GraphicsProcessor((Graphics2D) super.create(x, y, width, height));
    }
}
