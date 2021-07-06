package qtech.bubbles

import qtech.bubbles.common.Timer
import qtech.bubbles.common.Timer.time
import qtech.bubbles.common.entity.*
import qtech.bubbles.common.gamestate.GameEvent
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.random.PseudoRandom
import qtech.bubbles.common.screen.Screen
import qtech.bubbles.core.common.SavedGame
import qtech.bubbles.core.controllers.KeyboardController
import qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import qtech.bubbles.core.utils.categories.ShapeUtils.checkIntersection
import qtech.bubbles.entity.DamageableEntity
import qtech.bubbles.entity.Entity
import qtech.bubbles.environment.Environment
import qtech.bubbles.event.KeyboardEvent
import qtech.bubbles.event.MouseEvent
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.event.bus.EventBus
import qtech.bubbles.event.type.KeyEventType
import qtech.bubbles.event.type.MouseEventType
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.media.AudioSlot
import qtech.bubbles.screen.CommandScreen
import qtech.bubbles.screen.GameOverScreen
import qtech.bubbles.screen.PauseScreen
import qtech.bubbles.util.Util.game
import qtech.bubbles.util.Util.sceneManager
import qtech.bubbles.util.Util.setCursor
import lombok.Getter
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.geom.Point2D
import java.io.File
import java.net.URISyntaxException
import java.util.*

