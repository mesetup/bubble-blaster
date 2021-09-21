package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.api.event.keyboard.KeyboardModifiers;
import com.ultreon.bubbles.util.Util;
import com.ultreon.bubbles.util.helpers.MathHelper;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.IGuiListener;
import com.ultreon.hydro.screen.gui.Rectangle;
import com.ultreon.hydro.screen.gui.Widget;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class OptionsTextEntry extends Widget implements com.ultreon.bubbles.api.event.KeyboardEvent.KeyboardEventListener, IGuiListener {
    // Fonts.
    protected final Font defaultFont = new Font(Util.getGame().getSansFontName(), Font.PLAIN, 24);

    // Cursor Index/
    protected int cursorIndex;

    // Events/
    private boolean eventsActive;

    // Values.
    protected String text;

    // State
    protected boolean activated;

    public OptionsTextEntry(Rectangle bounds) {
        super(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public OptionsTextEntry(int x, int y, int width, int height) {
        this(new Rectangle(x, y, width, height));
    }

    @SubscribeEvent
    @Override
    public void onMouseRelease(int x, int y, int button) {
        if (button == 1) {
            activated = getBounds().contains(x, y);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void onKeyboard(KeyEventType type, char key, int keyCode, KeyboardModifiers modifiers) {
        if (!activated) return;

        if (type == KeyEventType.PRESS || type == KeyEventType.HOLD) {
            if (keyCode == KeyEvent.VK_BACK_SPACE) {
                if (text.length() == 0) return;

                String leftText = text.substring(0, cursorIndex - 1);
                String rightText = text.substring(cursorIndex);

                text = leftText + rightText;

                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length());
                return;
            }

            if (keyCode == KeyEvent.VK_LEFT) {
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length());
                return;
            }

            if (keyCode == KeyEvent.VK_RIGHT) {
                cursorIndex = MathHelper.clamp(cursorIndex + 1, 0, text.length());
                return;
            }

            char c = key;

            if (keyCode == KeyEvent.VK_DEAD_ACUTE) {
                c = '\'';
            }

            if (keyCode == KeyEvent.VK_QUOTEDBL) {
                c = '"';
            }

            if ((short) c >= 32) {
//                text += c;
                String leftText = text.substring(0, cursorIndex);
                String rightText = text.substring(cursorIndex);

                text = leftText + c + rightText;

                cursorIndex++;
            }
        }
    }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        if (!activated) return;

        if (evt.getType() == KeyEventType.PRESS || evt.getType() == KeyEventType.HOLD) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (text.length() == 0) return;

                String leftText = text.substring(0, cursorIndex - 1);
                String rightText = text.substring(cursorIndex);

                text = leftText + rightText;

                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_LEFT) {
                cursorIndex = MathHelper.clamp(cursorIndex - 1, 0, text.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_RIGHT) {
                cursorIndex = MathHelper.clamp(cursorIndex + 1, 0, text.length());
                return;
            }

            char c = evt.getParentEvent().getKeyChar();

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_DEAD_ACUTE) {
                c = '\'';
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_QUOTEDBL) {
                c = '"';
            }

            if ((short) c >= 32) {
//                text += c;
                String leftText = text.substring(0, cursorIndex);
                String rightText = text.substring(cursorIndex);

                text = leftText + c + rightText;

                cursorIndex++;
            }
        }
    }

    @Override
    public void make() {
        eventsActive = true;
        BubbleBlaster.getEventBus().subscribe(this);
    }

    @Override
    public void destroy() {
        eventsActive = false;
        BubbleBlaster.getEventBus().unsubscribe(this);
    }

    @Override
    public boolean isValid() {
        return eventsActive;
    }

    @Override
    public void render(Renderer renderer) {
        if (activated) {
            renderer.color(new Color(128, 128, 128));
            renderer.fill(getBounds());

            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(0, this.y, new Color(0, 192, 255), 0f, this.y + getHeight(), new Color(0, 255, 192));
            renderer.paint(p);
            renderer.fill(new Rectangle(x, y + height, width, 4));
            renderer.paint(old);
        } else {
            renderer.color(new Color(64, 64, 64));
            renderer.fill(getBounds());
        }

        renderer.color(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(renderer, text, new Point2D.Double(2, getY() + getHeight() - (getHeight() - 4)), getHeight() - 4, defaultFont);

        FontMetrics fontMetrics = renderer.getFontMetrics(defaultFont);

        int cursorX;
        renderer.color(new Color(0, 192, 192, 255));
        if (cursorIndex >= text.length()) {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 2 + getX();
            } else {
                cursorX = getX();
            }

            cursorX += getX();

            renderer.line(cursorX, getY() + 2, cursorX, getY() + getHeight() - 2);
            renderer.line(cursorX + 1, getY() + 2, cursorX + 1, getY() + getHeight() - 2);
        } else {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + getX();
            } else {
                cursorX = getX();
            }

            int width = fontMetrics.charWidth(text.charAt(cursorIndex));

            renderer.line(cursorX, getY() + getHeight() - 2, cursorX + width, getY() + getHeight() - 2);
            renderer.line(cursorX, getY() + getHeight() - 1, cursorX + width, getY() + getHeight() - 1);
        }
    }

    @SuppressWarnings("EmptyMethod")
    public void tick() {

    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Deprecated
    public Integer getVisualX() {
        return this.x;
    }

    @Deprecated
    public void setVisualX(Integer visualX) {

    }

    @Deprecated
    public Integer getVisualY() {
        return this.y;
    }

    @Deprecated
    public void setVisualY(Integer visualY) {

    }

    public static class Builder {
        public Rectangle bounds = null;
        private String _text = "";

        public Builder() {

        }

        public OptionsTextEntry build() {
            if (bounds == null) throw new IllegalArgumentException("Missing bounds for creating OptionsTextEntry.");

            OptionsTextEntry obj = new OptionsTextEntry(bounds.x, bounds.y, bounds.width, bounds.height);
            obj.setText(_text);

            return obj;
        }

        public Builder bounds(Rectangle _bounds) {
            this.bounds = _bounds;
            return this;
        }

        public Builder text(String _text) {
            this._text = _text;
            return this;
        }
    }
}
