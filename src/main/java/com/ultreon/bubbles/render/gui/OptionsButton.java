package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.hydro.screen.gui.IGuiListener;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.screen.gui.AbstractButton;
import com.ultreon.hydro.util.GraphicsUtils;
import com.ultreon.hydro.event.RenderEventPriority;
import com.ultreon.hydro.event.bus.EventBus;
import com.ultreon.hydro.screen.gui.border.Border;
import com.ultreon.bubbles.media.AudioSlot;
import com.ultreon.bubbles.util.Util;
import com.ultreon.hydro.vector.Vector2i;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import com.ultreon.hydro.render.Renderer;
import java.awt.geom.Rectangle2D;
import java.net.URISyntaxException;
import java.util.Objects;
import com.ultreon.hydro.screen.gui.Rectangle;

@SuppressWarnings("unused")
public class OptionsButton extends AbstractButton implements IGuiListener {
    protected long pressedTime;
    protected final long hash;
    @Deprecated
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
        private Runnable _command = () -> {
        };

        public Builder() {
        }

        public OptionsButton build() {
            OptionsButton button = new OptionsButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height);

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

        public Builder command(Runnable command) {
            this._command = command;
            return this;
        }
    }

    protected OptionsButton(int x, int y, int width, int height) {
        super(x, y, width, height);

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

    @Override
    public void onMousePress(int x, int y, int button) {
        if (isWithinBounds(x, y)) {
            pressedTime = System.currentTimeMillis();
            pressed = true;
        }
    }

    @Override
    public void onMouseRelease(int x, int y, int button) {
        if (isWithinBounds(x, y)) {
            command.run();
        }
        pressed = false;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void render(Renderer renderer) {
        Integer vx = visualX;
        if (visualX == null) vx = getX();
        Integer vy = visualY;
        if (visualY == null) vy = getY();
        Rectangle bounds = new Rectangle(vx, vy, getBounds().width, getBounds().height);

        Vector2i mousePos = MouseInput.getPos();
        if (isWithinBounds(mousePos)) {
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

        Color textColor;

        Stroke oldStroke = renderer.getStroke();

        if (pressed && isWithinBounds(MouseInput.getPos())) {

            // Shadow
            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(0, vy, new Color(0, 192, 255), 0f, vy + getHeight(), new Color(0, 255, 192));
            renderer.paint(p);
            renderer.fill(bounds);
            renderer.paint(old);

//            gg.setColor(new Color(0, 96, 128));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 48, 64));
//            gg.draw(bounds);
            textColor = Color.white;
        } else if (hovered) {

            renderer.stroke(new BasicStroke(2.0f));

            renderer.color(new Color(128, 128, 128));
            renderer.fill(bounds);

            // ShadowQ
//            Paint old = gg.getPaint();
            double shiftX = ((double) bounds.width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(bounds.x + ((float) shiftX - bounds.width), 0, new Color(0, 192, 255), bounds.x + (float) shiftX, 0f, new Color(0, 255, 192), true);
//            gg.setPaint(p);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
//            gg.setPaint(old);
            Border border = new Border(0, 0, 2, 0);
            border.setPaint(p);
            border.paintBorder(renderer, bounds.x, bounds.y, bounds.width, bounds.height);
//            gg.setColor(new Color(0, 192, 192));
//            gg.fill(bounds);
//            gg.setColor(new Color(0, 96, 128));
//            gg.draw(bounds);
            textColor = new Color(255, 255, 255);
        } else {

            renderer.stroke(new BasicStroke(1.0f));

            renderer.color(new Color(128, 128, 128));
            renderer.fill(bounds);

//            gg.setColor(new Color(128, 128, 128));
//            gg.draw(bounds);
            textColor = new Color(192, 192, 192);
        }

        paint0a(renderer, textColor, oldStroke, bounds, text);
    }

    static void paint0a(Renderer gg, Color textColor, Stroke oldStroke, Rectangle bounds, String text) {
        gg.stroke(oldStroke);

        Renderer gg1 = gg.create(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
        gg1.color(textColor);
        GraphicsUtils.drawCenteredString(gg1, text, new Rectangle2D.Double(0, 0, bounds.width - 2, bounds.height - 2), new Font(BubbleBlaster.getInstance().getFont().getName(), Font.BOLD, 16));
        gg1.dispose();
    }

    public void tick(AbstractGameType gameType) {

    }

    @Override
    public void make() {
        BubbleBlaster.getEventBus().register(this);
        eventsActive = true;

        Vector2i mousePos = MouseInput.getPos();
        boolean hoveredNew = isWithinBounds(mousePos);
        if (isWithinBounds(mousePos)) {
            Util.setCursor(BubbleBlaster.getInstance().getPointerCursor());
            hovered = true;
        }
    }

    @Override
    public void destroy() {
        BubbleBlaster.getEventBus().unregister(this);
        eventsActive = false;

        if (hovered) {
            Util.setCursor(BubbleBlaster.getInstance().getDefaultCursor());
            hovered = false;
        }
    }

    @Override
    public boolean isValid() {
        return eventsActive;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    @Deprecated
    public RenderEventPriority getRenderEventPriority() {
        return renderEventPriority;
    }

    @Deprecated
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
