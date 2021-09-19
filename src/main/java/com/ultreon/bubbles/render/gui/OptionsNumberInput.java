package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.util.helpers.MathHelper;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.border.Border;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

@SuppressWarnings("unused")
public class OptionsNumberInput extends OptionsTextEntry {
    private final NumberInputButton upButton;
    private final NumberInputButton downButton;
    private boolean eventsActive = false;

    // Value
    private int value;
    private int min;
    private int max;

    public OptionsNumberInput(Rectangle bounds, int value, int min, int max) {
        this(bounds.x, bounds.y, bounds.width, bounds.height, value, min, max);
    }

    public OptionsNumberInput(int x, int y, int width, int height, int value, int min, int max) {
        super(x, y, width, height);
        this.value = value;
        this.min = min;
        this.max = max;
        this.upButton = new NumberInputButton(0, 0, 0, 0);
        this.downButton = new NumberInputButton(0, 0, 0, 0);

        upButton.setCommand(this::add);
        downButton.setCommand(this::subtract);

        text = Integer.toString(value);

        cursorIndex = Integer.toString(value).length();
    }

    private void add() {
        value = MathHelper.clamp(value + 1, min, max);
        text = Integer.toString(value);
    }

    private void subtract() {
        value = MathHelper.clamp(value - 1, min, max);
        text = Integer.toString(value);
    }

    @SubscribeEvent
    @Override
    public void onMousePress(int x, int y, int button) {
        super.onMousePress(x, y, button);
        if (!activated) {
            try {
                value = MathHelper.clamp(Integer.parseInt(text), min, max);
                if (!text.equals(Integer.toString(value)))
                    cursorIndex = Integer.toString(value).length();
                text = Integer.toString(value);
            } catch (NumberFormatException e) {
                value = MathHelper.clamp(0, min, max);
                text = Integer.toString(value);
                cursorIndex = text.length();
            }
        }
    }

    @SubscribeEvent
    @Override
    public void onKeyboard(KeyboardEvent evt) {
        if (!activated) return;

        if (evt.getType() == KeyEventType.PRESS || evt.getType() == KeyEventType.HOLD) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (cursorIndex == 0) return;
                String leftText = text.substring(0, cursorIndex - 1);
                String rightText = text.substring(cursorIndex);

                text = leftText + rightText;

                cursorIndex--;
                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_LEFT) {
                cursorIndex--;

                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
                return;
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_RIGHT) {
                cursorIndex++;

                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
                return;
            }

