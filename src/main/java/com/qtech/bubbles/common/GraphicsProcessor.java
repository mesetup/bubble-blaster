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
public class GraphicsProcessor {
    private final Graphics2D gg;
    private Font fallbackFont;

    public GraphicsProcessor(Graphics2D g2d) {
        this.gg = g2d;
    }

    public GraphicsProcessor(Graphics g) {
        this((Graphics2D) g);
    }

    public void draw(Shape s) {
        gg.draw(s);
    }

    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return gg.drawImage(img, xform, obs);
    }

    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        gg.drawImage(img, op, x, y);
    }

    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        gg.drawRenderedImage(img, xform);
    }

    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        gg.drawRenderableImage(img, xform);
    }

    public void drawString(String str, int x, int y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    public void drawString(String str, float x, float y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        gg.drawString(iterator, x, y);
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return gg.drawImage(img, x, y, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return gg.drawImage(img, x, y, width, height, observer);
    }

    public boolean drawImage(Image img, int x, int y, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, x, y, backgroundColor, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, x, y, width, height, backgroundColor, observer);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color backgroundColor, ImageObserver observer) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, backgroundColor, observer);
    }

    public void dispose() {
        gg.dispose();
    }

    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        gg.drawString(iterator, x, y);
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
        gg.drawGlyphVector(g, x, y);
    }

    public void fill(Shape s) {
        gg.fill(s);
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return gg.hit(rect, s, onStroke);
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        return gg.getDeviceConfiguration();
    }

    public void setComposite(Composite comp) {
        gg.setComposite(comp);
    }

    public void setPaint(Paint paint) {
        gg.setPaint(paint);
    }

    public void setStroke(Stroke s) {
        gg.setStroke(s);
    }

    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        gg.setRenderingHint(hintKey, hintValue);
    }

    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return gg.getRenderingHint(hintKey);
    }

    public void setRenderingHints(Map<?, ?> hints) {
        gg.setRenderingHints(hints);
    }

    public void addRenderingHints(Map<?, ?> hints) {
        gg.addRenderingHints(hints);
    }

    public RenderingHints getRenderingHints() {
        return gg.getRenderingHints();
    }

    public GraphicsProcessor create() {
        return new GraphicsProcessor((Graphics2D) gg.create());
    }

    public void translate(int x, int y) {
        gg.translate(x, y);
    }

    public Color getColor() {
        return gg.getColor();
    }

    public void setColor(Color c) {
        gg.setColor(c);
    }

    public void setPaintMode() {
        gg.setPaintMode();
    }

    public void setXORMode(Color c1) {
        gg.setXORMode(c1);
    }

    public Font getFont() {
        return gg.getFont();
    }

    public void setFont(Font font) {
        gg.setFont(font);
    }

    public FontMetrics getFontMetrics(Font f) {
        return gg.getFontMetrics(f);
    }

    public Rectangle getClipBounds() {
        return gg.getClipBounds();
    }

    public void clipRect(int x, int y, int width, int height) {
        gg.clipRect(x, y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        gg.setClip(x, y, width, height);
    }

    public Shape getClip() {
        return gg.getClip();
    }

    public void setClip(Shape clip) {
        gg.setClip(clip);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        gg.copyArea(x, y, width, height, dx, dy);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        gg.drawLine(x1, y1, x2, y2);
    }

    public void fillRect(int x, int y, int width, int height) {
        gg.fillRect(x, y, width, height);
    }

    public void clearRect(int x, int y, int width, int height) {
        gg.clearRect(x, y, width, height);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawOval(int x, int y, int width, int height) {
        gg.drawOval(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height) {
        gg.fillOval(x, y, width, height);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolyline(xPoints, yPoints, nPoints);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gg.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void translate(double tx, double ty) {
        gg.translate(tx, ty);
    }

    public void rotate(double theta) {
        gg.rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        gg.rotate(theta, x, y);
    }

    public void scale(double sx, double sy) {
        gg.scale(sx, sy);
    }

    public void shear(double shx, double shy) {
        gg.shear(shx, shy);
    }

    public void transform(AffineTransform Tx) {
        gg.transform(Tx);
    }

    public void setTransform(AffineTransform Tx) {
        gg.setTransform(Tx);
    }

    public AffineTransform getTransform() {
        return gg.getTransform();
    }

    public Paint getPaint() {
        return gg.getPaint();
    }

    public Composite getComposite() {
        return gg.getComposite();
    }

    public void setBackground(Color color) {
        gg.setBackground(color);
    }

    public Color getBackground() {
        return gg.getBackground();
    }

    public Stroke getStroke() {
        return gg.getStroke();
    }

    public void clip(Shape s) {
        gg.clip(s);
    }

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

    public GraphicsProcessor create(int x, int y, int width, int height) {
        return new GraphicsProcessor((Graphics2D) gg.create(x, y, width, height));
    }
    
    public Graphics2D getWrapped() {
        return gg;
    }

    public void fillPolygon(Polygon polygon) {
        gg.fillPolygon(polygon);
    }

    public void drawPolygon(Polygon polygon) {
        gg.drawPolygon(polygon);
    }

    public void drawRect(int x, int y, int width, int height) {
        gg.drawRect(x, y, width, height);
    }
}
