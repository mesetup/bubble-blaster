package com.qtech.bubbles.gui;

import com.qtech.bubbles.BubbleBlaster;
import javafx.geometry.Orientation;

import javax.swing.*;
import java.awt.*;

public class SavesDisplay extends JScrollPane {
    private final int currentY = 0;
    private Rectangle innerBounds;
    private Dimension preferredSize;

    public SavesDisplay() {
        super();
        this.getVerticalScrollBar().setUI(new ScrollBarUI(Orientation.VERTICAL));
        this.getHorizontalScrollBar().setUI(new ScrollBarUI(Orientation.HORIZONTAL));
        this.setOpaque(true);
        this.setBackground(new Color(64, 64, 64));
    }

//    @Override
//    public JViewport getViewport() {
//        JViewport viewport = super.getViewport();
//        try {
//            viewport.setBounds(savesPanel.getBounds());
//            System.out.println(savesPanel.getBounds());
//        } catch (NullPointerException ignored) {
//
//        }
//
//        return viewport;
//    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, BubbleBlaster.getInstance().getHeight());
    }

    public static class SavesPanel extends JPanel {
        private int currentY;

//        public SavesPanel(LayoutManager layout) {
//            super(layout);
////            initializeContents();
//        }

        public SavesPanel() {
            super();
//            initializeContents();
        }

//        protected void initializeContents() {
////            this.setOpaque(true);
//            this.setBackground(new Color(64, 64, 64));
////            setSize(new Dimension(800, 300));
//        }

//        @Override
//        public Component add(Component comp) {
//            comp.setLocation(0, currentY);
//            currentY += comp.getHeight() + 1;
//
//            Component component = super.add(comp);
//            setSize(800, currentY);
//
//            return component;
//        }

//        @Override
//        public Dimension getPreferredSize() {
//            return new Dimension(800, currentY);
//        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void paintComponent(Graphics g) {
            int margin = 0;
            Dimension dim = getSize();
            super.paintComponent(g);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(margin, margin, dim.width - margin * 2, dim.height - margin * 2);

//        System.out.println(Arrays.toString(getComponents()));
        }
    }
}
