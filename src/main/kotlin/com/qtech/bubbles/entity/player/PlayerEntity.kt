@file:Suppress("unused")

package com.qtech.bubbles.entity.player

import com.google.common.annotations.Beta
import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.ability.AbilityContainer
import com.qtech.bubbles.common.ammo.AmmoType
import com.qtech.bubbles.common.entity.*
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.entity.DamageableEntity
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.event.*
import com.qtech.bubbles.event.type.KeyEventType
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.init.AmmoTypes
import com.qtech.bubbles.init.Entities
import com.qtech.bubbles.item.inventory.PlayerInventory
import com.qtech.bubbles.screen.EnvLoadScreen
import com.qtech.bubbles.util.Util
import com.qtech.bubbles.util.helpers.MathHelper
import net.querz.nbt.tag.CompoundTag
import org.apache.batik.ext.awt.geom.Polygon2D
import org.apache.commons.lang3.SystemUtils
import java.awt.Color
import java.awt.Point
import java.awt.Taskbar
import java.awt.event.KeyEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class PlayerEntity(gameMode: AbstractGameMode?) : DamageableEntity(Entities.PLAYER.get(), gameMode!!) {
    // These are the vertex coordinates:
    //
    //          |- Middle point
    //          |
    // 9876543210123456789
    // --------------------,
    // ....#*********#.....| -8
    // ....*.........*.....| -7
    // ....#***#.#***#.....| -6
    // ........*.*.........| -5
    // ..#*****#.#******#..| -4
    // .*..................| -3
    // #..................#| -2
    // *..................*| -1
    // *..................*| 0  // Middle point.
    // *..................*| 1
    // #..................#| 2
    // .*................*.| 3
    // ..#..............#..| 4
    private val shipShape: Area
    private val arrowShape: Area

    // Ammo
    var currentAmmo: AmmoType = AmmoTypes.BASIC.get()

    // Rotation
    public override var rotation = 0.0

    // Normal values.
    var score = 0.0
    var level = 1
        get() = field.coerceAtLeast(1).also { field = it }
        set(value) {
            field = value.coerceAtLeast(1)
        }

    // Modifiers.
    private val abilityEnergy: Long = 0

    // Ability
    val abilityContainer = AbilityContainer(this)

    // Acceleration
    var accelerateX = 0.0
    var accelerateY = 0.0

    // Motion (Arrow Keys).
    private var forward = false
    private var backward = false
    private var left = false
    private var right = false

    // Speed.
    val rotationSpeed = 16f

    // Delta velocity.
    private var velDelta = 0.0

    // Motion (XInput).
    private var xInputX = 0f
    private var xInputY = 0f

    //
    val inventory = PlayerInventory(this)

    var scoreMultiplier: Float
        get() = attributeMap[Attribute.SCORE_MULTIPLIER]
        set(value) {
            attributeMap[Attribute.SCORE_MULTIPLIER] = value
        }

    var baseScoreMultiplier: Float
        get() = bases[Attribute.SCORE_MULTIPLIER]
        private set(value) {
            bases[Attribute.SCORE_MULTIPLIER] = value
        }

    var luck: Float
        get() = attributeMap[Attribute.LUCK]
        set(value) {
            attributeMap[Attribute.LUCK] = value
        }

    var baseLuck: Float
        get() = bases[Attribute.LUCK]
        private set(value) {
            bases[Attribute.LUCK] = value
        }

    /**
     * @param s the message.
     */
    fun sendMessage(s: String) {
        val loadedGame = BubbleBlaster.instance.loadedGame
        loadedGame?.triggerMessage(s)
    }

    /**
     * <h1>Prepare spawn</h1>
     * Called when the entity was spawned.
     *
     * @param spawnData the data to spawn with.
     */
    override fun prepareSpawn(spawnData: EntitySpawnData) {
        super.prepareSpawn(spawnData)
        val currentScene = Objects.requireNonNull(Util.sceneManager).currentScreen
        if (currentScene == null && BubbleBlaster.instance.isGameLoaded ||
            currentScene is EnvLoadScreen
        ) {
            val gameBounds = gameMode.gameBounds
            x = MathHelper.clamp(x.toDouble(), gameBounds!!.minX, gameBounds.maxX).toFloat()
            y = MathHelper.clamp(y.toDouble(), gameBounds.minY, gameBounds.maxY).toFloat()
            bindEvents()
        }
    }

    override fun bindEvents() {
        BubbleBlaster.eventBus.register(this)
        areEventsBound = true
    }

    override fun unbindEvents() {
        BubbleBlaster.eventBus.unregister(this)
        areEventsBound = false
    }

    override fun areEventsBound(): Boolean {
        return areEventsBound
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Shape     //
    ///////////////////
    override val shape: Area
        get() {
            var transform = AffineTransform()
            transform.rotate(Math.toRadians(rotation), shipShape.bounds.centerX, shipShape.bounds.centerY)
            val area = Area(shipShape)
            area.transform(transform)
            transform = AffineTransform()
            transform.scale(scale, scale)
            transform.translate(x.toDouble(), y.toDouble())
            area.transform(transform)
            return area
        }

    /**
     * @return the shape of the ship.
     */
    val shipArea: Area
        get() {
            var transform = AffineTransform()
            transform.rotate(Math.toRadians(rotation), shipShape.bounds.centerX, shipShape.bounds.centerY)
            val area = Area(shipShape)
            area.transform(transform)
            transform = AffineTransform()
            transform.scale(scale, scale)
            transform.translate(x.toDouble(), y.toDouble())
            area.transform(transform)
            return area
        }

    /**
     * @return the arrow shape of the ship.
     */
    val arrowArea: Area
        get() {
            var transform = AffineTransform()
            transform.rotate(Math.toRadians(rotation), shipShape.bounds.centerX, shipShape.bounds.centerY)
            val area = Area(arrowShape)
            area.transform(transform)
            transform = AffineTransform()
            transform.scale(scale, scale)
            transform.translate(x.toDouble(), y.toDouble())
            area.transform(transform)
            return area
        }

    /**
     * @return the center position.
     */
    val center: Point
        get() = Point(bounds!!.centerX.toInt(), bounds!!.centerY.toInt())

    /**
     * Tick the player.
     *
     * @param environment the game-type where the entity is from.
     */
    @SubscribeEvent
    override fun tick(environment: Environment?) {
        // Super call
        super.tick(environment)

        // Spawned and loaded checks
        val loadedGame = BubbleBlaster.instance.loadedGame ?: return
        if (!isSpawned) return

        // Component ticking
        abilityContainer.onEntityTick()
        inventory.tick()

        // Decelerate
        accelerateX = accelerateX / (BubbleBlaster.TPS.toDouble() / 20.0) / 1.05
        accelerateY = accelerateY / (BubbleBlaster.TPS.toDouble() / 20.0) / 1.05
        var tempVelMotSpeed = 0f
        var tempVelRotSpeed = 0f

        // Check each direction, to create velocity
        if (forward) tempVelMotSpeed += speed
        if (backward) tempVelMotSpeed -= speed
        if (left) tempVelRotSpeed -= rotationSpeed
        if (right) tempVelRotSpeed += rotationSpeed
        if (xInputY != 0f) tempVelMotSpeed = xInputY * speed
        if (xInputX != 0f) tempVelRotSpeed = xInputX * rotationSpeed

        // Update X, and Y.
        if (isMotionEnabled) {
            rotation += tempVelRotSpeed / BubbleBlaster.TPS * 20
        }

        // Calculate Velocity X and Y.
        val angelRadians = Math.toRadians(rotation)
        val tempVelX = cos(angelRadians) * tempVelMotSpeed
        val tempVelY = sin(angelRadians) * tempVelMotSpeed

        if (isMotionEnabled) {
            accelerateX += tempVelX / BubbleBlaster.TPS.toDouble()
            accelerateY += tempVelY / BubbleBlaster.TPS.toDouble()
        }

        // Update X, and Y.
        x += (accelerateX + velocityX / (BubbleBlaster.TPS.toDouble() * 1)).toFloat()
        y += (accelerateY + velocityY / (BubbleBlaster.TPS.toDouble() * 1)).toFloat()

        // Velocity.
        when {
            velocityX > 0 -> {
                if (velocityX + velDelta < 0) {
                    velocityX = 0f
                } else {
                    velocityX += velDelta.toFloat()
                }
            }
            velocityX < 0 -> {
                if (velocityX + velDelta > 0) {
                    velocityX = 0f
                } else {
                    velocityX -= velDelta.toFloat()
                }
            }
        }
        when {
            velocityY > 0 -> {
                if (velocityY + velDelta < 0) {
                    velocityY = 0f
                } else {
                    velocityY += velDelta.toFloat()
                }
            }
            velocityX < 0 -> {
                if (velocityY + velDelta > 0) {
                    velocityY = 0f
                } else {
                    velocityY -= velDelta.toFloat()
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Collision detection. //
        //////////////////////////
        val width = bounds!!.getWidth() / 2
        val height = bounds!!.getHeight() / 2

        // Game border.
        val gameBounds = loadedGame.gameMode.gameBounds
        x = MathHelper.clamp(x.toDouble(), gameBounds!!.minX + width, gameBounds.maxX - width).toFloat()
        y = MathHelper.clamp(y.toDouble(), gameBounds.minY + height, gameBounds.maxY - height).toFloat()
    }

    override fun damage(value: Double, source: EntityDamageSource?) {
        val defense = attributeMap[Attribute.DEFENSE]
        if (defense <= 0.0) {
            destroy()
            return
        }

        // Deal damage to the player.
        damageValue -= (value / defense).toFloat()

        // Check health.
        checkDamage()

        // Check if source has attack modifier.
        if (value > 0.0) {

            // Check if window is not focused.
            if (!BubbleBlaster.instance.frame.isFocused) {
                if (SystemUtils.IS_JAVA_9) {
                    // Let the taskbar icon flash. (Java 9+)
                    val taskbar = Taskbar.getTaskbar()
                    try {
                        taskbar.requestWindowUserAttention(BubbleBlaster.instance.frame)
                    } catch (ignored: UnsupportedOperationException) {
                    }
                }
            }
        }
    }

    @SubscribeEvent
    override fun onCollision(event: CollisionEvent) {

    }

    @SubscribeEvent
    fun onKeyboardEvent(evt: KeyboardEvent?) {
        if (evt != null) {
            val e = evt.parentEvent
            if (evt.type == KeyEventType.PRESS) {
                if (e.keyCode == KeyEvent.VK_UP) forward = true
                if (e.keyCode == KeyEvent.VK_DOWN) backward = true
                if (e.keyCode == KeyEvent.VK_LEFT) left = true
                if (e.keyCode == KeyEvent.VK_RIGHT) right = true
            }
            if (evt.type == KeyEventType.RELEASE) {
                if (e.keyCode == KeyEvent.VK_UP) forward = false
                if (e.keyCode == KeyEvent.VK_DOWN) backward = false
                if (e.keyCode == KeyEvent.VK_LEFT) left = false
                if (e.keyCode == KeyEvent.VK_RIGHT) right = false
            }
        }
    }

    @SubscribeEvent
    fun onXInputEvent(evt: XInputEvent?) {
        println(evt)
        if (evt != null) {
            if (forward || backward || left || right) return
            xInputX = evt.rightStickX
            xInputY = evt.leftStickY
        }
    }

    @Synchronized
    override fun renderEntity(gg: GraphicsProcessor?) {
        if (!isSpawned) return

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the ship with the correct color. //
        ///////////////////////////////////////////
        gg!!.color(Color.red)
        gg.shapeF(shipArea)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Fill the arrow with the correct color. //
        ////////////////////////////////////////////
        gg.color(Color.white)
        gg.shapeF(arrowArea)
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //     Rotate Delta     //
    //////////////////////////
    fun rotateDelta(deltaRotation: Int) {
        rotation += deltaRotation.toDouble()
    }

    /**
     * <h1>Trigger a Reflection</h1>
     * Triggers a reflection, there are some problems with the velocity.
     * That's why it's currently in beta.
     *
     * @param velocity the amount velocity for bounce.
     * @param delta    the delta change.
     */
    @Beta
    fun triggerReflect(velocity: Point2D, delta: Double) {
        velDelta = delta
        velocityX = velocity.x.toFloat()
        velocityY = velocity.y.toFloat()
    }

    /**
     * <h1>Delete method</h1>
     * ***WARNING: **<u>This is an unsafe method, use [AbstractGameMode.triggerGameOver] instead.</u>*
     *
     * @see AbstractGameMode.triggerGameOver
     */
    override fun delete() {
        unbindEvents()
    }

    /**
     * <h1>Checks health</h1>
     * Checks health, if health is zero or less, it will trigger game-over.
     */
    public override fun checkDamage() {
        if (damageValue <= 0) if (!gameMode.isGameOver) gameMode.triggerGameOver()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Entity Data     //
    /////////////////////////
    override fun getState(): CompoundTag {
        val document = super.getState()
        document.putDouble("score", score)
        document.putDouble("rotation", rotation)
        return document
    }

    override fun setState(state: CompoundTag) {
        super.getState()
        score = state.getDouble("score")
        rotation = state.getDouble("rotation")
    }

    fun addScore(value: Double) {
        score += value
    }

    fun subtractScore(value: Long) {
        score -= value.toDouble()
    }

    fun levelUp() {
        level++
    }

    fun levelDown() {
        level--
    }

    fun forward(bool: Boolean) {
        forward = bool
    }

    fun backward(bool: Boolean) {
        backward = bool
    }

    fun left(bool: Boolean) {
        left = bool
    }

    fun right(bool: Boolean) {
        right = bool
    }

    /**
     * <h1>Player entity.</h1>
     * The player is controlled be the keyboard and is one of the important features of the game. (Almost any game).
     *
     * @see DamageableEntity
     */
    init {
        this.addCollidable(Entities.BUBBLE.get())

        // Ship shape.
        val shipShape1: Ellipse2D = Ellipse2D.Double(-20.0, -20.0, 40.0, 40.0)
        shipShape = Area(shipShape1)

        // Arrow shape.
        val arrowShape1 = Polygon2D(intArrayOf(-5, -10, 15, -10), intArrayOf(0, -10, 0, 10), 4)
        arrowShape = Area(arrowShape1)

        // Velocity.
        velocityX = 0f
        velocityY = 0f

        // Set attributes.
        attributeMap[Attribute.DEFENSE] = 1f
        attributeMap[Attribute.ATTACK] = 10f
        attributeMap[Attribute.MAX_DAMAGE] = 100f
        attributeMap[Attribute.SPEED] = 16f

        // Health
        speed = 10f
        damageValue = 100f
    }
}