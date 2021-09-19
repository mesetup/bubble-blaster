package com.ultreon.bubbles.render.gui;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.AbstractButton;
import com.ultreon.hydro.screen.gui.border.Border;

import java.awt.*;

public class PauseButton extends AbstractButton {
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
        private Runnable _command = () -> {
        };

        public Builder() {
        }

        public PauseButton build() {
            PauseButton button = new PauseButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height);

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

    protected PauseButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(Renderer renderer) {
        Color textColor;

        Stroke oldStroke = renderer.getStroke();

        if (isPressed() && isWithinBounds(MouseInput.getPos())) {

            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(0, y, new Color(0, 192, 255), 0f, y + height, new Color(0, 255, 192));
            renderer.paint(p);
            Border border = new Border(1, 1, 1, 1);
            border.setPaint(new Color(255, 255, 255, 128));
            border.paintBorder(renderer, x, y, width, height);
            renderer.paint(old);

            Border border1 = new Border(0, 0, 2, 0);
            border1.setPaint(p);
            border1.paintBorder(renderer, x, y, width, height);

            textColor = Color.white;
        } else if (isHovered()) {
            renderer.stroke(new BasicStroke(4.0f));

            Paint old = renderer.getPaint();
            double shiftX = ((double) width * 2) * BubbleBlaster.getTicks() / (BubbleBlaster.TPS * 10);
            GradientPaint p = new GradientPaint(x + ((float) shiftX - width), 0, new Color(0, 192, 255), x + (float) shiftX, 0f, new Color(0, 255, 192), true);
            renderer.paint(p);

            renderer.color(new Color(255, 255, 255, 128));
            renderer.fill(getBounds());

            Border border1 = new Border(0, 0, 2, 0);
            border1.setPaint(p);
            border1.paintBorder(renderer, x, y, width, height);
//            gg.draw(new Rectangle(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4));

            renderer.paint(old);

            textColor = new Color(255, 255, 255);
        } else {
            renderer.stroke(new BasicStroke(1.0f));

            renderer.color(new Color(255, 255, 255, 128));
            renderer.fill(getBounds());
//            Border border = new Border(1, 1, 1, 1);
//            border.setPaint(new Color(255, 255, 255, 128));
//            border.paintBorder(QBubbles.getInstance(), gg, bounds.x, bounds.y, bounds.width, bounds.height);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));

            textColor = new Color(255, 255, 255, 128);
        }

        OptionsNumberInput.NumberInputButton.paint0a(renderer, textColor, oldStroke, getBounds(), text);
    }

    public Runnable getCommand() {
        return command;
    }

    public void setCommand(Runnable command) {
        this.command = command;
    }
}
