package com.qtech.bubbles.gui;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.interfaces.Listener;
import com.qtech.bubbles.core.controllers.MouseController;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.event.MouseEvent;
import com.qtech.bubbles.event.RenderEventPriority;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.bus.EventBus;
import com.qtech.bubbles.event.type.MouseEventType;
import com.qtech.bubbles.graphics.Border;
import com.qtech.bubbles.media.AudioSlot;
import com.qtech.bubbles.util.Util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.net.URISyntaxException;
import java.util.Objects;

@SuppressWarnings("unused")
public class TitleButton extends AbstractButton implements Listener {
    private final long hash;
    private Rectangle bounds;
    private RenderEventPriority renderEventPriority;
    private boolean hovered;
    private boolean pressed;
    private boolean eventsActive = false;
    private Runnable command;
    private int clickCount;
    private String text;
    private EventBus.Handler binding;

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

        public TitleButton build() {
            TitleButton button = new TitleButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority);

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

    protected TitleButton(int x, int y, int width, int height) {
        this(x, y, width, height, RenderEventPriority.AFTER_FILTER);
    }

    protected TitleButton(int x, int y, int width, int height, RenderEventPriority renderEventPriority) {
        this.renderEventPriority = renderEventPriority;
        this.bounds = new Rectangle(x, y, width, height);

        hash = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleButton that = (TitleButton) o;
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
        }
    }
//
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

    public void paint(GraphicsProcessor gg) {
//        hovered = MouseController.instance().getCurrentPoint() != null && bounds.contains(MouseController.instance().getCurrentPoint());

        Point mousePos = MouseController.instance().getCurrentPoint();
        boolean hoveredNew = mousePos != null && bounds.contains(mousePos);
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.getInstance().getPointerCursor());

                if (!hovered) {
                    try {
                        AudioSlot focusChangeSFX = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/bubbleblaster/audio/sfx/ui/button/focus_change.wav")), "focusChange");
                        focusChangeSFX.setVolume(0.2d);
                        focusChangeSFX.play();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
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

        gg.setColor(new Color(96, 96, 96));
        gg.fill(bounds);

        if (pressed && MouseController.instance().getCurrentPoint() != null && bounds.contains(MouseController.instance().getCurrentPoint())) {
            // Shadow
            Paint old = gg.getPaint();
            double shiftX = ((double)bounds.width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(bounds.x + ((float) shiftX - bounds.width), 0, new Color(0, 192, 255), bounds.x + (float)shiftX, 0f, new Color(0, 255, 192), true);
            gg.setColor(new Color(72, 72, 72));
            gg.fill(bounds);

            Border border = new Border(0, 0, 1, 0);
            border.setPaint(p);
            border.paintBorder(gg, bounds.x, bounds.y, bounds.width, bounds.height);
            gg.setPaint(old);

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds);
            textColor = Color.white;
        } else if (hovered) {
            gg.setStroke(new BasicStroke(4.0f));

            // Shadow
            Paint old = gg.getPaint();

            double shiftX = ((double)bounds.width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(bounds.x + ((float) shiftX - bounds.width), 0, new Color(0, 192, 255), bounds.x + (float)shiftX, 0f, new Color(0, 255, 192), true);
//            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
            Border border = new Border(0, 0, 2, 0);
            border.setPaint(p);
            border.paintBorder(gg, bounds.x, bounds.y, bounds.width, bounds.height);
            gg.setPaint(old);
//            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds);
            textColor = new Color(255, 255, 255);
        } else {
            gg.setStroke(new BasicStroke(1.0f));

//            gg.setColor(new Color(255, 255, 255, 128));
//            gg.draw(bounds);
//            Border border = new Border(1, 1, 1, 1);
//            border.setPaint(new Color(255, 255, 255, 128));
//            border.paintBorder(QBubbles.getInstance(), gg, bounds.x, bounds.y, bounds.width, bounds.height);
            textColor = new Color(224, 224, 224);
        }

        gg.setStroke(oldStroke);

        GraphicsProcessor gg1 = (GraphicsProcessor) gg.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
        gg1.setColor(textColor);
        GraphicsUtils.drawCenteredString(gg1, text, new Rectangle2D.Double(0, 0, bounds.width - 2, bounds.height - 2), new Font(BubbleBlaster.getInstance().getFont().getName(), Font.BOLD, 16));
        gg1.dispose();
    }

    @Override
    public void destroy() {

    }

    @SuppressWarnings("EmptyMethod")
    public void tick(AbstractGameType gameType) {

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
