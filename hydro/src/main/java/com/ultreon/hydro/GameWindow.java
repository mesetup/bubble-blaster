package com.ultreon.hydro;

import com.ultreon.commons.exceptions.OneTimeUseException;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.core.CursorManager;
import com.ultreon.hydro.event.bus.GameEvents;
import com.ultreon.hydro.event.window.WindowClosingEvent;
import com.ultreon.hydro.input.KeyInput;
import com.ultreon.hydro.input.MouseInput;
import org.jdesktop.swingx.JXFrame;
import org.jetbrains.annotations.Contract;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public class GameWindow {
    // GUI Elements.
    private final JXFrame wrap;
    final Canvas canvas;

    // AWT Toolkit.
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();

    // Graphics thingies.
    final ImageObserver observer;
    private final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final GraphicsDevice gd;
    private boolean initialized = false;
    private int fps;
    private CursorManager cursorManager;
    private Thread mainThread;

    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public GameWindow(Properties properties) {
        this.wrap = new JXFrame(properties.title);
        this.wrap.setPreferredSize(new Dimension(properties.width, properties.height));
        this.wrap.setSize(properties.width, properties.height);
        this.wrap.setResizable(false);
        this.wrap.enableInputMethods(true);
        this.wrap.setFocusTraversalKeysEnabled(false);
        this.gd = this.wrap.getGraphicsConfiguration().getDevice();

        this.canvas = new Canvas(this.wrap.getGraphicsConfiguration());
        this.canvas.enableInputMethods(true);
        this.canvas.setFocusTraversalKeysEnabled(false);

        this.observer = canvas::imageUpdate; // Didn't use canvas directly because of security reasons.

        GameWindow.this.canvas.setSize(properties.width, properties.height);

        this.wrap.add(this.canvas);

        this.wrap.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                GameWindow.this.canvas.setSize(e.getComponent().getSize());
            }
        });

        this.wrap.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.wrap.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                boolean cancelClose = GameEvents.get().publish(new WindowClosingEvent(GameWindow.this));
                if (!cancelClose) {
                    game().close();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        this.wrap.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                GameWindow.this.canvas.requestFocus();
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        this.canvas.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                GameWindow.this.canvas.requestFocus();
            }
        });

        canvas.requestFocus();
    }

    public synchronized void init() {
        if (initialized) {
            throw new OneTimeUseException("The game window is already initialized.");
        }

        this.canvas.setVisible(true);

//        if (this.canvas.getBounds().getMinX() > this.canvas.getBounds().getMaxX() ||
//                this.canvas.getBounds().getMinY() > this.canvas.getBounds().getMaxY()) {
//            this.canvas.setBounds(this.wrap.getBounds());
//        }
//
//        if (this.canvas.getBounds().getMinX() == this.canvas.getBounds().getMaxX() ||
//                this.canvas.getBounds().getMinY() == this.canvas.getBounds().getMaxY()) {
//            this.canvas.setBounds(this.wrap.getBounds());
//        }
//
//        if (this.canvas.getBounds().getMinX() > this.canvas.getBounds().getMaxX() ||
//                this.canvas.getBounds().getMinY() > this.canvas.getBounds().getMaxY()) {
//            throw new IllegalStateException("Game bounds is invalid: negative size");
//        }
//
//        if (this.canvas.getBounds().getMinX() == this.canvas.getBounds().getMaxX() ||
//                this.canvas.getBounds().getMinY() == this.canvas.getBounds().getMaxY()) {
//            throw new IllegalStateException("Game bounds is invalid: zero size");
//        }

        this.wrap.setVisible(true);

        KeyInput.init();
        MouseInput.init();

        KeyInput.listen(this.wrap);
        MouseInput.listen(this.wrap);

        KeyInput.listen(this.canvas);
        MouseInput.listen(this.canvas);

        this.initialized = true;

        System.out.println("Initialized game window");

        game().windowLoaded();
    }

    void close() {
        this.wrap.dispose(); // Window#dispose() closes the awt-based window.
        System.exit(0);
    }

    public Cursor registerCursor(int hotSpotX, int hotSpotY, ResourceEntry resourceEntry) {
        ResourceEntry textureEntry = new ResourceEntry(resourceEntry.namespace(), "textures/cursors/" + resourceEntry.path());
        Image image;
        try (InputStream assetAsStream = game().getResourceManager().getAssetAsStream(textureEntry)) {
            image = ImageIO.read(assetAsStream);
        } catch (IOException e) {
            throw new IOError(e);
        }

        return toolkit.createCustomCursor(image, new Point(hotSpotX, hotSpotY), resourceEntry.toString());
    }

    private Game game() {
        return Game.getInstance();
    }

    public void toggleFullscreen() {
        if (isFullscreen()) {
            gd.setFullScreenWindow(null);
        } else {
            gd.setFullScreenWindow(this.wrap);
        }
    }

    public void setFullscreen(boolean fullscreen) {
        if (isFullscreen() && !fullscreen) { // If currently not fullscreen and disabling fullscreen.
            gd.setFullScreenWindow(null); // Set fullscreen to false.
        } else if (!isFullscreen() && fullscreen) { // If currently in fullscreen and enabling fullscreen.
            gd.setFullScreenWindow(this.wrap); // Set fullscreen to true.
        }
    }

    public boolean isFullscreen() {
        return gd.getFullScreenWindow() == this.wrap;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Rectangle getBounds() {
        return this.wrap.getBounds();
    }

    public int getWidth() {
        return this.wrap.getWidth();
    }

    public int getHeight() {
        return this.wrap.getHeight();
    }

    public int getX() {
        return this.wrap.getX();
    }

    public int getY() {
        return this.wrap.getY();
    }

    public void setCursor(Cursor defaultCursor) {
        if (cursorManager == null) return;
        cursorManager.setCursor(this.wrap, defaultCursor);
    }

    public void requestFocus() {
        this.wrap.requestFocus();
        this.canvas.requestFocus();
    }

    public boolean isFocused() {
        return this.wrap.isFocused();
    }

    public void requestUserAttention() {
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
                taskbar.requestWindowUserAttention(this.wrap);
            }
        }
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public static class Properties {
        private final int width;
        private final int height;
        private final String title;
        private boolean fullscreen;

        public Properties(String title, int width, int height) {
            this.width = width;
            this.height = height;
            this.title = title;
        }

        @Contract("->this")
        public Properties fullscreen() {
            this.fullscreen = true;
            return this;
        }
    }
}
