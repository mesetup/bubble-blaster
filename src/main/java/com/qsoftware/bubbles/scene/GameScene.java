package com.qsoftware.bubbles.scene;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.GraphicsProcessor;
import com.qsoftware.bubbles.common.entity.Attribute;
import com.qsoftware.bubbles.common.entity.DamageSource;
import com.qsoftware.bubbles.common.entity.DamageSourceType;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.random.PseudoRandom;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.common.scene.SceneManager;
import com.qsoftware.bubbles.common.screen.Screen;
import com.qsoftware.bubbles.core.common.SavedGame;
import com.qsoftware.bubbles.core.controllers.KeyboardController;
import com.qsoftware.bubbles.core.utils.categories.GraphicsUtils;
import com.qsoftware.bubbles.core.utils.categories.ShapeUtils;
import com.qsoftware.bubbles.environment.Environment;
import com.qsoftware.bubbles.event.*;
import com.qsoftware.bubbles.event.bus.EventBus;
import com.qsoftware.bubbles.event.type.KeyEventType;
import com.qsoftware.bubbles.event.type.MouseEventType;
import com.qsoftware.bubbles.init.ScreenInit;
import com.qsoftware.bubbles.init.StateInit;
import com.qsoftware.bubbles.media.AudioSlot;
import com.qsoftware.bubbles.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <h1>Game scene.</h1>
 * <p>
 * For the {@code params} in the {@link SceneManager#displayScene(Scene)}, use:
 *
 * <ul>
 *  <il><b>a):</b> Key: <i><code>"gameType"</code></i> Value: <i>a {@link AbstractGameType} instance</i>.</il>
 * </ul>
 *
 * @see AbstractGameType
 * @see Scene
 */
@SuppressWarnings("unused")
public class GameScene extends Scene {
    // Fonts.
    private final Font defaultFont = new Font(Util.getGame().getPixelFontName(), Font.PLAIN, 32);

    // Types
    @Getter
    private final AbstractGameType gameType;
    @Getter
    private final Environment environment;

    // Threads.
    @Getter
    private Thread autoSaveThread;
    @Getter
    private Thread collisionThread;
    @Getter
    private Thread ambientAudioThread;

    // Files / folders.
    private File saveDir;

    // Active messages.
    private final ArrayList<String> activeMessages = new ArrayList<>();
    private final ArrayList<Long> activeMsgTimes = new ArrayList<>();

    // Current screen.
    @Getter
    private Screen<?> currentScreen;

    // Audio.
    private AudioSlot ambientAudio;
    private long nextAudio;

    // Flags.
    @SuppressWarnings("FieldCanBeLocal")
    @Getter
    @Setter
    private boolean paused = false;
    private boolean eventsActive = false;

    // Event
    private EventBus.Handler binding;

    // Save
    @Getter
    private final SavedGame savedGame;

