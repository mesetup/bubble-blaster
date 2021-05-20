package com.qtech.bubbleblaster;

import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.common.entity.*;
import com.qtech.bubbleblaster.common.gamestate.GameEvent;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.common.random.PseudoRandom;
import com.qtech.bubbleblaster.common.screen.Screen;
import com.qtech.bubbleblaster.core.common.SavedGame;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.core.utils.categories.GraphicsUtils;
import com.qtech.bubbleblaster.core.utils.categories.ShapeUtils;
import com.qtech.bubbleblaster.environment.Environment;
import com.qtech.bubbleblaster.event.CollisionEvent;
import com.qtech.bubbleblaster.event.KeyboardEvent;
import com.qtech.bubbleblaster.event.MouseEvent;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.bus.EventBus;
import com.qtech.bubbleblaster.event.type.KeyEventType;
import com.qtech.bubbleblaster.event.type.MouseEventType;
import com.qtech.bubbleblaster.media.AudioSlot;
import com.qtech.bubbleblaster.screen.CommandScreen;
import com.qtech.bubbleblaster.screen.GameOverScreen;
import com.qtech.bubbleblaster.screen.PauseScreen;
import com.qtech.bubbleblaster.util.Util;
import lombok.Getter;

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
    private File saveDir;

    // Active messages.
    private final ArrayList<String> activeMessages = new ArrayList<>();
    private final ArrayList<Long> activeMsgTimes = new ArrayList<>();

    // Current screen.
    @Getter
    @Deprecated
    private Screen currentScreen;

    // Audio.
    private AudioSlot ambientAudio;
    private long nextAudio;

    // Flags.
    @SuppressWarnings("FieldCanBeLocal")
    private boolean gameActive = false;

    // Event
    private EventBus.Handler binding;

    // Save
    @Getter
    private final SavedGame savedGame;
    private GameEvent currentGameEvent;

    public LoadedGame(SavedGame savedGame, Environment environment) {
        this.savedGame = savedGame;
        this.gameType = environment.getGameType();
        this.environment = environment;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Show and Hide     //
    ///////////////////////////
    public void run() {
        Util.setCursor(BubbleBlaster.getInstance().getBlankCursor());

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
            gameType.unbindEvents();
            unbindEvents();

            autoSaveThread.interrupt();
            collisionThread.interrupt();
            ambientAudioThread.interrupt();
            gameEventHandlerThread.interrupt();

            environment.quit();

            // Hide cursor.
            Util.setCursor(BubbleBlaster.getInstance().getDefaultCursor());
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
                            this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/bgm/art_of_silence.mp3")), "artOfSilence");
                            this.ambientAudio.setVolume(0.25d);
                            this.ambientAudio.play();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            BubbleBlaster.getInstance().shutdown();
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
                        BubbleBlaster.getInstance().shutdown();
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
                    this.ambientAudio = new AudioSlot(Objects.requireNonNull(getClass().getResource("/assets/qbubbles/audio/bgm/blood_moon_state.mp3")), "bloodMoonState");
                    this.ambientAudio.setVolume(0.25d);
                    this.ambientAudio.play();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    BubbleBlaster.getInstance().shutdown();
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

            List<Entity> entities = this.environment.getEntities();

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
                                        BubbleBlaster.getEventBus().post(new CollisionEvent(BubbleBlaster.getInstance(), delta, source, target));
                                        BubbleBlaster.getEventBus().post(new CollisionEvent(BubbleBlaster.getInstance(), delta, target, source));

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

    private synchronized void onAutoSave() {
        this.gameType.dumpSaveData(savedGame);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Event Binding     //
    ///////////////////////////
    public void bindEvents() {
        BubbleBlaster.getEventBus().register(this);
        this.gameActive = true;
    }

    public void unbindEvents() {
        this.binding.unbind();
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

    @Deprecated
    public synchronized void triggerGameOver() {
        if (!this.gameType.isGameOver()) {
            this.gameType.setResultScore(Math.round(Objects.requireNonNull(this.gameType.getPlayer()).getScore()));
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
    @Deprecated
    public void renderGUI(BubbleBlaster game, GraphicsProcessor gg) {
        this.gameType.renderGUI(gg);

        if (this.currentScreen != null) this.currentScreen.renderGUI(game, new GraphicsProcessor(gg));
    }

    public void render(BubbleBlaster game, GraphicsProcessor gg) {
//        if (this.gameType.isBloodMoonActive()) {
//            gg.setColor(GameEvents.BLOOD_MOON_EVENT.get().getBackgroundColor());
//            gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
//        }
        if (this.gameType.isInitialized()) {
            this.gameType.render(gg);
        }
        this.renderHUD(game, gg);
    }

    public void renderHUD(@SuppressWarnings({"unused", "RedundantSuppression"}) BubbleBlaster game, GraphicsProcessor gg) {
        int i = 0;
        for (String s : activeMessages) {
            GraphicsProcessor gg2 = gg.create(0, 71 + (32 * i), 1000, 32);

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
     */
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onUpdate() {
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
                if (!BubbleBlaster.isPaused()) {
                    BubbleBlaster.getInstance().displayScene(new PauseScreen());
                }
            } else if (evt.getParentEvent().getKeyCode() == KeyEvent.VK_SLASH) {
                if (!BubbleBlaster.isPaused()) {
                    BubbleBlaster.getInstance().displayScene(new CommandScreen());
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
                if (KeyboardController.instance().isPressed(KeyEvent.VK_F1)) {
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
