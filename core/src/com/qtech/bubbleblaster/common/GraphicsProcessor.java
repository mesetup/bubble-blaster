package com.qtech.bubbleblaster.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.common.annotations.Beta;
import com.qtech.bubbleblaster.core.utils.categories.StringUtils;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.text.AttributedCharacterIterator;
import java.util.List;

@SuppressWarnings("unused")
public class GraphicsProcessor {
    private final SpriteBatch batch;
    private final ShapeDrawer shapeDrawer;
    private BitmapFont fallbackFont;
    private BitmapFont font;
    private Color background;

    public GraphicsProcessor(SpriteBatch batch, ShapeDrawer shapeDrawer) {
        this.batch = batch;
        this.shapeDrawer = shapeDrawer;
    }

//    public void draw(Shape s) {
//        gg.draw(s);
//    }

    public void drawImage(Texture tex, int x, int y) {
        batch.begin();
        batch.draw(tex, 0, 0);
        batch.end();
    }

    @Beta
    public void drawString(String str, int x, int y) {
//        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    @Beta
    public void drawString(String str, float x, float y) {
//        gg.drawString(StringUtils.createFallbackString(str, getFont()).getIterator(), x, y);
    }

    @Beta
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
//        gg.drawString(iterator, x, y);
    }

    public void dispose() {
        batch.dispose();
    }

    public void translate(int x, int y) {
        batch.getTransformMatrix().translate(x, y, 0);
    }

    public void translate(int x, int y, int z) {
        batch.getTransformMatrix().translate(x, y, z);
    }

    public Color getColor() {
        return batch.getColor();
    }

    public void setColor(Color color) {
        batch.setColor(color);
    }

    @Beta
    public BitmapFont getFont() {
        return font;
    }

