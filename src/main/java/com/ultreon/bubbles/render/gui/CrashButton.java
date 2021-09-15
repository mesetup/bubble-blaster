package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.interfaces.Listener;
import com.ultreon.hydro.gui.Widget;
import com.ultreon.hydro.input.MouseController;
import com.ultreon.hydro.event._common.RenderEventPriority;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.input.MouseEvent;
import com.ultreon.hydro.event.type.MouseEventType;
import com.ultreon.hydro.gui.border.Border;
import com.ultreon.hydro.gui.border.OuterBorder;
import com.ultreon.bubbles.util.Util;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.util.Objects;

public class CrashButton extends Widget implements Listener {
    private final long hash;
    private Rectangle bounds;
    private RenderEventPriority renderEventPriority;
    private boolean hovered;
    private boolean pressed;
    private boolean eventsActive = false;
    private Runnable command;
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Builder {
        private Rectangle _bounds = new Rectangle(10, 10, 96, 48);
        private String _text = "";
        private RenderEventPriority _renderPriority = RenderEventPriority.AFTER_FILTER;
        private Runnable _command = () -> {
        };

        public Builder() {
        }

        public CrashButton build() {
            CrashButton button = new CrashButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority);

            button.setText(_text);
            button.setCommand(_command);
            return button;
        }

        public Builder bounds(Rectangle bounds) {
            this._bounds = bounds;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this._bounds = new Rectangle(x, y, width, height);
            return this;
        }

        public Builder text(String text) {
            this._text = text;
            return this;
        }

        public Builder renderPriority(RenderEventPriority renderPriority) {
            this._renderPriority = renderPriority;
            return this;
        }

        public Builder command(Runnable command) {
            this._command = command;
            return this;
        }
    }

    public CrashButton(int x, int y, int width, int height) {
        this(x, y, width, height, RenderEventPriority.AFTER_FILTER);
    }

    protected CrashButton(int x, int y, int width, int height, RenderEventPriority renderEventPriority) {
        this.renderEventPriority = renderEventPriority;
        this.bounds = new Rectangle(x, y, width, height);

        hash = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrashButton that = (CrashButton) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @SubscribeEvent
    public void onMouse(MouseEvent evt) {
        if (evt.getType() == MouseEventType.PRESS) {
            if (bounds.contains(evt.getPoint())) {
                pressed = true;
            }
        } else if (evt.getType() == MouseEventType.RELEASE) {
            if (bounds.contains(evt.getPoint())) {
                command.run();
            }
            pressed = false;
//        } else if (evt.getType() == MouseEventType.CLICK) {
        }
    }

//    @SubscribeEvent
//    public void onMouseMotion(MouseMotionEvent evt) {
//        if (bounds.contains(evt.getParentEvent().getPoint())) {
//            QBubbles.setCursor(Game.instance().getPointerCursor());
//            hovered = true;
//        } else {
//            if (hovered) {
//                QBubbles.setCursor(Game.instance().getDefaultCursor());
//                hovered = false;
//            }
//        }
//    }

    public void paint(Renderer gg) {
//        hovered = MouseController.instance().getCurrentPoint() != null && bounds.contains(MouseController.instance().getCurrentPoint());

        Point mousePos = MouseController.instance().getCurrentPoint();
        boolean hoveredNew = mousePos != null && bounds.contains(mousePos);
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.getInstance().getPointerCursor());
                hovered = true;
            } else {
                if (hovered) {
                    Util.setCursor(BubbleBlaster.getInstance().getDefaultCursor());
                    hovered = false;
                }
            }
        }

        Color textColor;

        Stroke oldStroke = gg.getStroke();

        if (pressed && MouseController.instance().getCurrentPoint() != null && bounds.contains(MouseController.instance().getCurrentPoint())) {

            Paint old = gg.getPaint();
            GradientPaint p = new GradientPaint(0, bounds.y, new Color(255, 0, 0), bounds.width, bounds.y + bounds.height, new Color(255, 64, 0));
            gg.paint(p);
            gg.fill(bounds);
            gg.paint(old);

            textColor = Color.white;
        } else if (hovered) {
            gg.stroke(new BasicStroke(4.0f));

            Paint old = gg.getPaint();
            GradientPaint p = new GradientPaint(0, bounds.y, new Color(255, 0, 0), bounds.width, bounds.y + bounds.height, new Color(255, 64, 0));
            gg.paint(p);
            Border border = new OuterBorder(2, 2, 2, 2);
            border.setPaint(p);
            border.paintBorder(gg, bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
//            gg.draw(new Rectangle(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4));

            gg.paint(old);

            textColor = new Color(255, 255, 255);
        } else {
            gg.stroke(new BasicStroke(1.0f));

            gg.color(new Color(255, 255, 255));
            Border border = new Border(1, 1, 1, 1);
            border.setPaint(new Color(255, 255, 255));
            border.paintBorder(gg, bounds.x, bounds.y, bounds.width, bounds.height);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));

            textColor = new Color(255, 255, 255);
        }

        OptionsNumberInput.NumberInputButton.paint0a(gg, textColor, oldStroke, bounds, text);
    }

    @Override
    public void destroy() {
        // Do nothing!
    }

    @SuppressWarnings("EmptyMethod")
    public void tick() {

    }

    @Override
    public void bindEvents() {
        BubbleBlaster.getEventBus().register(this);
        eventsActive = true;

        Point mousePos = MouseController.instance().getCurrentPoint();
        boolean hoveredNew = mousePos != null && bounds.contains(mousePos);
        if (mousePos != null && bounds.contains(mousePos)) {
            Util.setCursor(BubbleBlaster.getInstance().getPointerCursor());
            hovered = true;
        }
    }

    @Override
    public void unbindEvents() {
        BubbleBlaster.getEventBus().unregister(this);
        eventsActive = false;

        if (hovered) {
            Util.setCursor(BubbleBlaster.getInstance().getDefaultCursor());
            hovered = false;
        }
    }

    @Override
    public boolean eventsAreActive() {
        return eventsActive;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setHeight(int height) {
        this.bounds.height = height;
    }

    public void setWidth(int width) {
        this.bounds.width = width;
    }

    public void setX(int x) {
        this.bounds.x = x;
    }

    public void setY(int y) {
        this.bounds.y = y;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public RenderEventPriority getRenderEventPriority() {
        return renderEventPriority;
    }

    public void setRenderEventPriority(RenderEventPriority renderEventPriority) {
        this.renderEventPriority = renderEventPriority;
    }

    public Runnable getCommand() {
        return command;
    }

    public void setCommand(Runnable command) {
        this.command = command;
    }
}
