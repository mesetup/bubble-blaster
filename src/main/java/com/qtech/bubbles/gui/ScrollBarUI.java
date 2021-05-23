package com.qtech.bubbles.gui;

import com.qtech.bubbles.graphics.Border;
import javafx.geometry.Orientation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBarUI extends BasicScrollBarUI {
    private final Dimension d = new Dimension();
    private final @NotNull Orientation orientation;
    @SuppressWarnings("FieldCanBeLocal")
    private JButton button;

    public ScrollBarUI(@NotNull Orientation orientation) {
        super();
        this.orientation = orientation;
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ScrollBarUI(((JScrollBar) c).getOrientation() == JScrollBar.VERTICAL ? Orientation.VERTICAL : Orientation.HORIZONTAL);
    }


    @Override
    protected JButton createDecreaseButton(int orientation) {
        button = new JButton() {

            @Override
            public Dimension getPreferredSize() {
                return d;
            }
        };

        return button;
//
//        button.setUI(new BasicButtonUI() {
//            @Override
//            public void paint(Graphics g, JComponent c) {
//                super.paint(g, c);
//
//                JButton button = (JButton)c;
//
//                Dimension dim = button.getSize();
//
//                if (button.getModel().isPressed()) {
//                    Border border = new Border(1, 1, 1, 1);
//                    border.setPaint(new LinearGradientPaint(0, 0, 0, dim.height, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
//                    border.paintBorder(c, g, 0, 0, dim.width, dim.height);
//
//                    g.setColor(new Color(72, 72, 72));
//                } else if (button.getModel().isRollover() || button.getModel().isSelected()) {
//                    Border border = new Border(1, 1, 1, 1);
//                    border.setPaint(new LinearGradientPaint(0, 0, 0, dim.height, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
//                    border.paintBorder(c, g, 0, 0, dim.width, dim.height);
//
//                    g.setColor(new Color(128, 128, 128));
//                }
//                g.fillRect(0, 0, c.getWidth(), c.getHeight());
//
////                g.setColor(Color.white);
////
////                int size = Math.min(dim.width, dim.height);
////
////                Insets2 insets
////                if (dim.width > size) insets = new Insets2(0, dim.width / 2 - size, 0, dim.width / 2 - size)
////                else if (dim.height > size) insets = new Insets2(dim.width / 2 - size, 0, dim.width / 2 - size, 0);
////                else insets = new Insets2(0, 0, 0, 0);
////
////                insets.shrink(2);
////
////                if (orientation == SwingConstants.NORTH) {
////                    // Up arrow
////                    g.drawLine(insets.left, insets.bottom + size - size / 3, insets.left + size / 2, insets.top + size / 3);
////                    g.drawLine(insets.left + size / 2, insets.top + size / 3, insets.left + size, insets.bottom + size - size / 3);
////                }
////                if (orientation == SwingConstants.SOUTH) {
////                    // Down arrow
////                    g.drawLine(insets.left, insets.top + size / 3, insets.left + size / 2, insets.bottom + size - size / 3);
////                    g.drawLine(insets.left + size / 2, insets.bottom + size - size / 3, insets.left + size, insets.top + size / 3);
////                }
////                if (orientation == SwingConstants.WEST) {
////                    // Right arrow
////                    g.drawLine(insets.left + size / 3, insets.bottom + size - size / 3, insets.left + size - size / 3, insets.top + size / 3);
////                    g.drawLine(insets.left + size / 2, insets.top + size / 3, insets.left + size, insets.bottom + size - size / 3);
////                }
////                if (orientation == SwingConstants.EAST) {
////                    // Left arrow
////                    g.drawLine(insets.left, insets.top + size / 3, insets.left + size / 2, insets.bottom + size - size / 3);
////                    g.drawLine(insets.left + size / 2, insets.bottom + size - size / 3, insets.left + size, insets.top + size / 3);
////                }
//            }
//        });
//
//        return button;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return d;
            }
        };
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.setColor(new Color(64, 64, 64));
        gg.fill(r);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        System.out.println(r);

        Color color;
        JScrollBar sb = (JScrollBar) c;
        if (!sb.isEnabled() || r.width > r.height) {
            return;
        } else if (isDragging) {
            color = new Color(72, 72, 72);
            gg.setPaint(color);
            gg.fillRect(r.x, r.y, r.width, r.height);

            Border border = new Border(1, 1, 1, 1);
            border.setPaint(new LinearGradientPaint(r.x, r.y, r.x, r.y + r.height, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
            border.paintBorder(c, g, r.x, r.y, r.width, r.height);
        } else if (isThumbRollover()) {
            color = new Color(160, 160, 160);
            gg.setPaint(color);
            gg.fillRect(r.x, r.y, r.width, r.height);

            Border border = new Border(2, 2, 2, 2);
            border.setPaint(new LinearGradientPaint(r.x, r.y, r.x, r.y + r.height, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
            border.paintBorder(c, g, r.x, r.y, r.width, r.height);
        } else {
            color = new Color(128, 128, 128);
            gg.setPaint(color);
            gg.fillRect(r.x, r.y, r.width, r.height);

            Border border = new Border(0, 0, 0, 2);
            border.setPaint(new LinearGradientPaint(r.x, r.y, r.x, r.y + r.height, new float[]{0f, 1f}, new Color[]{new Color(0, 128, 255), new Color(0, 255, 128)}));
            border.paintBorder(c, g, r.x, r.y, r.width, r.height);
        }

        gg.setColor(color.brighter());

        if (orientation == Orientation.VERTICAL) {
            gg.drawLine(r.x + 2, r.y + r.height / 2 - 4, r.x + r.width - 2, r.y + r.height / 2 - 4);
            gg.drawLine(r.x + 2, r.y + r.height / 2, r.x + r.width - 2, r.y + r.height / 2);
            gg.drawLine(r.x + 2, r.y + r.height / 2 + 4, r.x + r.width - 2, r.y + r.height / 2 + 4);
        } else {
            gg.drawLine(r.x + r.width / 2 - 4, r.y + 2, r.x + r.width / 2 - 4, r.y + r.height - 2);
            gg.drawLine(r.x + r.width / 2, r.y + 2, r.x + r.width / 2, r.y + r.height - 2);
            gg.drawLine(r.x + r.width / 2 + 4, r.y + 2, r.x + r.width / 2 + 4, r.y + r.height - 2);
        }

        gg.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);

        if (scrollbar.getIgnoreRepaint()) scrollbar.setIgnoreRepaint(false);
        scrollbar.repaint();
//        scrollbar.repaint(0);
//        scrollbar.invalidate();
//        scrollbar.tickUI();
//        scrollbar.revalidate();
//        scrollbar.invalidate();
//        scrollbar.tickUI();
//        scrollbar.paint(scrollbar.getGraphics());
    }
}
