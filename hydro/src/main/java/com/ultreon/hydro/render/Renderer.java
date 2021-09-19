/////////////////////
//     Package     //
/////////////////////
package com.ultreon.hydro.render;

/////////////////////
//     Imports     //
/////////////////////
import com.ultreon.commons.util.StringUtils;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.vector.Vector4i;

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

/**
 * Renderer class.
 *
 * @author Qboi
 * @see Graphics
 * @see Graphics2D
 * @see Font
 * @see GraphicsConfiguration
 * @see FontRenderContext
 * @see FontMetrics
 * @see Color
 * @see Paint
 * @see Composite
 * @see Stroke
 * @see String
 * @see ImageObserver
 * @see RenderingHints
 * @see Shape
 * @see AffineTransform
 * @see AttributedCharacterIterator
 * @see GlyphVector
 * @see Polygon
 * @see Rectangle
 */
@SuppressWarnings("unused")
public class Renderer {
    ////////////////////
    //     Fields     //
    ////////////////////
    private final Graphics2D gg;
    private Font fallbackFont;

    //////////////////////////
    //     Constructors     //
    //////////////////////////
    public Renderer(Graphics gfx) {
        this.gg = (Graphics2D) gfx;
    }

    public Renderer(Graphics2D gfx2d) {
        this.gg = gfx2d;
    }

    public Renderer(Renderer renderer) {
        this.fallbackFont = renderer.fallbackFont;
        this.gg = renderer.gg;
    }

    ////////////////////////
    //     Properties     //
    ////////////////////////
    public void composite(Composite comp) {
        gg.setComposite(comp);
    }

    public void paint(Paint paint) {
        gg.setPaint(paint);
    }

    public void stroke(Stroke s) {
        gg.setStroke(s);
    }

    public void color(Color c) {
        gg.setColor(c);
    }

    public void clearColor(Color color) {
        gg.setBackground(color);
    }

    public void paintMode() {
        gg.setPaintMode();
    }

    public void xorMode(Color c1) {
        gg.setXORMode(c1);
    }

    public void hint(RenderingHints.Key hintKey, Object hintValue) {
        gg.setRenderingHint(hintKey, hintValue);
    }

    public void hints(Map<?, ?> hints) {
        gg.setRenderingHints(hints);
    }

    public void addHints(Map<?, ?> hints) {
        gg.addRenderingHints(hints);
    }

    ////////////////////
    //     Shapes     //
    ////////////////////
    public void outline(Shape s) {
        gg.draw(s);
    }

    public void fill(Shape s) {
        gg.fill(s);
    }

