package com.ultreon.bubbles.render.gui;

import com.ultreon.hydro.screen.gui.IGuiListener;
import com.ultreon.hydro.event.RenderEventPriority;
import com.ultreon.hydro.input.MouseInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.gui.AbstractButton;
import com.ultreon.hydro.screen.gui.border.Border;
import com.ultreon.hydro.screen.gui.border.OuterBorder;

import java.awt.*;

public class CrashButton extends AbstractButton implements IGuiListener {
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
            CrashButton button = new CrashButton(_bounds.x, _bounds.y, _bounds.width, _bounds.height);

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
        super(x, y, width, height);
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

    @Override
    public void render(Renderer renderer) {
        Color textColor;

        Stroke oldStroke = renderer.getStroke();

        if (isPressed() && isWithinBounds(MouseInput.getPos())) {

            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(0, y, new Color(255, 0, 0), width, y + height, new Color(255, 64, 0));
            renderer.paint(p);
            renderer.fill(getBounds());
            renderer.paint(old);

            textColor = Color.white;
        } else if (isHovered()) {
            renderer.stroke(new BasicStroke(4.0f));

            Paint old = renderer.getPaint();
            GradientPaint p = new GradientPaint(0, y, new Color(255, 0, 0), width, y + height, new Color(255, 64, 0));
            renderer.paint(p);
            Border border = new OuterBorder(2, 2, 2, 2);
            border.setPaint(p);
            border.paintBorder(renderer, x + 1, y + 1, width - 2, height - 2);
//            gg.draw(new Rectangle(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4));

            renderer.paint(old);

            textColor = new Color(255, 255, 255);
        } else {
            renderer.stroke(new BasicStroke(1.0f));

            renderer.color(new Color(255, 255, 255));
            Border border = new Border(1, 1, 1, 1);
            border.setPaint(new Color(255, 255, 255));
            border.paintBorder(renderer, x, y, width, height);
//            gg.draw(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));

            textColor = new Color(255, 255, 255);
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