    public GameScene(SavedGame savedGame, Environment environment) {
        this.savedGame = savedGame;
        this.gameType = environment.getGameType();
        this.environment = environment;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Show and Hide     //
    ///////////////////////////
    @Override
    public void showScene() {
        Util.setCursor(QBubbles.getInstance().getBlankCursor());

        bindEvents();

        this.environment.getGameType().start();

        this.autoSaveThread = new Thread(this::autoSaveThread, "AutoSaver");
        this.autoSaveThread.start();

        this.collisionThread = new Thread(this::collisionThread, "Collision");
        this.collisionThread.start();

        this.ambientAudioThread = new Thread(this::ambientAudioThread, "Collision");
        this.ambientAudioThread.start();
    }

    @Override
    public boolean hideScene(Scene to) {
        try {
            // Unbind events.
            gameType.unbindEvents();
            unbindEvents();

            try {
                autoSaveThread.join();
                collisionThread.join();
                ambientAudioThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Hide cursor.
            Util.setCursor(QBubbles.getInstance().getDefaultCursor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.hideScene(to);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Pause methods     //
    ///////////////////////////
    public void pause() {
        this.paused = true;
        QBubbles.getEventBus().post(new PauseEvent(QBubbles.getInstance(), true));
    }

    public void unpause() {
        this.paused = false;
        QBubbles.getEventBus().post(new PauseEvent(QBubbles.getInstance(), false));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Screen control methods     //
    ////////////////////////////////////
    public void closeCurrentScreen() {
        this.currentScreen.closeScreen();
        this.currentScreen = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Thread methods     //
    ////////////////////////////
    private void ambientAudioThread() {
//        Toolkit toolkit = Toolkit.getToolkit();

        while (this.eventsActive) {
            if (this.ambientAudio == null) {
                if (!this.gameType.isBloodMoonActive() && this.nextAudio < System.currentTimeMillis()) {
                    if (new PseudoRandom(System.nanoTime()).getNumber(0, 5, -1) == 0) {
                        try {
                            this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/bgm/art_of_silence.mp3")), "artOfSilence");
                            this.ambientAudio.setVolume(0.25d);
                            this.ambientAudio.play();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            QBubbles.getInstance().shutdown();
                        }
                    } else {
                        this.nextAudio = System.currentTimeMillis() + 1000;
                    }
                } else if (this.gameType.isBloodMoonActive()) {
                    try {
                        this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/bgm/blood_moon_state.mp3")), "bloodMoonState");
                        this.ambientAudio.setVolume(0.25d);
                        this.ambientAudio.play();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        QBubbles.getInstance().shutdown();
                    }
                }
            } else if (this.ambientAudio.isStopped()) {
                this.ambientAudio = null;
            } else if (!this.ambientAudio.getClip().isPlaying()) {
                this.ambientAudio.stop();
                this.ambientAudio = null;
            } else if (this.gameType.isBloodMoonActive() && !this.ambientAudio.getName().equals("bloodMoonState")) {
                this.ambientAudio.stop();
                this.ambientAudio = null;

                try {
                    this.ambientAudio = new AudioSlot(getClass().getResource("/assets/qbubbles/audio/bgm/blood_moon_state.mp3"), "bloodMoonState");
                    this.ambientAudio.setVolume(0.25d);
                    this.ambientAudio.play();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    QBubbles.getInstance().shutdown();
                }
            }
        }
    }

    private void collisionThread() {
        // Initiate variables for game loop.
        long lastTime = System.nanoTime();
        double amountOfUpdates = 30.0;
        double ns = 1000000000 / amountOfUpdates;
        @SuppressWarnings({"unused", "RedundantSuppression"}) double delta = 0;
        @SuppressWarnings({"unused", "RedundantSuppression"}) long timer = System.currentTimeMillis();
        @SuppressWarnings({"unused", "RedundantSuppression"}) int frames = 0;

        while (this.eventsActive) {
            // Calculate tick delta-time.
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            double deltaTime = (now - lastTime) / ns;
            lastTime = now;

            List<Entity> entities = this.environment.getEntities();
            handleCollision(entities, deltaTime);

            if (entities.isEmpty()) {
                System.out.println("ERROR");
            }

            if (gameType.isInitialized()) {
                List<Entity> entityList = entities;
                for (int i = 0; i < entityList.size(); i++) {
                    Entity entity = entityList.get(i);
                    entity.tick(environment);

                    @Nullable Scene scene = QBubbles.getInstance().getSceneManager().getCurrentScene();

                    if (scene instanceof GameScene) {
                        GameScene gameScene = (GameScene) scene;

                        if (!gameScene.isPaused()) {
                            for (int j = i; j < entities.size(); j++) {
                                Entity target = entities.get(j);
                                try {
                                    // Check is collisionable with each other
                                    if (entity.isCollidingWith(target)) {

                                        // Check intersection.
                                        if (ShapeUtils.checkIntersection(entity.getShape(), target.getShape())) {

                                            // Handling collision by posting collision event, and let the intersected entities attack each other.
                                            QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, entity, target));
                                            QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, target, entity));

                                            gameType.attack(target, entity.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(entity, DamageSourceType.COLLISION));
                                            gameType.attack(entity, target.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(target, DamageSourceType.COLLISION));
                                        }
                                    }
                                } catch (ArrayIndexOutOfBoundsException exception) {
                                    QBubbles.getLogger().info("Array index was out of bounds! Check check double check!");
                                }
                            }
                        }
                    }
                }
                gameType.tick();
            }
        }

        QBubbles.getLogger().info("Collision thread will die now.");
    }

    private void handleCollision(List<Entity> entities, double deltaTime) {
        // Loop all game objects twice (because there'll be checked for intersection between 2 entities..

        List<Entity> entityList = this.environment.getEntities();

        @Nullable Scene scene = QBubbles.getInstance().getSceneManager().getCurrentScene();
        if (scene instanceof GameScene) {
            GameScene gameScene = (GameScene) scene;

            if (!gameScene.isPaused()) {
                for (int i = 0; i < entityList.size(); i++) {
                    Entity entity = entityList.get(i);

                    for (int j = i; j < entities.size(); j++) {
                        Entity target = entities.get(j);
                        try {
                            // Check is collisionable with each other
                            if (entity.isCollidingWith(target)) {

                                // Check intersection.
                                if (ShapeUtils.checkIntersection(entity.getShape(), target.getShape())) {

                                    // Handling collision by posting collision event, and let the intersected entities attack each other.
                                    QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, entity, target));
                                    QBubbles.getEventBus().post(new CollisionEvent(QBubbles.getInstance(), gameScene, 1, target, entity));

                                    gameType.attack(target, entity.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(entity, DamageSourceType.COLLISION));
                                    gameType.attack(entity, target.getAttributeMap().get(Attribute.ATTACK) * 1 / 2, new DamageSource(target, DamageSourceType.COLLISION));
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException exception) {
                            QBubbles.getLogger().info("Array index was out of bounds! Check check double check!");
                        }
                    }
                }
            }
        }
    }

    private void autoSaveThread() {
        long nextSave = System.currentTimeMillis();
        while (!this.gameType.isGameOver()) {
//            System.out.println(nextSave - System.currentTimeMillis());
            if (nextSave - System.currentTimeMillis() < 0) {
                QBubbles.getLogger().info("Auto Saving...");
                onAutoSave();
                nextSave = System.currentTimeMillis() + 30000;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                QBubbles.getLogger().warn("Could not sleep thread.");
            }
        }
        QBubbles.getLogger().debug("Stopping AutoSaveThread...");
    }

    private synchronized void onAutoSave() {
        this.gameType.dumpSaveData(savedGame);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Event Binding     //
    ///////////////////////////
    @Override
    public void bindEvents() {
        QBubbles.getEventBus().register(this);
        this.eventsActive = true;
    }

    @Override
    public void unbindEvents() {
        this.binding.unbind();
        this.eventsActive = false;
    }

    @Override
    public boolean eventsAreActive() {
        return this.eventsActive;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Getter-Only Properties     //
    ////////////////////////////////////
    public AudioSlot getAmbientAudio() {
        return this.ambientAudio;
    }

    public File getSaveDir() {
        return this.saveDir;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Trigger Methods     //
    /////////////////////////////
    public void triggerMessage(String s) {
        this.activeMessages.add(s);
        this.activeMsgTimes.add(System.currentTimeMillis() + 3000);
    }

    @Deprecated
    public synchronized void triggerGameOver() {
        if (!this.gameType.isGameOver()) {
            this.gameType.setResultScore(Math.round(this.gameType.getPlayer().getScore()));
            this.gameType.triggerGameOver();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Render Methods     //
    ////////////////////////////

    /**
     * Render event
     *
     * @param game the game.
     * @param gg   the graphics instance.
     */
    @Override
    public void renderGUI(QBubbles game, Graphics2D gg) {
        this.gameType.renderGUI(game, gg);

        if (this.currentScreen != null) this.currentScreen.renderGUI(game, new GraphicsProcessor(gg));
    }

    @Override
    public void render(QBubbles game, Graphics2D gg) {
        if (this.gameType.isBloodMoonActive()) {
            gg.setColor(StateInit.BLOOD_MOON_STATE.getBackgroundColor());
            gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
        }
        if (this.gameType.isInitialized()) {
            this.gameType.render(gg);
        }
        this.renderHUD(game, gg);
    }

    public synchronized void renderHUD(@SuppressWarnings({"unused", "RedundantSuppression"}) QBubbles game, Graphics2D gg) {
        int i = 0;
        for (String s : activeMessages) {
            Graphics2D gg2 = (Graphics2D) gg.create(0, 71 + (32 * i), 1000, 32);

            gg2.setColor(new Color(0, 0, 0, 128));
            gg2.fillRect(0, 0, 1000, 32);

            gg2.setColor(new Color(255, 255, 255, 255));
            GraphicsUtils.drawLeftAnchoredString(gg2, s, new Point2D.Double(2, 2), 28, defaultFont);

            gg2.dispose();
            i++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Events     //
    ////////////////////

    /**
     * <h1>Update Event-Handler</h1>
     * Update event handler for the game scene. Used for collision detection.
     *
     * @param evt the tick event.
     */
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onUpdate(TickEvent evt) {
        @SuppressWarnings({"unused", "RedundantSuppression"}) ArrayList<String> messageCopy = (ArrayList<String>) this.activeMessages.clone();
        @SuppressWarnings({"unused", "RedundantSuppression"}) ArrayList<Long> timeCopy = (ArrayList<Long>) this.activeMsgTimes.clone();

        for (int i = 0; i < this.activeMessages.size(); i++) {
            if (this.activeMsgTimes.get(i) < System.currentTimeMillis()) {
                this.activeMsgTimes.remove(i);
                this.activeMessages.remove(i);
                i--;
            }
        }
    }

    /**
     * <h1>Keyboard Event-Handler</h1>
     * Keyboard event-handler, handles screen changing and debug-mode activations.
     *
     * @param evt the keyboard event.
     */
    @SubscribeEvent
    public void onKeyboard(KeyboardEvent evt) {
        if (evt.getType() == KeyEventType.PRESS) {
            if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!isPaused()) {
                    Screen<?> screen = ScreenInit.PAUSE_SCREEN.get().getScreen(this);
                    screen.openScreen();
                    this.currentScreen = screen;
                }
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_SLASH) {
                if (!isPaused()) {
                    Screen<?> screen = ScreenInit.COMMAND_SCREEN.get().getScreen(this);
                    screen.openScreen();
                    this.currentScreen = screen;
                }
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F1 && QBubbles.isDebug()) {
                this.gameType.triggerBloodMoon();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F3 && QBubbles.isDebug()) {
                this.gameType.getPlayer().instantDeath();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F4 && QBubbles.isDebug()) {
                this.gameType.getPlayer().levelUp();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F5 && QBubbles.isDebug()) {
                this.gameType.getPlayer().addScore(1000);
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F6 && QBubbles.isDebug()) {
                this.gameType.getPlayer().setHealth(this.gameType.getPlayer().getMaxHealth());
            }
        } else if (evt.getType() == KeyEventType.TYPE) {
            if (evt.getKeyChar() == ' ') {
                if (this.gameType.isGameOver()) {
                    Util.getSceneManager().displayScene(new GameOverScene(gameType.getResultScore()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent evt) {
        if (evt.getType() == MouseEventType.CLICK) {
            if (evt.getParentEvent().getButton() == 1) {
                if (KeyboardController.instance().isPressed(KeyEvent.VK_F1)) {
                    this.gameType.getPlayer().teleport(evt.getParentEvent().getPoint());
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Utility methods     //
    /////////////////////////////

    /**
     * <h1>Draw bubble.</h1>
     * Draws bubble on screen.
     *
     * @param g      the graphics-2D instance
     * @param x      the x-coordinate.
     * @param y      the y-coordinate.
     * @param radius the bubble radius (full width.).
     * @param colors the bubble colors (on sequence).
     */
    public static void drawBubble(Graphics2D g, int x, int y, int radius, Color... colors) {

        // Define ellipse-depth (pixels).
        double i = 0f;

        // Loop colors.
        for (Color color : colors) {
            // Set stroke width.
            if (i == 0) {
                if (colors.length > 1) {
                    g.setStroke(new BasicStroke(2.2f));
                } else {
                    g.setStroke(new BasicStroke(2.0f));
                }
            } else if (i == colors.length - 1) {
                g.setStroke(new BasicStroke(2.0f));
            } else {
                g.setStroke(new BasicStroke(2.2f));
            }

            // Set color.
            g.setColor(color);

            // Draw ellipse.
            Ellipse2D ellipse = getEllipse(x, y, radius, i);
            g.draw(ellipse);

            // Add 2 to ellipse-depth (pixels).
            i += 2f;
        }
    }

    /**
     * <h1>Get Ellipse</h1>
     * Get ellipse from x, y, radius, delta-radius and delta-value.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param r the radius.
     * @param i the delta-radius.
     * @return the ellipse.
     */
    protected static Ellipse2D getEllipse(double x, double y, double r, double i) {
        return new Ellipse2D.Double(x + i, y + i, r - i * 2f, r - i * 2f);
    }

    @Override
    public void tick() {
        this.gameType.tick();
    }
}
