package com.qtech.bubbles.gui;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.api.event.keyboard.KeyboardModifiers;
import com.qtech.bubbles.common.interfaces.Listener;
import com.qtech.bubbles.core.controllers.MouseController;
import com.qtech.bubbles.core.utils.categories.GraphicsUtils;
import com.qtech.bubbles.event.KeyboardEvent;
import com.qtech.bubbles.event.MouseEvent;
import com.qtech.bubbles.event.RenderEventPriority;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.type.KeyEventType;
import com.qtech.bubbles.util.Util;
import com.qtech.bubbles.util.helpers.MathHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class OptionsTextEntry extends Widget implements com.qtech.bubbles.api.event.KeyboardEvent.KeyboardEventListener, Listener {
    // Fonts.
    protected final Font defaultFont = new Font(Util.getGame().getSansFontName(), Font.PLAIN, 24);

    // Bounds.
    private Rectangle bounds;

    // Cursor Index/
    protected int cursorIndex;

    // Events/
    private boolean eventsActive;

    // Values.
    protected String text;

    // Render priority/
    private RenderEventPriority renderEventPriority;

    // State
    protected boolean activated;
    protected Integer visualX;
    protected Integer visualY;
    protected boolean hovered;

    // Constructor.
    public OptionsTextEntry(Rectangle bounds) {
        this(bounds.x, bounds.y, bounds.width, bounds.height, RenderEventPriority.AFTER_FILTER);
    }

    public OptionsTextEntry(Rectangle bounds, RenderEventPriority renderEventPriority) {
        this.bounds = bounds;
        this.renderEventPriority = renderEventPriority;
    }

    public OptionsTextEntry(int x, int y, int width, int height) {
        this(new Rectangle(x, y, width, height));
    }

    public OptionsTextEntry(int x, int y, int width, int height, RenderEventPriority renderEventPriority) {
        this(new Rectangle(x, y, width, height), renderEventPriority);
    }

    @SubscribeEvent
    public void onMouse(MouseEvent evt) {
        if (evt.getButton() == 1) {
            activated = getBounds().contains(evt.getPoint());
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
    public void bindEvents() {
        eventsActive = true;
        QBubbles.getEventBus().register(this);
    }

    @Override
    public void unbindEvents() {
        eventsActive = false;
        QBubbles.getEventBus().unregister(this);
    }

    @Override
    public boolean eventsAreActive() {
        return eventsActive;
    }

    @Override
    public void paint(Graphics2D gg) {
        Integer vx = visualX;
        if (visualX == null) vx = getX();
        Integer vy = visualY;
        if (visualY == null) vy = getY();
        Rectangle bounds = new Rectangle(vx, vy, getBounds().width, getBounds().height);

        Point mousePos = MouseController.instance().getCurrentPoint();
        if (mousePos != null) {
            if (bounds.contains(mousePos)) {
                Util.setCursor(QBubbles.getInstance().getTextCursor());
                hovered = true;
            } else {
                if (hovered) {
                    Util.setCursor(QBubbles.getInstance().getDefaultCursor());
                    hovered = false;
                }
            }
        }

        if (activated) {
            gg.setColor(new Color(128, 128, 128));
            gg.fill(bounds);

            Paint old = gg.getPaint();
            GradientPaint p = new GradientPaint(0, vy, new Color(0, 192, 255), 0f, vy + getHeight(), new Color(0, 255, 192));
            gg.setPaint(p);
            gg.fill(new Rectangle(bounds.x, bounds.y + bounds.height, bounds.width, 4));
            gg.setPaint(old);
        } else {
            gg.setColor(new Color(64, 64, 64));
            gg.fill(bounds);
        }

        gg.setColor(new Color(255, 255, 255, 255));
        GraphicsUtils.drawLeftAnchoredString(gg, text, new Point2D.Double(2, getY() + getHeight() - (getHeight() - 4)), getHeight() - 4, defaultFont);

        FontMetrics fontMetrics = gg.getFontMetrics(defaultFont);

        int cursorX;
        gg.setColor(new Color(0, 192, 192, 255));
        if (cursorIndex >= text.length()) {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 2 + getX();
            } else {
                cursorX = getX();
            }

            cursorX += getX();

            gg.drawLine(cursorX, getY() + 2, cursorX, getY() + getHeight() - 2);
            gg.drawLine(cursorX + 1, getY() + 2, cursorX + 1, getY() + getHeight() - 2);
        } else {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + getX();
            } else {
                cursorX = getX();
            }

            int width = fontMetrics.charWidth(text.charAt(cursorIndex));

            gg.drawLine(cursorX, getY() + getHeight() - 2, cursorX + width, getY() + getHeight() - 2);
            gg.drawLine(cursorX, getY() + getHeight() - 1, cursorX + width, getY() + getHeight() - 1);
        }
    }

    @Override
    public void destroy() {

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

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public int getX() {
        return bounds.x;
    }

    public void setX(int x) {
        bounds.x = x;
    }

    public int getY() {
        return bounds.y;
    }

    public void setY(int y) {
        bounds.y = y;
    }

    public int getWidth() {
        return bounds.width;
    }

    public void setWidth(int width) {
        bounds.width = width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public void setHeight(int height) {
        bounds.height = height;
    }

    public RenderEventPriority getRenderEventPriority() {
        return renderEventPriority;
    }

    public void setRenderEventPriority(RenderEventPriority renderEventPriority) {
        this.renderEventPriority = renderEventPriority;
    }

    public Integer getVisualX() {
        return visualX;
    }

    public void setVisualX(Integer visualX) {
        this.visualX = visualX;
    }

    public Integer getVisualY() {
        return visualY;
    }

    public void setVisualY(Integer visualY) {
        this.visualY = visualY;
    }

    public static class Builder {
        public Rectangle _bounds = null;
        private String _text = "";
        private RenderEventPriority _renderPriority = null;

        public Builder() {

        }

        public OptionsTextEntry build() {
            if (_bounds == null) {
                throw new IllegalArgumentException("Missing bounds for creating OptionsTextEntry.");
            }

            OptionsTextEntry obj;
            if (_renderPriority == null)
                obj = new OptionsTextEntry(_bounds.x, _bounds.y, _bounds.width, _bounds.height);
            else
                obj = new OptionsTextEntry(_bounds.x, _bounds.y, _bounds.width, _bounds.height, _renderPriority);
            obj.setText(_text);

            return obj;
        }

        public Builder bounds(Rectangle _bounds) {
            this._bounds = _bounds;
            return this;
        }

        public Builder text(String _text) {
            this._text = _text;
            return this;
        }

        public Builder renderPriority(RenderEventPriority _renderPriority) {
            this._renderPriority = _renderPriority;
            return this;
        }
    }
}
