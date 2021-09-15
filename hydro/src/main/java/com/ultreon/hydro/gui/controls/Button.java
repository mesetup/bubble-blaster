package com.ultreon.hydro.gui.controls;

import com.ultreon.hydro.gui.Widget;
import com.ultreon.hydro.gui.style.State;
import com.ultreon.hydro.gui.style.Style;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.vector.Vector2f;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

@Deprecated
@SuppressWarnings("unused")
public class Button extends Widget {
    private final Style style;
    private final MouseListener mouseListener;
    private final Set<State> state;
    protected final Rectangle2D buttonRectangle;
    protected Button instance = this;

    // Size
    private double width;
    private double height;
    private double y;
    private double x;

    /**
     * Constructor for button.
     *
     * @param pos   the position.
     * @param style the style.
     */
    public Button(Vector2f pos, Style style) {
        this(pos, style, 96f, 24f);
    }

    /**
     * Constructor for button.
     *  @param pos   the position.
     * @param style the style.
     */
    public Button(Vector2f pos, Style style, float width, float height) {
        this(pos, new Vector2f(width, height), style);
        this.width = width;
        this.height = height;
    }

    /**
     * Get button states.
     *
     * @return a set of states.
     */
    public Set<State> getState() {
        return state;
    }

    public Button(Vector2f pos, Vector2f size, Style style) {
        this.buttonRectangle = new Rectangle2D.Float(pos.x, pos.y, size.x, size.y);
        this.state = new HashSet<>();
        this.state.add(State.NORMAL);
        this.style = style;
        this.mouseListener = new MouseListener();
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    protected class MouseListener extends MouseAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            if (buttonRectangle.contains(e.getPoint())) state.add(State.HOVERED);
            else state.remove(State.HOVERED);
        }
    }

    @Override
    public void paint(Renderer g) {
        if (state.contains(State.PRESSED)) {
            g.color(style.getBackgroundColors().getPressed());
            g.stroke(new BasicStroke(style.getBorderWidths().getPressed()));
//            g.fill(buttonRectangle);
            g.fill(new Rectangle2D.Double(0, 0, width, height));
            g.color(style.getBorderColors().getPressed());
            g.outline(new Rectangle2D.Double(0, 0, width, height));
        } else if (state.contains(State.NORMAL)) {
            g.color(style.getBackgroundColors().getNormal());
            g.stroke(new BasicStroke(style.getBorderWidths().getNormal()));
            g.fill(new Rectangle2D.Double(0, 0, width, height));
            g.color(style.getBorderColors().getNormal());
            g.outline(new Rectangle2D.Double(0, 0, width, height));
        }
    }

    @Override
    public void destroy() {

    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }
}
