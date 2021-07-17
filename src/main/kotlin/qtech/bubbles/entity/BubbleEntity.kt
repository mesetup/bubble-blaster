@file:Suppress("unused")

package qtech.bubbles.entity

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.bubble.AbstractBubble
import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.ResourceLocation.Companion.fromString
import qtech.bubbles.common.entity.*
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.core.controllers.KeyboardController.Companion.instance
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.entity.types.EntityType
import qtech.bubbles.environment.Environment
import qtech.bubbles.event.*
import qtech.bubbles.event.bus.EventBus
import qtech.hydro.GraphicsProcessor
import qtech.bubbles.init.Bubbles
import qtech.bubbles.init.Entities
import qtech.bubbles.init.TextureCollections
import qtech.bubbles.registry.Registry
import net.querz.nbt.tag.CompoundTag
import java.awt.Color
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin

/**
 * <h1>Bubble Entity.</h1>
 * One of the most important parts of the game.
 *
 * @see AbstractBubbleEntity
 */
@Suppress("UNUSED_PARAMETER")
open class BubbleEntity(gameMode: AbstractGameMode) : AbstractBubbleEntity(entityType, gameMode) {
    ///////////////////////////////////////////////////////////////////////////
    //     Attributes     //
    ////////////////////////
    // Radius.
    // Attributes
    var radius: Int
    var baseRadius: Int
        protected set

    // Bounce amount.
    var bounceAmount: Float
    var baseBounceAmount = 0f
        protected set
    // Event codes

    @Deprecated("not used anymore")
    private val collisionEventCode = 0

    @Deprecated("not used anymore")
    private val renderEventCode = 0

    @Deprecated("not used anymore")
    private val tickEventCode = 0

    ///////////////////////////////////////////////////////////////////////////
    //     Bubble Type     //
    /////////////////////////
    // Bubble type.
    var bubbleType: AbstractBubble
    var isEffectApplied = false
    private val binding: EventBus.Handler? = null
    private var ticksLived = 0

    /**
     * <h1>Spawn Event Handler</h1>
     * On-spawn.
     *
     * @param spawnData the entity's spawn data.
     */
    override fun prepareSpawn(spawnData: EntitySpawnData) {
        super.prepareSpawn(spawnData)
        val randomLocation = bubbleType.getRandomLocation(this, gameMode.ticks, gameMode.bubbleRandomizer.xRng, gameMode.bubbleRandomizer.yRng)
        if (!gameMode.isXCoordOverridden) {
            x = randomLocation.x
        }
        if (!gameMode.isYCoordOverridden) {
            y = randomLocation.y
        }
        bindEvents()
    }

    @SubscribeEvent
    fun onMouse(evt: MouseMotionEvent) {
//        if (evt.getType() == MouseEventType.CLICK) {
//            if (evt.getParentEvent().getButton() == 1) {
//                if (KeyboardController.instance().isPressed(KeyEvent.VK_F2)) {
//                    if (getShape().contains(evt.getParentEvent().getPoint())) {
//                        instantDestroy();
//                    }
//                }
//            }
//        }
        if (instance().isPressed(KeyEvent.VK_F2)) {
            if (shape.contains(evt.parentEvent.point)) {
                destroy()
            }
        }
    }

    /**
     * <h1>Bind Events</h1>
     *
     * **Warning: ***Unsafe method! Use [Environment.spawn] instead.*
     *
     * Events:
     *
     *  * [TickEvent]
     *  * [RenderEvent]
     *
     *
     * @see TickEvent
     *
     * @see RenderEvent
     *
     * @see Environment.spawn
     */
    override fun bindEvents() {
//        tickEventCode = QUpdateEvent.addListener(QUpdateEvent.getInstance(), GameScene.getInstance(), this::tick, RenderEventPriority.LOWER);
//        renderEventCode = QRenderEvent.addListener(QRenderEvent.getInstance(), GameScene.getInstance(), this::render, RenderEventPriority.LOWER);
//        collisionEventCode = QCollisionEvent.addListener(GameScene.getInstance(), this::onCollision, RenderEventPriority.NORMAL);
        BubbleBlaster.eventBus.registerK(this)
        areEventsBound = true
    }