            char c = evt.getParentEvent().getKeyChar();

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_DEAD_ACUTE) {
                c = '\'';
            }

            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_QUOTEDBL) {
                c = '"';
            }

            if ("0123456789".contains(Character.toString(c))) {
//                text += c;
                String leftText = text.substring(0, cursorIndex);
                String rightText = text.substring(cursorIndex);

                text = leftText + c + rightText;

                cursorIndex++;

                cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
            }
        }
    }

    @Override
    public void make() {
        eventsActive = true;
        upButton.make();
        downButton.make();
        BubbleBlaster.getEventBus().register(this);
    }

    @Override
    public void destroy() {
        eventsActive = false;
        upButton.destroy();
        downButton.destroy();
        BubbleBlaster.getEventBus().unregister(this);
    }

    @Override
    public boolean isValid() {
        return eventsActive;
    }

    @Override
    public void render(Renderer renderer) {
        upButton.setY(this.y);
        upButton.setX(this.x + getBounds().width - 24);
        upButton.setHeight(getBounds().height / 2);
        upButton.setWidth(24);
        upButton.setText("+");

        downButton.setY(this.y + getBounds().height / 2);
        downButton.setX(this.x + getBounds().width - 24);
        downButton.setHeight(getBounds().height / 2);
        downButton.setWidth(24);
        downButton.setText("-");

        if (activated) {
            renderer.color(new Color(128, 128, 128));
            renderer.fill(getBounds());

            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(this.x, 0, new Color(0, 192, 255), this.x + getWidth(), 0, new Color(0, 255, 192));
            renderer.paint(p);
            renderer.fill(new Rectangle(x, y + height - 2, width, 2));
            renderer.paint(old);
        } else {
            renderer.color(new Color(79, 79, 79));
            renderer.fill(getBounds());
        }

        Renderer gg1 = renderer.create(x, y, width, height);
        gg1.color(new Color(255, 255, 255, 255));

        cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
        GraphicsUtils.drawLeftAnchoredString(gg1, text, new Point2D.Double(8, height - (height - 5)), height - 5, defaultFont);

        FontMetrics fontMetrics = renderer.getFontMetrics(defaultFont);

        upButton.render(renderer);
        downButton.render(renderer);

        int cursorX;
        gg1.color(new Color(0, 192, 192, 255));
        if (cursorIndex >= text.length()) {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 8;
            } else {
                cursorX = 10;
            }

            gg1.line(cursorX, 2, cursorX, height - 5);
            gg1.line(cursorX + 1, 2, cursorX + 1, height - 5);
        } else {
            if (text.length() != 0) {
                cursorX = fontMetrics.stringWidth(text.substring(0, cursorIndex)) + 8;
            } else {
                cursorX = 10;
            }

            int width = fontMetrics.charWidth(text.charAt(cursorIndex));

            gg1.line(cursorX, height - 5, cursorX + width, height - 5);
            gg1.line(cursorX, height - 4, cursorX + width, height - 4);
        }

        gg1.dispose();
    }

    @Override
    @SubscribeEvent
    public void tick() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = MathHelper.clamp(value, min, max);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        value = MathHelper.clamp(value, min, max);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        value = MathHelper.clamp(value, min, max);
    }

    public static class NumberInputButton extends OptionsButton {
        private long previousCommand;

        @SuppressWarnings("SameParameterValue")
        protected NumberInputButton(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void render(Renderer renderer) {
            MouseInput.getPos();
            hovered = isWithinBounds(MouseInput.getPos());

            Color textColor;

            Stroke oldStroke = renderer.getStroke();

            Integer vx = visualX;
            if (visualX == null) vx = getX();
            Integer vy = visualY;
            if (visualY == null) vy = getY();
            Rectangle bounds = new Rectangle(vx, vy, getBounds().width, getBounds().height);

            if (pressed && getBounds().contains(MouseInput.getPos())) {

                // Shadow
                Paint old = renderer.getPaint();
                GradientPaint p = new GradientPaint(0, vy, new Color(0, 192, 255), 0f, vy + getHeight(), new Color(0, 255, 192));
                renderer.paint(p);
                renderer.fill(bounds);
                renderer.paint(old);

                textColor = Color.white;
            } else if (hovered) {
                renderer.stroke(new BasicStroke(1.0f));

                renderer.color(new Color(128, 128, 128));
                renderer.fill(bounds);

                // Shadow
//                Paint old = gg.getPaint();
                GradientPaint p = new GradientPaint(0, vy, new Color(0, 192, 255), 0f, vy + getHeight(), new Color(0, 255, 192));
//                gg.setPaint(p);
//                gg.draw(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1));
                Border border = new Border(2, 2, 2, 2);
                border.setPaint(p);
                border.paintBorder(renderer, bounds.x, bounds.y, bounds.width, bounds.height);
//                gg.setPaint(old);

                textColor = new Color(255, 255, 255);
            } else {

                renderer.stroke(new BasicStroke(1.0f));

                renderer.color(new Color(128, 128, 128));
                renderer.fill(bounds);

//            gg.setColor(new Color(128, 128, 128));
//            gg.draw(bounds);
                textColor = new Color(192, 192, 192);
            }

            paint0a(renderer, textColor, oldStroke, getBounds(), text);
        }

        @Override
        @SubscribeEvent
        public void tick(AbstractGameType gameType) {
            super.tick(gameType);
            if (pressed) {
                if (pressedTime + 1000 < System.currentTimeMillis()) {
                    if (previousCommand < System.currentTimeMillis()) {
                        previousCommand = System.currentTimeMillis() + 25;
                        this.command.run();
                    }
                }
            }
        }
    }
}
