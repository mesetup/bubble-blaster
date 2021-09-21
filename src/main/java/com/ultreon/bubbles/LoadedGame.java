package com.ultreon.bubbles;

import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.common.random.PseudoRandom;
import com.ultreon.bubbles.entity.DamageableEntity;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.bubbles.entity.damage.DamageSource;
import com.ultreon.bubbles.entity.damage.DamageSourceType;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.event.EntityCollisionEvent;
import com.ultreon.bubbles.media.AudioSlot;
import com.ultreon.bubbles.save.SavedGame;
import com.ultreon.bubbles.screen.CommandScreen;
import com.ultreon.bubbles.screen.GameOverScreen;
import com.ultreon.bubbles.screen.PauseScreen;
import com.ultreon.bubbles.util.Util;
import com.ultreon.commons.util.ShapeUtils;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.input.MouseEvent;
import com.ultreon.hydro.event.type.KeyEventType;
import com.ultreon.hydro.event.type.MouseEventType;
import com.ultreon.hydro.input.KeyInput;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.util.GraphicsUtils;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadedGame implements Runnable {

    // Fonts.
    private final Font defaultFont = new Font(Util.getGame().getPixelFontName(), Font.PLAIN, 32);

    // Types
    @Getter
    private final AbstractGameType gameType;
    @Getter
    private final Environment environment;

    // Threads.
    private Thread autoSaveThread;
    private Thread collisionThread;
    private Thread ambientAudioThread;
    private Thread gameEventHandlerThread;

    // Files / folders.
    private final File saveDir;

    // Active messages.
    private final ArrayList<String> activeMessages = new ArrayList<>();
    private final ArrayList<Long> activeMsgTimes = new ArrayList<>();

    // Audio.
    private AudioSlot ambientAudio;
    private long nextAudio;

    // Flags.
    @SuppressWarnings("FieldCanBeLocal")
    private boolean gameActive = false;

    // Save
    @Getter
    private final SavedGame savedGame;
    private GameEvent currentGameEvent;

    public LoadedGame(SavedGame savedGame, Environment environment) {
        this.savedGame = savedGame;
        this.gameType = environment.getGameType();
        this.environment = environment;
        this.saveDir = savedGame.getDirectory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Show and Hide     //
    ///////////////////////////
    public void run() {
        Util.setCursor(BubbleBlaster.instance().getBlankCursor());

        bindEvents();

        this.environment.getGameType().start();

        this.autoSaveThread = new Thread(this::autoSaveThread, "AutoSaver");
        this.autoSaveThread.start();

        this.collisionThread = new Thread(this::collisionThread, "Collision");
        this.collisionThread.start();

        this.ambientAudioThread = new Thread(this::ambientAudioThread, "Collision");
        this.ambientAudioThread.start();
    }

    @SuppressWarnings("deprecation")
    public void quit() {
        this.environment.quit();

        try {
            // Unbind events.
            gameType.destroy();
            unbindEvents();

            if (autoSaveThread != null) autoSaveThread.interrupt();
            if (collisionThread != null) collisionThread.interrupt();
            if (ambientAudioThread != null) ambientAudioThread.interrupt();
            if (gameEventHandlerThread != null) gameEventHandlerThread.interrupt();

            environment.quit();

            // Hide cursor.
            Util.setCursor(BubbleBlaster.instance().getDefaultCursor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.gc();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Pause methods     //
    ///////////////////////////
    @Deprecated
    public void pause() {

    }

    @Deprecated
    public void unpause() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Thread methods     //
    ////////////////////////////
    private void ambientAudioThread() {
//        Toolkit toolkit = Toolkit.getToolkit();

        while (this.gameActive) {
            if (this.ambientAudio == null) {
                if (!this.gameType.isBloodMoonActive() && this.nextAudio < System.currentTimeMillis()) {
                    if (new PseudoRandom(System.nanoTime()).getNumber(0, 5, -1) == 0) {
                        try {
                            this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/bubbleblaster/audio/bgm/art_of_silence.mp3")), "artOfSilence");
                            this.ambientAudio.setVolume(0.25d);
                            this.ambientAudio.play();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            BubbleBlaster.instance().shutdown();
                        }
                    } else {
                        this.nextAudio = System.currentTimeMillis() + 1000;
                    }
                } else if (this.gameType.isBloodMoonActive()) {
                    try {
                        this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/bubbleblaster/audio/bgm/blood_moon_state.mp3")), "bloodMoonState");
                        this.ambientAudio.setVolume(0.25d);
                        this.ambientAudio.play();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        BubbleBlaster.instance().shutdown();
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
                    this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/bubbleblaster/audio/bgm/blood_moon_state.mp3")), "bloodMoonState");
                    this.ambientAudio.setVolume(0.25d);
                    this.ambientAudio.play();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    BubbleBlaster.instance().shutdown();
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

        while (this.gameActive) {
            // Calculate tick delta-time.
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            java.util.List<Entity> entities = this.environment.getEntities();

            if (entities.isEmpty()) {
                System.out.println("ERROR");
            }

            List<Entity> loopingEntities = new ArrayList<>(entities);

            if (gameType.isInitialized()) {
                if (!BubbleBlaster.isPaused()) {
                    for (int i = 0; i < loopingEntities.size(); i++) {
                        for (int j = i + 1; j < loopingEntities.size(); j++) {
                            Entity source = loopingEntities.get(i);
                            Entity target = loopingEntities.get(j);
                            try {
                                // Check is collisionable with each other
                                if (source.isCollidingWith(target)) {

                                    // Check intersection.
                                    if (ShapeUtils.checkIntersection(source.getShape(), target.getShape())) {
                                        // Handling collision by posting collision event, and let the intersected entities attack each other.
                                        BubbleBlaster.getEventBus().publish(new EntityCollisionEvent(BubbleBlaster.instance(), delta, source, target));
                                        BubbleBlaster.getEventBus().publish(new EntityCollisionEvent(BubbleBlaster.instance(), delta, target, source));

                                        if (target instanceof DamageableEntity) {
                                            ((DamageableEntity) target).damage(source.getAttributeMap().getBase(Attribute.ATTACK) * delta / target.getAttributeMap().getBase(Attribute.DEFENSE), new DamageSource(source, DamageSourceType.COLLISION));
                                        }
                                        if (source instanceof DamageableEntity) {
                                            ((DamageableEntity) source).damage(target.getAttributeMap().getBase(Attribute.ATTACK) * delta / source.getAttributeMap().getBase(Attribute.DEFENSE), new DamageSource(target, DamageSourceType.COLLISION));
                                        }
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                BubbleBlaster.getLogger().info("Array index was out of bounds! Check check double check!");
                            }
                        }
                    }
                }
            }
            gameType.tick();
        }

        BubbleBlaster.getLogger().info("Collision thread will die now.");
    }

    private void autoSaveThread() {
        long nextSave = System.currentTimeMillis();
        while (!this.gameType.isGameOver()) {
//            System.out.println(nextSave - System.currentTimeMillis());
            if (nextSave - System.currentTimeMillis() < 0) {
                BubbleBlaster.getLogger().info("Auto Saving...");
                onAutoSave();
                nextSave = System.currentTimeMillis() + 30000;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                BubbleBlaster.getLogger().warn("Could not sleep thread.");
            }
        }
        BubbleBlaster.getLogger().debug("Stopping AutoSaveThread...");
    }

    private void onAutoSave() {
        this.gameType.dumpSaveData(savedGame);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Event Binding     //
    ///////////////////////////
    public void bindEvents() {
        BubbleBlaster.getEventBus().subscribe(this);
        this.gameActive = true;
    }

    public void unbindEvents() {
        this.gameActive = false;
    }

    public boolean eventsAreActive() {
        return this.gameActive;
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

    /**
     * Triggers game over.
     *
     * @see AbstractGameType#triggerGameOver()
     * @since 1.0.0
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    public void triggerGameOver() {
        if (!this.gameType.isGameOver()) {
            this.gameType.setResultScore(Math.round(Objects.requireNonNull(this.gameType.getPlayer()).getScore()));
            this.gameType.triggerGameOver();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Render Methods     //
    ////////////////////////////

    /**
     * Renders the game, such as the HUD, or environment.
     *
     * @param game the game instance.
     * @param gfx  a 2D graphics instance.
     * @apiNote should not being called, for internal use only.
     */
    public void render(BubbleBlaster game, Renderer gfx) {
        if (this.gameType.isInitialized()) {
            this.gameType.render(gfx);
        }
        this.renderHUD(game, gfx);
    }

    /**
     * Renders the hud, in this method only the system and chat messages.
     *
     * @param game the game instance.
     * @param gfx  a 2D graphics instance.
     * @apiNote should not being called, for internal use only.
     */
    public void renderHUD(@SuppressWarnings({"unused", "RedundantSuppression"}) BubbleBlaster game, Renderer gfx) {
        int i = 0;
        for (String s : activeMessages) {
            Renderer gg2 = gfx.create(0, 71 + (32 * i), 1000, 32);

            gg2.color(new Color(0, 0, 0, 128));
            gg2.rect(0, 0, 1000, 32);

            gg2.color(new Color(255, 255, 255, 255));
            GraphicsUtils.drawLeftAnchoredString(gg2, s, new Point2D.Double(2, 2), 28, defaultFont);

            gg2.dispose();
            i++;
        }

        for (i = 0; i < this.activeMessages.size(); i++) {
            if (this.activeMsgTimes.get(i) < System.currentTimeMillis()) {
                this.activeMsgTimes.remove(i);
                this.activeMessages.remove(i);
                i--;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Events     //
    ////////////////////

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
                if (!BubbleBlaster.isPaused()) {
                    BubbleBlaster.instance().showScreen(new PauseScreen());
                }
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_SLASH) {
                if (!BubbleBlaster.isPaused()) {
                    BubbleBlaster.instance().showScreen(new CommandScreen());
                }
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F1 && BubbleBlaster.isDebugMode()) {
                this.gameType.triggerBloodMoon();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F3 && BubbleBlaster.isDebugMode()) {
                Objects.requireNonNull(this.gameType.getPlayer()).destroy();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F4 && BubbleBlaster.isDebugMode()) {
                Objects.requireNonNull(this.gameType.getPlayer()).levelUp();
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F5 && BubbleBlaster.isDebugMode()) {
                Objects.requireNonNull(this.gameType.getPlayer()).addScore(1000);
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_F6 && BubbleBlaster.isDebugMode()) {
                Objects.requireNonNull(this.gameType.getPlayer()).setDamageValue(this.gameType.getPlayer().getMaxDamageValue());
            }
        } else if (evt.getType() == KeyEventType.TYPE) {
            if (evt.getKeyChar() == ' ') {
                if (this.gameType.isGameOver()) {
                    Util.getSceneManager().displayScreen(new GameOverScreen(gameType.getResultScore()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent evt) {
        if (evt.getType() == MouseEventType.CLICK) {
            if (evt.getParentEvent().getButton() == 1) {
                if (KeyInput.isDown(KeyEvent.VK_F1)) {
                    Objects.requireNonNull(this.gameType.getPlayer()).teleport(evt.getParentEvent().getPoint());
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Utility methods     //
    /////////////////////////////

    public void tick() {
        this.gameType.tick();
    }
}