    /**
     * <h1>Unbind Events</h1>
     *
     * **Warning: ***Unsafe method! Use [Environment.removeEntity]  removeBubble of GameType} instead.*
     *
     * Events:
     *
     *  * [TickEvent]
     *  * [RenderEvent]
     *
     *
     * @throws NoSuchElementException If listener is already fully or partly removed.
     * @see TickEvent
     *
     * @see RenderEvent
     *
     * @see Environment.removeEntity
     */
    override fun unbindEvents() {
        try {
            BubbleBlaster.eventBus.unregisterK(this)
        } catch (ignored: IllegalArgumentException) {
        } catch (ignored: IllegalStateException) {
        }
        areEventsBound = false
    }

    override val bounds: Rectangle?
        get() {
            val rectangle = super.bounds
            rectangle!!.width += 4
            rectangle!!.height += 4
            return rectangle
        }

    /**
     * Tick bubble entity.
     *
     * @param environment the environment where the entity is from.
     */
    @SubscribeEvent
    override fun tick(environment: Environment?) {
        // Check player and current scene.
        val player = gameMode.player
        if (player == null || !BubbleBlaster.instance.isGameLoaded) return

        // Set velocity speed.
        velocityX = if (BubbleBlaster.instance.isGameLoaded) {
            (-speed * (gameMode.player!!.level / 10.0 + 1)).toFloat()
        } else {
            (-speed * 5.0).toFloat()
        }
        velocityX *= gameMode.globalBubbleSpeedModifier.toFloat()
        super.tick(environment)
        if (x < -radius) {
            delete()
        }
        ticksLived++
    }

    override fun renderEntity(gg: GraphicsProcessor?) {
        if (!areEventsBound) return
        val composite1 = bubbleType.getComposite(ticksLived)
        val composite = gg!!.composite()
        gg.img(TextureCollections.BUBBLE_TEXTURES.get()[ResourceLocation(bubbleType.registryName!!.namespace, bubbleType.registryName!!.path + "/" + radius)], x.toInt() - radius / 2, y.toInt() - radius / 2)

        if (composite1 != null) {
            println(composite)
            //        if (composite instanceof AlphaComposite) {
//            AlphaComposite alphaComposite = (AlphaComposite) composite;
//            System.out.println(alphaComposite.getAlpha());
//            System.out.println(alphaComposite.getRule());
//        }
            gg.composite(composite1)
        }
    }

    override val shape: Ellipse2D
        get() {
            val colors: Array<out Color> = bubbleType.colors
            val rad: Int = radius + colors.size * 2
            return Ellipse2D.Float((x - rad.toFloat() / 2), (y - rad / 2), rad.toFloat(), rad.toFloat())
        }

    public override fun checkDamage() {
        if (damageValue <= 0.0) {
            delete()
        }
    }

    override var damageValue: Float
        get() = super.damageValue
        set(hardness) {
            super.damageValue = hardness
            radius = hardness.toInt() + bubbleType.colors.size * RADIUS_MOD
            checkDamage()
        }

