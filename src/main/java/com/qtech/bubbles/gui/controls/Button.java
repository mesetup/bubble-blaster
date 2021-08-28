package com.qtech.bubbles.gui.controls;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.gui.Widget;
import com.qtech.bubbles.gui.style.State;
import com.qtech.bubbles.gui.style.Style;
import com.qtech.bubbles.util.position.AbsoluteSize;
import com.qtech.bubbles.util.position.Position;
import com.qtech.bubbles.util.position.Size;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

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
    public Button(Position pos, Style style) {
        this(pos, style, 96d, 24d);
    }

    /**
     * Constructor for button.
     *
     * @param pos   the position.
     * @param style the style.
     */
    public Button(Position pos, Style style, double width, double height) {
        this(pos, new AbsoluteSize(width, height), style);
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

    public Button(Position pos, Size size, Style style) {
        this.buttonRectangle = new Rectangle2D.Double(pos.getPointX(), pos.getPointY(), size.getWidth(), size.getHeight());
        this.state = new HashSet<>();
        this.state.add(State.NORMAL);
        this.style = style;
        this.mouseListener = new MouseListener();

        BubbleBlaster.getInstance().addMouseListener(this.mouseListener);
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
    public void paint(Graphics2D g) {
        if (state.contains(State.PRESSED)) {
            g.setColor(style.getBackgroundColors().getPressed());
            g.setStroke(new BasicStroke(style.getBorderWidths().getPressed()));
//            g.fill(buttonRectangle);
            g.fill(new Rectangle2D.Double(0, 0, width, height));
            g.setColor(style.getBorderColors().getPressed());
            g.draw(new Rectangle2D.Double(0, 0, width, height));
        } else if (state.contains(State.NORMAL)) {
            g.setColor(style.getBackgroundColors().getNormal());
            g.setStroke(new BasicStroke(style.getBorderWidths().getNormal()));
            g.fill(new Rectangle2D.Double(0, 0, width, height));
            g.setColor(style.getBorderColors().getNormal());
            g.draw(new Rectangle2D.Double(0, 0, width, height));
        }
    }

    @Override
    public void destroy() {
//        Game.instance().removeMouseListener(this.mouseListener);
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }
}
