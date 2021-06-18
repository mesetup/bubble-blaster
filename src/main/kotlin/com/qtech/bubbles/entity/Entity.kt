package com.qtech.bubbles.entity

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.ability.AbilityType
import com.qtech.bubbles.common.effect.EffectInstance
import com.qtech.bubbles.common.entity.EntitySpawnData
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.interfaces.StateHolder
import com.qtech.bubbles.entity.types.EntityType
import com.qtech.bubbles.environment.Environment
import com.qtech.bubbles.event.CollisionEvent
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.registry.Registers
import net.querz.nbt.tag.CompoundTag
import org.bson.BsonDocument
import java.awt.Point
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.Point2D
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.math.roundToInt

/**
 * <h1>Entity base class.</h1>
 * The base for all entities, such as the player or a bubble.
 *
 * @author Quinten Jungblut
 * @see DamageableEntity
 *
 * @see AbstractBubbleEntity
 *
 * @since 1.0.0
 */
@Suppress("UNUSED_PARAMETER")
abstract class Entity (// Types
    open val type: EntityType<*>,
    open val gameMode: AbstractGameMode
) : StateHolder {
    // ID's
    open val entityId: Long
    open val uniqueId: UUID

    // Effects
    open val activeEffects = CopyOnWriteArraySet<EffectInstance>()

    // Flags
    open var isMotionEnabled = true

    // Position
    open var x: Float = 0f
    open var y: Float = 0f
    open var velocityX: Float = 0f
    open var velocityY: Float = 0f

    open var pos: Point2D.Float
        get() = Point2D.Float(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    open var velocity: Point2D.Float
        get() = Point2D.Float(velocityX, velocityY)
        set(value) {
            velocityX = value.x
            velocityY = value.y
        }

    open val registryName: ResourceLocation?
        get() = type.registryName

    // Misc
    abstract val shape: Shape?
    // Protected thingz
    protected val game: BubbleBlaster

    protected val collisionWith = HashSet<EntityType<*>>()
    // Attributes
    open var scale = 1.0
    var environment: Environment?
        protected set

    protected var areEventsBound = false

    var isSpawned = false
        private set

    // Tag
    private var tag = CompoundTag()

    // Fields.
    protected open var rotation: Double = 0.0

    // Abilities.
    private val abilities = HashMap<AbilityType<*>, BsonDocument>()

    //        if (markedForDeletion) {
//            PreGameLoader.LOGGER.debug("DELETION FLAG GETTING");
//        }
    var isMarkedForDeletion = false
        private set

    open fun prepareSpawn(spawnData: EntitySpawnData) {
        environment = spawnData.environment
        val pos = spawnData.pos
        if (pos != null) {
            x = pos.x.toFloat()
            y = pos.y.toFloat()
        }
    }

    /**
     * On spawn.
     *
     * @param pos         the position to spawn at.
     * @param environment te environment to spawn in.
     */
    open fun onSpawn(pos: Point?, environment: Environment?) {
        isSpawned = true
    }

    /**
     * On collision for entity.
     *
     * @param target collision target.
     */
    @Deprecated("")
    protected fun onCollision(target: Entity?) {
    }

    /**
     * On collision using event.
     *
     * @param event a [collision event][CollisionEvent].
     */
    @SubscribeEvent
    abstract fun onCollision(event: CollisionEvent)

    /**
     * @return True if the events are bound, false otherwise.
     */
    protected open fun areEventsBound(): Boolean {
        return areEventsBound
    }
    /**
     * Tick event.
     *
     * @param environment the environment where the entity is from.
     */
    open fun tick(environment: Environment?) {
//        if (!isSpawned()) return;
        for (effectInstance in activeEffects) {
            effectInstance.tick(this)
        }
        activeEffects.removeIf { effectInstance: EffectInstance -> effectInstance.remainingTime < 0.0 }
        x += if (isMotionEnabled) velocityX / BubbleBlaster.TPS.toFloat() else 0.toFloat()
        y += if (isMotionEnabled) velocityY / BubbleBlaster.TPS.toFloat() else 0.toFloat()
    }

    abstract fun renderEntity(gg: GraphicsProcessor?)

    open fun delete() {
        if (areEventsBound()) {
            unbindEvents()
        }
        isMarkedForDeletion = true
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Equals & HashCode     //
    ///////////////////////////////
    @SubscribeEvent
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val entity = other as Entity
        return entityId == entity.entityId
    }

    protected abstract fun bindEvents()

    protected abstract fun unbindEvents()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Get Bounds     //
    ////////////////////////
    open val bounds: Rectangle?
        get() {
            val shapeObj = shape
            return shapeObj!!.bounds
        }

    //    public MetaData getMetaData() {
    //        return metaData;
    //    }
    //
    //    public void setMetaData(MetaData metaData) {
    //        this.metaData = metaData;
    //    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Teleport and Position     //
    ///////////////////////////////////
    fun teleport(x: Double, y: Double) {
        this.teleport(Point2D.Double(x, y))
    }

    fun teleport(pos: Point2D) {
        onTeleporting(Point2D.Double(x.toDouble(), y.toDouble()), Point2D.Double(pos.x, pos.y))
        x = pos.x.toFloat()
        y = pos.y.toFloat()
        onTeleported(Point2D.Double(x.toDouble(), y.toDouble()), Point2D.Double(pos.x, pos.y))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Teleport events     //
    /////////////////////////////
    @Suppress("UNUSED_PARAMETER")
    fun onTeleported(from: Point2D?, to: Point2D?) {}

    @Suppress("UNUSED_PARAMETER")
    fun onTeleporting(from: Point2D?, to: Point2D?) {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Motion     //
    ////////////////////
    fun move(deltaX: Double, deltaY: Double) {
        x += deltaX.toFloat()
        y += deltaY.toFloat()
    }

    fun setVelocity(velX: Float, velY: Float) {
        this.velocityX = velX
        this.velocityY = velY
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Collidable     //
    ////////////////////////
    fun addCollidable(entity: Entity) {
        collisionWith.add(entity.type)
    }

    fun addCollidable(entityType: EntityType<*>) {
        collisionWith.add(entityType)
    }

    fun isCollidingWith(entity: Entity): Boolean {
        return collisionWith.contains(entity.type)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     Effects     //
    /////////////////////
    fun addEffect(effectInstance: EffectInstance): EffectInstance {
        for (effectInstance1 in activeEffects) {
            if (effectInstance1.type === effectInstance.type) {
                if (effectInstance1.remainingTime < effectInstance.remainingTime) {
                    effectInstance1.remainingTime = effectInstance.remainingTime
                }
                return effectInstance1
            }
        }
        activeEffects.add(effectInstance)
        effectInstance.start(this)
        return effectInstance
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     To-String     //
    ///////////////////////
    open fun toSimpleString(): String {
        return registryName.toString() + "@(" + x.roundToInt() + "," + y.roundToInt() + ")"
    }

    open fun toAdvancedString(): String {
        val nbt = getState()
        val data = nbt.toString()
        return "$registryName@$data"
    }

    fun removeEffect(effectInstance: EffectInstance) {
        activeEffects.remove(effectInstance)
        if (effectInstance.isActive) {
            effectInstance.stop(this)
        }
    }

    override fun setState(state: CompoundTag) {
        tag = state.getCompoundTag("Tag")
    }

    override fun getState(): CompoundTag {
        val state = CompoundTag()
        state.put("Tag", tag)
        state.putString("type", type.registryName.toString())
        return state
    }

    fun getAbilityCompound(abilityType: AbilityType<*>): BsonDocument? {
        return abilities[abilityType]
    }

    fun <E> getTag(): CompoundTag {
        return tag
    }

    override fun hashCode(): Int {
        return entityId.hashCode()
    }

    companion object {
        @Deprecated("")
        fun getEntity(gameMode: AbstractGameMode, tag: CompoundTag): Entity {
            val name = tag.getString("Name")
            val rl = ResourceLocation.fromString(name)
            val entityType = Registers.ENTITIES[rl]!!
            return entityType.create(gameMode, tag)
        }
    }

    // Constructor
    init {
        this.environment = gameMode.environment
        this.entityId = gameMode.getEntityId(this)
        this.game = BubbleBlaster.instance
        this.uniqueId = UUID.randomUUID()
    }
}