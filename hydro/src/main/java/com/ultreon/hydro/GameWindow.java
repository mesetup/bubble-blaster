package com.ultreon.hydro;

import com.ultreon.commons.crash.CrashReport;
import com.ultreon.commons.crash.GameCrash;
import com.ultreon.commons.exceptions.OneTimeUseException;
import com.ultreon.commons.time.TimeProcessor;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.core.CursorManager;
import com.ultreon.hydro.event.WindowClosingEvent;
import com.ultreon.hydro.event.bus.GameEventBus;
import com.ultreon.hydro.input.KeyboardController;
import com.ultreon.hydro.input.MouseController;
import com.ultreon.hydro.render.Renderer;
import org.jdesktop.swingx.JXFrame;
import org.jetbrains.annotations.Contract;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public class GameWindow {
    // GUI Elements.
    private final JXFrame wrap;
    private final Canvas canvas;

    // AWT Toolkit.
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();

    // Graphics thingies.
    final ImageObserver observer;
    private final int tps;
    private final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final GraphicsDevice gd;
    private boolean initialized = false;
    private volatile boolean running = false;
    private int fps;
    private final List<Runnable> tasks = new CopyOnWriteArrayList<>();
    private CursorManager cursorManager;
    private Thread mainThread;

    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public GameWindow(Properties properties) {
        this.wrap = new JXFrame(properties.title);
        this.wrap.setPreferredSize(new Dimension(properties.width, properties.height));
        this.wrap.setSize(properties.width, properties.height);
        this.wrap.setResizable(false);
        this.wrap.enableInputMethods(true);
        this.gd = this.wrap.getGraphicsConfiguration().getDevice();

        this.tps = properties.tps;

        this.canvas = new Canvas(this.wrap.getGraphicsConfiguration());

        this.observer = canvas::imageUpdate; // Didn't use canvas directly because of security reasons.

        this.canvas.addKeyListener(KeyboardController.instance());
        this.canvas.addMouseListener(MouseController.instance());
        this.canvas.addMouseMotionListener(MouseController.instance());
        this.canvas.addMouseWheelListener(MouseController.instance());

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
                boolean cancelClose = GameEventBus.get().post(new WindowClosingEvent(GameWindow.this));
                if (!cancelClose) {
                    shutdown();
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
    }

    @SuppressWarnings({"Convert2Lambda"})
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
        this.initialized = true;

        System.out.println("Initialized game window");

        this.mainThread = new Thread(new Runnable() {
            public void run() {
                running = true;
                GameWindow.this.mainThread();
            }
        }, "TickThread");
        this.mainThread.start();
    }

    private void mainThread() {
        double tickCap = 1d / (double) tps;
        double frameTime = 0d;
        double frames = 0;

        double time = TimeProcessor.getTime();
        double unprocessed = 0;

        try {
            while (running) {
                boolean canTick = false;

                double time2 = TimeProcessor.getTime();
                double passed = time2 - time;
                unprocessed += passed;
                frameTime += passed;

                time = time2;

                while (unprocessed >= tickCap) {
                    unprocessed -= tickCap;

                    canTick = true;
                }

                if (canTick) {
                    try {
                        tick();
                    } catch (Throwable t) {
                        CrashReport crashReport = new CrashReport("Game being ticked.", t);
                        throw crashReport.getReportedException();
                    }
                }

                if (frameTime >= 1.0d) {
                    frameTime = 0;
                    fps = (int) Math.round(frames);
                    frames = 0;
                }

                frames++;

                try {
                    render(fps);
                } catch (Throwable t) {
                    CrashReport crashReport = new CrashReport("Game being rendered.", t);
                    throw crashReport.getReportedException();
                }

                for (Runnable task : tasks) {
                    task.run();
                }

                tasks.clear();
            }
        } catch (GameCrash e) {
            e.printStackTrace();

            Game.instance.crash(e);
//            showScreen(new CrashScreen(e.getCrashReport()), true);
            while (running) Thread.onSpinWait();
        }

        close();
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

    public void scheduleTask(Runnable task) {
        this.tasks.add(task);
    }

    /**
     * Update method, for updating values and doing things.
     *
     */
    @SuppressWarnings("SameParameterValue")
    private void tick() {
        game().tick0();
    }

    /**
     * Render method, for rendering window.
     * @param fps current game framerate.
     */
    private void render(int fps) {
        if (!initialized) return;

        // Buffer strategy (triple buffering).
        BufferStrategy bs = this.canvas.getBufferStrategy();

        // Create buffers if not created yet.
        if (bs == null) {
            this.canvas.createBufferStrategy(2);
            bs = this.canvas.getBufferStrategy();
//            return;
        }

        // Get GraphicsProcessor and GraphicsProcessor objects.
        Renderer renderer = new Renderer(bs.getDrawGraphics());

        game().render0(renderer, fps);

        // Dispose and show.
        renderer.dispose();

        try {
            bs.show();
        } catch (IllegalStateException e) {
            close();
        }
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

    public void shutdown() {
        this.running = false;
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public static class Properties {
        private final int width;
        private final int height;
        private final String title;
        private boolean fullscreen;
        private int tps = 20;

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

        @Contract("_->this")
        public Properties tps(int tps) {
            this.tps = tps;
            return this;
        }
    }
}