@Suppress("DEPRECATION", "UNUSED_PARAMETER", "UNUSED_VARIABLE", "UNCHECKED_CAST")
class LoadedGame(
// Save
    @field:Getter internal val savedGame: SavedGame, environment: Environment
) : Runnable {
    // Fonts.
    private val defaultFont = Font(game.pixelFontName, Font.PLAIN, 32)

    // Types
    @Getter
    val gameMode: AbstractGameMode

    @Getter
    private val environment: Environment

    // Threads.
    private var autoSaveThread: Thread? = null
    private var collisionThread: Thread? = null
    private var ambientAudioThread: Thread? = null
    private val gameEventHandlerThread: Thread? = null

    // Files / folders.
    val saveDir: File? = null

    // Active messages.
    private val activeMessages = ArrayList<String>()
    private val activeMsgTimes = ArrayList<Long>()

    // Current screen.
    @Getter
    @Deprecated("")
    private val currentScreen: Screen? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Getter-Only Properties     //
    ////////////////////////////////////
    // Audio.
    var ambientAudio: AudioSlot? = null
        private set
    private var nextAudio: Long = 0

    // Flags.
    private var gameActive = false

    // Event
    private val binding: EventBus.Handler? = null
    private val currentGameEvent: GameEvent? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Show and Hide     //
    ///////////////////////////
    override fun run() {
        setCursor(BubbleBlaster.instance.blankCursor)
        activate()
        environment.gameMode.start()
        autoSaveThread = Thread({ autoSaveThread() }, "AutoSaver")
        autoSaveThread!!.start()
        collisionThread = Thread({ collisionThread() }, "Collision")
        collisionThread!!.start()
        ambientAudioThread = Thread({ ambientAudioThread() }, "Collision")
        ambientAudioThread!!.start()
    }

    fun quit() {
        environment.quit()
        try {
            // Unbind events.
            gameMode.unbindEvents()
            deactivate()
            autoSaveThread!!.interrupt()
            collisionThread!!.interrupt()
            ambientAudioThread!!.interrupt()
            gameEventHandlerThread!!.interrupt()
            environment.quit()

            // Hide cursor.
            setCursor(BubbleBlaster.instance.defaultCursor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        System.gc()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Pause methods     //
    ///////////////////////////
    @Deprecated("")
    fun pause() {
    }

    @Deprecated("")
    fun unpause() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Thread methods     //
    ////////////////////////////
    private fun ambientAudioThread() {
//        Toolkit toolkit = Toolkit.getToolkit();
        while (gameActive) {
            if (ambientAudio == null) {
                if (!gameMode.isBloodMoonActive && nextAudio < System.currentTimeMillis()) {
                    if (PseudoRandom(System.nanoTime()).getNumber(0, 5, -1) == 0) {
                        try {
                            ambientAudio = AudioSlot(Objects.requireNonNull(javaClass.getResource("/assets/bubbleblaster/audio/bgm/art_of_silence.mp3")), "artOfSilence")
                            ambientAudio!!.volume = 0.25
                            ambientAudio!!.play()
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                            BubbleBlaster.instance.shutdown()
                        }
                    } else {
                        nextAudio = System.currentTimeMillis() + 1000
                    }
                } else if (gameMode.isBloodMoonActive) {
                    try {
                        ambientAudio = AudioSlot(Objects.requireNonNull(javaClass.getResource("/assets/bubbleblaster/audio/bgm/blood_moon_state.mp3")), "bloodMoonState")
                        ambientAudio!!.volume = 0.25
                        ambientAudio!!.play()
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                        BubbleBlaster.instance.shutdown()
                    }
                }
            } else if (ambientAudio!!.isStopped) {
                ambientAudio = null
            } else if (!ambientAudio!!.clip.isPlaying) {
                ambientAudio!!.stop()
                ambientAudio = null
            } else if (gameMode.isBloodMoonActive && ambientAudio!!.name != "bloodMoonState") {
                ambientAudio!!.stop()
                ambientAudio = null
                try {
                    ambientAudio = AudioSlot(Objects.requireNonNull(javaClass.getResource("/assets/bubbleblaster/audio/bgm/blood_moon_state.mp3")), "bloodMoonState")
                    ambientAudio!!.volume = 0.25
                    ambientAudio!!.play()
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                    BubbleBlaster.instance.shutdown()
                }
            }
        }
    }

    private fun collisionThread() {
        var time = time
        var passed: Double
        while (gameActive) {
            // Calculate tick delta-time.
            val time2 = Timer.time
            passed = time2 - time
            time = time2
            val entities: List<Entity> = environment.entities
            if (entities.isEmpty()) {
                println("ERROR")
            }
            val loopingEntities: List<Entity> = ArrayList(entities)
            if (gameMode.isInitialized) {
                if (!BubbleBlaster.isPaused) {
                    for (i in loopingEntities.indices) {
                        for (j in i + 1 until loopingEntities.size) {
                            val source = loopingEntities[i]
                            val target = loopingEntities[j]
                            try {
                                // Check is collisionable with each other
                                if (source.isCollidingWith(target)) {

                                    // Check intersection.
                                    if (checkIntersection(source.shape, target.shape)) {
                                        // Handling collision by posting collision event, and let the intersected entities attack each other.
//                                        BubbleBlaster.getEventBus().post(new CollisionEvent(BubbleBlaster.instance, passed, source, target));
//                                        BubbleBlaster.getEventBus().post(new CollisionEvent(BubbleBlaster.instance, passed, target, source));

//                                        LOGGER.debug("Attack / Defense: " + source.getAttributeMap().getBase(Attribute.ATTACK) + " / " + source.getAttributeMap().getBase(Attribute.DEFENSE));
//                                        LOGGER.debug("Attack / Defense: " + target.getAttributeMap().getBase(Attribute.ATTACK) + " / " + target.getAttributeMap().getBase(Attribute.DEFENSE));
                                        if (target is DamageableEntity) {
                                            if (source is DamageableEntity) {
                                                target.damage(source.attributeMap.get(Attribute.ATTACK) * passed / target.attributeMap.get(Attribute.DEFENSE), EntityDamageSource(source, DamageSourceType.COLLISION))
                                                source.damage(target.attributeMap.get(Attribute.ATTACK) * passed / source.attributeMap.get(Attribute.DEFENSE), EntityDamageSource(target, DamageSourceType.COLLISION))
                                            }
                                        }
                                    }
                                }
                            } catch (exception: ArrayIndexOutOfBoundsException) {
                                BubbleBlaster.logger.info("Array index was out of bounds! Check check double check!")
                            }
                        }
                    }
                }
            }
            gameMode.tick()
        }
        BubbleBlaster.logger.info("Collision thread will die now.")
    }

    private fun autoSaveThread() {
        var nextSave = System.currentTimeMillis()
        while (!gameMode.isGameOver) {
//            System.out.println(nextSave - System.currentTimeMillis());
            if (nextSave - System.currentTimeMillis() < 0) {
                BubbleBlaster.logger.info("Auto Saving...")
                onAutoSave()
                nextSave = System.currentTimeMillis() + 30000
            }
            try {
                Thread.sleep(30000)
            } catch (e: InterruptedException) {
                BubbleBlaster.logger.warn("Could not sleep thread.")
            }
        }
        BubbleBlaster.logger.debug("Stopping AutoSaveThread...")
    }

    @Synchronized
    private fun onAutoSave() {
        gameMode.dumpSaveData(savedGame)
        gameMode.dumpState(savedGame)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Event Binding     //
    ///////////////////////////
    internal fun activate() {
        BubbleBlaster.eventBus.register(this)
        gameActive = true
    }

    internal fun deactivate() {
        binding!!.unbind()
        gameActive = false
    }

    fun isGameActive(): Boolean {
        return gameActive
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Trigger Methods     //
    /////////////////////////////
    fun triggerMessage(s: String) {
        activeMessages.add(s)
        activeMsgTimes.add(System.currentTimeMillis() + 3000)
    }

    @Deprecated("")
    @Synchronized
    fun triggerGameOver() {
        if (!gameMode.isGameOver) {
            gameMode.resultScore = Math.round(Objects.requireNonNull(gameMode.player)!!.score)
            gameMode.triggerGameOver()
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
    @Deprecated("")
    fun renderGUI(game: BubbleBlaster?, gg: GraphicsProcessor?) {
        gameMode.renderGUI(gg)
        if (currentScreen != null) currentScreen.renderGUI(game!!, GraphicsProcessor(gg!!))
    }

    fun render(game: BubbleBlaster?, gg: GraphicsProcessor) {
//        if (this.gameType.isBloodMoonActive()) {
//            gg.setColor(GameEvents.BLOOD_MOON_EVENT.get().getBackgroundColor());
//            gg.fillRect(0, 0, QBubbles.getInstance().getWidth(), QBubbles.getInstance().getHeight());
//        }
        if (gameMode.isInitialized) {
            gameMode.render(gg)
        }
        renderHUD(game, gg)
    }

    fun renderHUD(game: BubbleBlaster?, gg: GraphicsProcessor) {
        var i = 0
        for (s in activeMessages) {
            val gg2 = gg.create(0, 71 + 32 * i, 1000, 32)
            gg2.color(Color(0, 0, 0, 128))
            gg2.rectF(0, 0, 1000, 32)
            gg2.color(Color(255, 255, 255, 255))
            drawLeftAnchoredString(gg2, s, Point2D.Double(2.0, 2.0), 28.0, defaultFont)
            gg2.dispose()
            i++
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Events     //
    ////////////////////
    /**
     * <h1>Update Event-Handler</h1>
     * Update event handler for the game scene. Used for collision detection.
     */
    @SubscribeEvent
    fun onUpdate() {
        val messageCopy = activeMessages.clone() as ArrayList<String>
        val timeCopy = activeMsgTimes.clone() as ArrayList<Long>
        var i = 0
        while (i < activeMessages.size) {
            if (activeMsgTimes[i] < System.currentTimeMillis()) {
                activeMsgTimes.removeAt(i)
                activeMessages.removeAt(i)
                i--
            }
            i++
        }
    }

    /**
     * <h1>Keyboard Event-Handler</h1>
     * Keyboard event-handler, handles screen changing and debug-mode activations.
     *
     * @param evt the keyboard event.
     */
    @SubscribeEvent
    fun onKeyboard(evt: KeyboardEvent) {
        if (evt.type === KeyEventType.PRESS) {
            if (evt.parentEvent.keyCode == KeyEvent.VK_ESCAPE) {
                if (!BubbleBlaster.isPaused) {
                    BubbleBlaster.instance.displayScene(PauseScreen())
                }
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_SLASH) {
                if (!BubbleBlaster.isPaused) {
                    BubbleBlaster.instance.displayScene(CommandScreen())
                }
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_F1 && BubbleBlaster.debugMode) {
                gameMode.triggerBloodMoon()
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_F3 && BubbleBlaster.debugMode) {
                Objects.requireNonNull(gameMode.player)!!.destroy()
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_F4 && BubbleBlaster.debugMode) {
                Objects.requireNonNull(gameMode.player)!!.levelUp()
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_F5 && BubbleBlaster.debugMode) {
                Objects.requireNonNull(gameMode.player)!!.addScore(1000.0)
            } else if (evt.parentEvent.keyCode == KeyEvent.VK_F6 && BubbleBlaster.debugMode) {
                Objects.requireNonNull(gameMode.player)!!.damageValue = gameMode.player!!.maxDamageValue
            }
        } else if (evt.type === KeyEventType.TYPE) {
            if (evt.keyChar == ' ') {
                if (gameMode.isGameOver) {
                    sceneManager.displayScreen(GameOverScreen(gameMode.resultScore))
                }
            }
        }
    }

    @SubscribeEvent
    fun onMouse(evt: MouseEvent) {
        if (evt.type === MouseEventType.CLICK) {
            if (evt.parentEvent.button == 1) {
                if (KeyboardController.instance().isPressed(KeyEvent.VK_F1)) {
                    Objects.requireNonNull(gameMode.player)!!.teleport(evt.parentEvent.point)
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Utility methods     //
    /////////////////////////////
    fun tick() {
        gameMode.tick()
    }

    init {
        gameMode = environment.gameMode
        this.environment = environment
    }
}