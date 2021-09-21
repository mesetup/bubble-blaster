package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.AbstractButton;
import com.ultreon.hydro.screen.gui.border.Border;

import java.awt.*;

@SuppressWarnings("unused")
public class TitleButton extends AbstractButton {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Builder {
        private Rectangle bounds = new Rectangle(10, 10, 96, 48);
        private String text = "";
        private Runnable command = () -> {

        };

        public Builder() {
        }

        public TitleButton build() {
            TitleButton button = new TitleButton(bounds.x, bounds.y, bounds.width, bounds.height);

            button.setText(text);
            button.setCommand(command);
            return button;
        }

        public Builder bounds(Rectangle bounds) {
            this.bounds = bounds;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.bounds = new Rectangle(x, y, width, height);
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder command(Runnable command) {
            this.command = command;
            return this;
        }
    }

    protected TitleButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void render(Renderer renderer) {
        Color textColor;

        Stroke oldStroke = renderer.getStroke();

        renderer.color(new Color(96, 96, 96));
        renderer.fill(getBounds());

        if (isPressed() && isWithinBounds(MouseInput.getPos())) {
            // Shadow
            Paint old = renderer.getPaint();

            double shiftX = ((double) width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(x + ((float) shiftX - width), 0, new Color(0, 192, 255), x + (float) shiftX, 0f, new Color(0, 255, 192), true);
            renderer.color(new Color(72, 72, 72));
            renderer.fill(getBounds());

            Border border = new Border(0, 0, 1, 0);
            border.setPaint(p);
            border.paintBorder(renderer, x, y, width, height);

            renderer.paint(old);
            textColor = Color.white;
        } else if (isHovered()) {
            renderer.stroke(new BasicStroke(4.0f));

            Paint old = renderer.getPaint();

            double shiftX = ((double) width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(x + ((float) shiftX - width), 0, new Color(0, 192, 255), x + (float) shiftX, 0f, new Color(0, 255, 192), true);
            Border border = new Border(0, 0, 2, 0);
            border.setPaint(p);
            border.paintBorder(renderer, x, y, width, height);

            renderer.paint(old);
            textColor = new Color(255, 255, 255);
        } else {
            renderer.stroke(new BasicStroke(1.0f));
            textColor = new Color(224, 224, 224);
        }

        OptionsNumberInput.NumberInputButton.paint0a(renderer, textColor, oldStroke, getBounds(), text);
    }

    @SuppressWarnings("EmptyMethod")
    public void tick(AbstractGameType gameType) {

    }
}
