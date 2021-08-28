package com.qtech.bubbles.gui;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.crash.CrashCategory;
import com.qtech.bubbles.common.crash.CrashReport;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.StackLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <h1>Window Class </h1>
 * <p>
 * This is just a <img src="https://www.iconfinder.com/icons/35640/download/png/16"></img>
 */
public class Window extends JXFrame {
    private static final long serialVersionUID = -240840600533728354L;
    private final GraphicsDevice fullscreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public Window(BubbleBlaster main, boolean fullscreen) {

        try {
//            this.setPreferredSize(new Dimension(width, height));
//            this.setMaximumSize(new Dimension(width, height));
//            this.setMinimumSize(new Dimension(width, height));
            this.setBackground(new Color(64, 64, 64));
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            if (fullscreen) {
                this.setBounds(bounds);
            } else {
                this.setBounds(new Rectangle((int) bounds.getCenterX() - 800 / 2, (int) bounds.getCenterY() - 450 / 2, 800, 450));
                this.setMaximumSize(new Dimension(1280, 640));
                this.setMinimumSize(new Dimension(1280, 640));
            }

//            this.setAlwaysOnTop(true);
            if (fullscreen) {
                this.setUndecorated(true);
            }
//            this.setVisible(true);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLayout(new StackLayout());
            this.getContentPane().setLayout(new StackLayout());
            this.setResizable(false);
            this.setMaximizedBounds(getBounds());
            this.setLocationRelativeTo(null);
            if (main != null) {
                this.getContentPane().add(main);
                this.setComponentZOrder(main, 1);
            }
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    if (main != null) {
                        main.shutdown();
                    }
                    System.exit(0);
                }
            });

            this.enableInputMethods(true);
            this.setVisible(true);
            GraphicsDevice device = GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getScreenDevices()[0];
            if (fullscreen) {
                device.setFullScreenWindow(this);
            }
//            this.setIdle(true);
            this.repaint();
            this.requestFocus();

            if (getBounds().getMinX() > getBounds().getMaxX() || getBounds().getMinY() > getBounds().getMaxY()) {
                throw new IllegalStateException("Window bounds is invalid: negative size");
            }

            if (getBounds().getMinX() == getBounds().getMaxX() || getBounds().getMinY() == getBounds().getMaxY()) {
                throw new IllegalStateException("Window bounds is invalid: zero size");
            }
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Could not startup QBubbles.", t);
            CrashCategory category = new CrashCategory("Startup failure");
            category.add("Window Size", this.getSize());
            category.add("Window Pos", this.getLocation());
            category.add("Window Pos-Screen", this.getLocationOnScreen());
            category.add("Window State", this.getState());
            category.add("Window Extended State", this.getExtendedState());
            crashReport.addCategory(category);
            throw crashReport.getReportedException();
        }

        try {
            if (main != null) {
                main.start(this);
            }
        } catch (Throwable t) {
            CrashReport crashReport = new CrashReport("Could not startup QBubbles.", t);
            CrashCategory category = new CrashCategory("Startup failure");
            category.add("Window Size", this.getSize());
            category.add("Window Pos", this.getLocation());
            category.add("Window Pos-Screen", this.getLocationOnScreen());
            category.add("Window State", this.getState());
            category.add("Window Extended State", this.getExtendedState());
            crashReport.addCategory(category);
            throw crashReport.getReportedException();
        }
    }

    public void toggleFullscreen() {
        this.dispose();

        if (this.isUndecorated()) {
            fullscreenDevice.setFullScreenWindow(null);
            this.setUndecorated(false);
        } else {
            this.setUndecorated(true);
            fullscreenDevice.setFullScreenWindow(this);
        }

        this.setVisible(true);
        this.repaint();
    }

    public boolean isFullscreen() {
        return this.isUndecorated();
    }

    public JXFrame getFrame() {
        return this;
    }

    public java.awt.Window getWindow() {
        return this;
    }
}
