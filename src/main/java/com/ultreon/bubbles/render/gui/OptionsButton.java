package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.common.interfaces.Listener;
import com.ultreon.hydro.gui.AbstractButton;
import com.ultreon.hydro.input.MouseController;
import com.ultreon.hydro.util.GraphicsUtils;
import com.ultreon.hydro.event._common.RenderEventPriority;
import com.ultreon.hydro.event._common.SubscribeEvent;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.hydro.event.input.MouseEvent;
import com.ultreon.hydro.event.type.MouseEventType;
import com.ultreon.hydro.gui.border.Border;
import com.ultreon.bubbles.media.AudioSlot;
import com.ultreon.bubbles.util.Util;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.net.URISyntaxException;
import java.util.Objects;

@SuppressWarnings("unused")
public class OptionsButton extends AbstractButton implements Listener {
    protected long pressedTime;
    protected final long hash;
    protected Rectangle bounds;
    protected RenderEventPriority renderEventPriority;
    protected boolean hovered;
    protected boolean pressed;
    protected boolean eventsActive = false;
    protected Runnable command = () -> {
    };
    protected int clickCount;
    protected String text;
    @Nullable
    protected Integer visualX;
    @Nullable
    protected Integer visualY;
    private EventBus.Handler binding;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setVisualX(@Nullable Integer visualX) {
        this.visualX = visualX;
    }

    @Nullable
    public Integer getVisualX() {
        return visualX;
    }


    public void setVisualY(@Nullable Integer visualY) {
        this.visualY = visualY;
    }

    @Nullable
    public Integer getVisualY() {
        return visualY;
    }

    public static class Builder {
        private Rectangle _bounds = new Rectangle(10, 10, 96, 48);
        private String _text = "";
        private RenderEventPriority _renderPriority = RenderEventPriority.AFTER_FILTER;
        private Runnable _command = () -> {
        };

        public Builder() {
        }

        public OptionsButton build() {
            OptionsButton button = new OptionsButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority);

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

    protected OptionsButton(int x, int y, int width, int height) {
        this(x, y, width, height, RenderEventPriority.AFTER_FILTER);
    }

    protected OptionsButton(int x, int y, int width, int height, RenderEventPriority renderEventPriority) {
        this.renderEventPriority = renderEventPriority;
        this.bounds = new Rectangle(x, y, width, height);

        hash = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionsButton that = (OptionsButton) o;
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
                pressedTime = System.currentTimeMillis();
                pressed = true;
            }
        } else if (evt.getType() == MouseEventType.RELEASE) {
            if (bounds.contains(evt.getPoint())) {
                command.run();
            }
            pressed = false;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void paint(Renderer gg) {
        Integer vx = visualX;
        if (visualX == null) vx = getX();
        Integer vy = visualY;
        if (visualY == null) vy = getY();
        Rectangle bounds = new Rectangle(vx, vy, getBounds().width, getBounds().height);

        Point mousePos = MouseController.instance().getCurrentPoint();
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(BubbleBlaster.getInstance().getPointerCursor());

                if (!hovered) {
                    try {
                        AudioSlot focusChangeSFX = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/sfx/ui/button/focus_change.wav")), "focusChange");
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

        if (pressed && MouseController.instance().getCurrentPoint() != null && getBounds().contains(MouseController.instance().getCurrentPoint())) {

            // Shadow
            Paint old = gg.getPaint();
            GradientPaint p = new GradientPaint(0, vy, new Color(0, 192, 255), 0f, vy + getHeight(), new Color(0, 255, 192));
            gg.paint(p);
            gg.fill(bounds);
            gg.paint(old);

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds);
            textColor = Color.white;
        } else if (hovered) {

            gg.stroke(new BasicStroke(2.0f));

            gg.color(new Color(128, 128, 128));
            gg.fill(bounds);

            // ShadowQ
//            Paint old = gg.getPaint();
            double shiftX = ((double) bounds.width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(bounds.x + ((float) shiftX - bounds.width), 0, new Color(0, 192, 255), bounds.x + (float) shiftX, 0f, new Color(0, 255, 192), true);
//            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
//            gg.setPaint(old);
            Border border = new Border(0, 0, 2, 0);
            border.setPaint(p);
            border.paintBorder(gg, bounds.x, bounds.y, bounds.width, bounds.height);
//            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds);
            textColor = new Color(255, 255, 255);
        } else {

            gg.stroke(new BasicStroke(1.0f));

            gg.color(new Color(128, 128, 128));
            gg.fill(bounds);

//            gg.setColor(new Color(128, 128, 128));
//            gg.draw(bounds);
            textColor = new Color(192, 192, 192);
        }

        paint0a(gg, textColor, oldStroke, bounds, text);
    }

    static void paint0a(Renderer gg, Color textColor, Stroke oldStroke, Rectangle bounds, String text) {
        gg.stroke(oldStroke);

        Renderer gg1 = (Renderer) gg.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
        gg1.color(textColor);
        GraphicsUtils.drawCenteredString(gg1, text, new Rectangle2D.Double(0, 0, bounds.width - 2, bounds.height - 2), new Font(BubbleBlaster.getInstance().getFont().getName(), Font.BOLD, 16));
        gg1.dispose();
    }

    @Override
    public void destroy() {

    }

    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    private int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

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