    public void fill(com.ultreon.hydro.screen.gui.Rectangle r) {
        gg.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public void fill(Vector4i r) {
        gg.fillRect(r.x, r.y, r.z, r.w);
    }

    public void line(int x1, int y1, int x2, int y2) {
        gg.drawLine(x1, y1, x2, y2);
    }

    public void rectLine(int x, int y, int width, int height) {
        gg.drawRect(x, y, width, height);
    }

    public void rect(int x, int y, int width, int height) {
        gg.fillRect(x, y, width, height);
    }

    public void roundRectLine(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void roundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gg.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void rect3DLine(int x, int y, int width, int height, boolean raised) {
        gg.draw3DRect(x, y, width, height, raised);
    }

    public void rect3D(int x, int y, int width, int height, boolean raised) {
        gg.fill3DRect(x, y, width, height, raised);
    }

    public void ovalLine(int x, int y, int width, int height) {
        gg.drawOval(x, y, width, height);
    }

    public void oval(int x, int y, int width, int height) {
        gg.fillOval(x, y, width, height);
    }

    public void arcLine(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void arc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gg.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void polyline(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolyline(xPoints, yPoints, nPoints);
    }

    public void polygonLine(int[] xPoints, int[] yPoints, int nPoints) {
        gg.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void polygonLine(Polygon p) {
        gg.drawPolygon(p);
    }

    public void polygon(int[] xPoints, int[] yPoints, int nPoints) {
        gg.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void polygon(Polygon p) {
        gg.fillPolygon(p);
    }

    ///////////////////
    //     Image     //
    ///////////////////
    public boolean image(Image img, int x, int y) {
        return gg.drawImage(img, x, y, Game.getInstance().getObserver());
    }

    public boolean image(Image img, int x, int y, int width, int height) {
        return gg.drawImage(img, x, y, width, height, Game.getInstance().getObserver());
    }

    public boolean image(Image img, int x, int y, Color backgroundColor) {
        return gg.drawImage(img, x, y, backgroundColor, Game.getInstance().getObserver());
    }

    public boolean image(Image img, int x, int y, int width, int height, Color backgroundColor) {
        return gg.drawImage(img, x, y, width, height, backgroundColor, Game.getInstance().getObserver());
    }

    public boolean image(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, Game.getInstance().getObserver());
    }

    public boolean image(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color backgroundColor) {
        return gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, backgroundColor, Game.getInstance().getObserver());
    }

    public boolean image(Image img, AffineTransform xForm) {
        return gg.drawImage(img, xForm, Game.getInstance().getObserver());
    }

    public void image(BufferedImage img, BufferedImageOp op, int x, int y) {
        gg.drawImage(img, op, x, y);
    }

    public void renderedImage(RenderedImage img, AffineTransform xForm) {
        gg.drawRenderedImage(img, xForm);
    }

    public void renderableImage(RenderableImage img, AffineTransform xForm) {
        gg.drawRenderableImage(img, xForm);
    }

    //////////////////
    //     Text     //
    //////////////////
    public void text(String str, int x, int y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    public void text(String str, float x, float y) {
        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    public void text(AttributedCharacterIterator iterator, int x, int y) {
        gg.drawString(iterator, x, y);
    }

    public void text(AttributedCharacterIterator iterator, float x, float y) {
        gg.drawString(iterator, x, y);
    }

    public void multiLineText(String str, int x, int y) {
        y -= gg.getFontMetrics().getHeight();

        for (String line : str.split("\n"))
            text(line, x, y += gg.getFontMetrics().getHeight());
    }

    public void wrappedText(String str, int x, int y, int maxWidth) {
        List<String> lines = StringUtils.wrap(str, getFontMetrics(getFont()), maxWidth);
        String joined = org.apache.commons.lang3.StringUtils.join(lines.toArray(new String[]{}), '\n');
        multiLineText(joined, x, y);
    }

    public void tabString(String str, int x, int y) {
        for (String line : str.split("\t"))
            text(line, x += gg.getFontMetrics().getHeight(), y);
    }

    public void chars(char[] data, int offset, int length, int x, int y) {
        gg.drawChars(data, offset, length, x, y);
    }

    public void bytes(byte[] data, int offset, int length, int x, int y) {
        gg.drawBytes(data, offset, length, x, y);
    }

    public void font(Font font) {
        gg.setFont(font);
    }

    public void fallbackFont(Font font) {
        fallbackFont = font;
    }

    public void clearRect(int x, int y, int width, int height) {
        gg.clearRect(x, y, width, height);
    }

    public void glyphVectorLine(GlyphVector g, float x, float y) {
        gg.drawGlyphVector(g, x, y);
    }

    ////////////////////////////
    //     Transformation     //
    ////////////////////////////
    public void translate(double tx, double ty) {
        gg.translate(tx, ty);
    }

    public void translate(int x, int y) {
        gg.translate(x, y);
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

    public void clip(Shape s) {
        gg.clip(s);
    }

    public void clipRect(int x, int y, int width, int height) {
        gg.clipRect(x, y, width, height);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        gg.copyArea(x, y, width, height, dx, dy);
    }

    /////////////////////
    //     Setters     //
    /////////////////////
    public void setTransform(AffineTransform Tx) {
        gg.setTransform(Tx);
    }

    public void setClip(int x, int y, int width, int height) {
        gg.setClip(x, y, width, height);
    }

    public void setClip(Shape clip) {
        gg.setClip(clip);
    }

    /////////////////////
    //     Getters     //
    /////////////////////
    public AffineTransform getTransform() {
        return gg.getTransform();
    }

    public Paint getPaint() {
        return gg.getPaint();
    }

    public Composite getComposite() {
        return gg.getComposite();
    }

    public Color getClearColor() {
        return gg.getBackground();
    }

    public Stroke getStroke() {
        return gg.getStroke();
    }

    public Color getColor() {
        return gg.getColor();
    }

    public Font getFallbackFont() {
        return fallbackFont;
    }

    public Font getFont() {
        return gg.getFont();
    }

    public FontMetrics getFontMetrics(Font f) {
        return gg.getFontMetrics(f);
    }

    public Shape getClip() {
        return gg.getClip();
    }

    public Rectangle getClipBounds() {
        return gg.getClipBounds();
    }

    public Rectangle getClipBounds(Rectangle r) {
        return gg.getClipBounds(r);
    }

    public FontMetrics getFontMetrics() {
        return gg.getFontMetrics();
    }

    public FontRenderContext getFontRenderContext() {
        return gg.getFontRenderContext();
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        return gg.getDeviceConfiguration();
    }

    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return gg.getRenderingHint(hintKey);
    }

    public RenderingHints getRenderingHints() {
        return gg.getRenderingHints();
    }

    ///////////////////////////
    //     Miscellaneous     //
    ///////////////////////////
    public Renderer create() {
        return new Renderer(gg.create());
    }

    public Renderer create(int x, int y, int width, int height) {
        return new Renderer(gg.create(x, y, width, height));
    }

    public boolean hitClip(int x, int y, int width, int height) {
        return gg.hitClip(x, y, width, height);
    }

    public void dispose() {
        gg.dispose();
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return gg.hit(rect, s, onStroke);
    }

    ///////////////////////
    //     To String     //
    ///////////////////////
    public String toString() {
        return gg.toString();
    }
}