    override fun damage(value: Double, source: EntityDamageSource?) {
        var value1 = value
        value1 = value1.coerceAtMost(damageValue.toDouble())
        super.damage(value1, source)
        if (source!!.entity is PlayerEntity) {
            val player = source.entity as PlayerEntity?
            val relX = bounds!!.centerX - player!!.bounds!!.centerX
            val relY = bounds!!.centerY - player.bounds!!.centerY
            val radians = atan2(relX, relY) * 180 / Math.PI
            val tempVelX = cos(radians) * bounceAmount
            val tempVelY = sin(radians) * bounceAmount
            player.triggerReflect(Point2D.Double(tempVelX, tempVelY), -0.01)
            if (value1 > 0) {
                bubbleType.onCollision(this, player)

                // Attributes
//                val scoreMultiplier = attributeMap.getBase(Attribute.SCORE_MULTIPLIER).toDouble()
//                val attack = attributeMap.getBase(Attribute.ATTACK).toDouble() // Maybe used.
//                val defense = attributeMap.getBase(Attribute.DEFENSE).toDouble() // Maybe used.

                // Calculate score value.
//                double visibleValue = radius * speed;
//                double nonVisibleValue = attack * defense;
//                double scoreValue = ((visibleValue * (nonVisibleValue + 1)) * scoreMultiplier * game.getLoadedGame().getGameType().getScoreMultiplier() * player.getScoreMultiplier()) * value / 32;
                val scoreValue = value1 * bubbleType.score

                // Add score.
                player.addScore(scoreValue)
            }
        }
        val colors: Array<out Color> = bubbleType.colors

        radius = damageValue.toInt() + colors.size * RADIUS_MOD
    }

    override fun getState(): CompoundTag {
        val document = super.getState()
        document.putInt("radius", radius)
        document.putInt("baseRadius", baseRadius)
        document.putDouble("bounceAmount", bounceAmount.toDouble())
        document.putDouble("baseBounceAmount", baseBounceAmount.toDouble())
        document.putBoolean("isEffectApplied", isEffectApplied)
        document.putString("bubbleName", bubbleType.registryName.toString())
        return document
    }

    override fun setState(state: CompoundTag) {
        super.setState(state)
        radius = state.getInt("Radius")
        baseRadius = state.getInt("BaseRadius")
        bounceAmount = state.getInt("BounceAmount").toFloat()
        baseBounceAmount = state.getInt("BaseBounceAmount").toFloat()
        val bubbleTypeKey = fromString(state.getString("bubbleType"))
        isEffectApplied = state.getBoolean("IsEffectApplied")
        bubbleType = Registry.getRegistry(AbstractBubble::class.java)[bubbleTypeKey]!!
    }

    companion object {
        const val RADIUS_MOD = 6

        // Entity type.
        private val entityType = Entities.BUBBLE.get()
        private val random = Random((System.currentTimeMillis() / 86400000).toDouble().roundToLong()) // Random day. 86400000 milliseconds == 1 day.

        fun getRandomType(gameMode: AbstractGameMode?): EntityType<out BubbleEntity> {
            return if (random.nextInt(3000) == 0) {
                Entities.GIANT_BUBBLE.get()
            } else Entities.BUBBLE.get()
        }
    }

    init {

        // Add collidables.
        this.addCollidable(Entities.PLAYER.get())

        // Get random properties
        val randomizer = this.gameMode.bubbleRandomizer
        val properties = randomizer.getRandomProperties(gameMode.gameBounds!!, gameMode)

        // Bubble Type
        bubbleType = if (properties.type.canSpawn(gameMode)) properties.type else Bubbles.NORMAL_BUBBLE.get()

        val colors: Array<out Color> = properties.type.colors

        // Dynamic values
        radius = properties.damageValue.toInt() + colors.size * RADIUS_MOD

        // Set attributes.
        attributeMap[Attribute.MAX_DAMAGE] = (properties.radius - colors.size * RADIUS_MOD).toFloat()
        attributeMap[Attribute.ATTACK] = properties.attack
        attributeMap[Attribute.DEFENSE] = properties.defense
        attributeMap[Attribute.SCORE_MULTIPLIER] = properties.scoreMultiplier
        attributeMap[Attribute.SPEED] = properties.speed

        // Set fields.
        speed = properties.speed
        radius = properties.radius
        baseSpeed = properties.speed
        baseRadius = properties.radius
        damageValue = (properties.radius - colors.size * RADIUS_MOD).toFloat()
        bounceAmount = bubbleType.bounceAmount

        // Set velocity
        velocityX = (-baseSpeed)

        // Set attributes.
    }
}