    @Beta
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        shapeDrawer.line(x1, y1, x2, y2);
    }

    public void fillRect(float x, float y, float width, float height) {
        shapeDrawer.filledRectangle(x, y, width, height);
    }

    public void fillRect(float x, float y, float width, float height, Color color) {
        shapeDrawer.filledRectangle(x, y, width, height, color);
    }

    public void drawRect(float x, float y, float width, float height) {
        shapeDrawer.rectangle(x, y, width, height);
    }

    public void drawRect(float x, float y, float width, float height, Color color) {
        shapeDrawer.rectangle(x, y, width, height, color);
    }

    public void drawRect(float x, float y, float width, float height, Color color, float lineWidth) {
        shapeDrawer.rectangle(x, y, width, height, color, lineWidth);
    }

    public void drawRect(float x, float y, float width, float height, float lineWidth) {
        shapeDrawer.rectangle(x, y, width, height, lineWidth);
    }

    public void drawRect(float x, float y, float width, float height, float lineWidth, JoinType joinType) {
        shapeDrawer.rectangle(x, y, width, height, lineWidth, joinType);
    }

    public void drawRect(float x, float y, float width, float height, float lineWidth, float rotation, JoinType joinType) {
        shapeDrawer.rectangle(x, y, width, height, lineWidth, rotation, joinType);
    }

    public void clearRect(int x, int y, int width, int height) {
        ScreenUtils.clear(getBackground());
    }
    
    @Deprecated
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        
    }

    @Deprecated
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        
    }
    
    public void drawOval(float x, float y, float width, float height) {
        shapeDrawer.ellipse(x + width / 2, y + height / 2, width, height);
    }

    public void drawOval(float x, float y, float width, float height, float rotation) {
        shapeDrawer.ellipse(x + width / 2, y + height / 2, width, height, rotation);
    }

    public void drawOval(float x, float y, float width, float height, float rotation, float lineWidth) {
        shapeDrawer.ellipse(x + width / 2, y + height / 2, width, height, rotation, lineWidth);
    }

    public void drawOval(float x, float y, float width, float height, float rotation, float lineWidth, JoinType joinType) {
        shapeDrawer.ellipse(x + width / 2, y + height / 2, width, height, rotation, lineWidth, joinType);
    }

    public void fillOval(float x, float y, float width, float height) {
        shapeDrawer.filledEllipse(x + width / 2, y + height / 2, width, height);
    }

    public void fillOval(float x, float y, float width, float height, float rotation) {
        shapeDrawer.filledEllipse(x + width / 2, y + height / 2, width, height, rotation);
    }

    public void fillOval(float x, float y, float width, float height, float rotation, Color innerColor, Color outerColor) {
        shapeDrawer.filledEllipse(x + width / 2, y + height / 2, width, height, rotation, innerColor, outerColor);
    }

    public void fillOval(float x, float y, float width, float height, float rotation, float innerColor, float outerColor) {
        shapeDrawer.filledEllipse(x + width / 2, y + height / 2, width, height, rotation, innerColor, outerColor);
    }

    public void drawArc(float x, float y, float radius, float startAngle, float arcAngle) {
        shapeDrawer.arc(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle));
    }

    public void drawArc(float x, float y, float radius, float startAngle, float arcAngle, float lineWidth) {
        shapeDrawer.arc(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle), lineWidth);
    }

    public void drawArc(float x, float y, float radius, float startAngle, float arcAngle, float lineWidth, boolean useJoin) {
        shapeDrawer.arc(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle), lineWidth, useJoin);
    }
    
    public void drawArc(float x, float y, float radius, float startAngle, float arcAngle, float lineWidth, boolean useJoin, int sides) {
        shapeDrawer.arc(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle), lineWidth, useJoin, sides);
    }

    public void fillArc(float x, float y, float radius, float startAngle, float arcAngle, Color innerColor, Color outerColor) {
        shapeDrawer.sector(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle), innerColor, outerColor);
    }

    public void fillArc(float x, float y, float radius, float startAngle, float arcAngle, int sides, Color innerColor, Color outerColor) {
        shapeDrawer.sector(x + radius / 2, y + radius / 2, radius, startAngle, (float) Math.toRadians(arcAngle), sides, innerColor.toFloatBits(), outerColor.toFloatBits());
    }

    public void drawPolyline(Array<Vector2> path) {
        shapeDrawer.path(path);
    }

    public void drawPolygon(float[] xyPositions) {
        shapeDrawer.polygon(xyPositions);
    }

    public void fillPolygon(float[] xyPositions) {
        shapeDrawer.filledPolygon(xyPositions);
    }

    public void translate(float tx, float ty) {
        batch.getTransformMatrix().translate(tx, ty, 0);
    }

    public void translate(float tx, float ty, float tz) {
        batch.getTransformMatrix().translate(tx, ty, tz);
    }

    public void scale(float sx, float sy) {
        batch.getTransformMatrix().scale(sx, sy, 0f);
    }

    public void scale(float sx, float sy, float sz) {
        batch.getTransformMatrix().scale(sx, sy, sz);
    }

    public void setBackground(Color color) {
        background = color;
    }

    public Color getBackground() {
        return background;
    }

    public void setFallbackFont(BitmapFont font) {
        fallbackFont = font;
    }

    public BitmapFont getFallbackFont() {
        return fallbackFont;
    }

    @Beta
    public void drawMultiLineString(String str, int x, int y) {
//        y -= gg.getFontMetrics().getHeight();
//
//        for (String line : str.split("\n"))
//            drawString(line, x, y += gg.getFontMetrics().getHeight());
    }

    @Beta
    public void drawWrappedString(String str, int x, int y, int maxWidth) {
//        List<String> lines = StringUtils.wrap(str, getFontMetrics(getFont()), maxWidth);
//        String joined = org.apache.commons.lang3.StringUtils.join(lines.toArray(new String[]{}), '\n');
////        System.out.println(joined.charAt(0));
//        drawMultiLineString(joined, x, y);
    }

    @Beta
    public void drawTabString(String str, int x, int y) {
//        for (String line : str.split("\t"))
//            drawString(line, x += gg.getFontMetrics().getHeight(), y);
    }

//    @Override
//    public GraphicsProcessor create(int x, int y, int width, int height) {
//        return new GraphicsProcessor((GraphicsProcessor) super.create(x, y, width, height));
//    }
}
