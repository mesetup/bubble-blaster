package qtech.bubbles.entity

import qtech.bubbles.common.AttributeMap
import qtech.bubbles.common.entity.Attribute
import qtech.bubbles.common.entity.EntityDamageSource
import qtech.bubbles.common.entity.Modifier
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.entity.types.EntityType
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.event.CollisionEvent
import qtech.bubbles.event.SubscribeEvent
import qtech.bubbles.util.helpers.MathHelper
import com.qtech.preloader.PreGameLoader
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag
import kotlin.math.roundToInt

/**
 * <h1>Living Entity base class.</h1>
 * The class for all living entities, such as [the player][PlayerEntity].
 *
 * @see Entity
 */
abstract class DamageableEntity (type: EntityType<*>, gameMode: AbstractGameMode) : Entity(type, gameMode) {
    open var damageValue: Float = 0f
        set(value) {
            field = MathHelper.clamp(value, 0f, attributeMap.get(Attribute.MAX_DAMAGE))
        }

    protected var bases = AttributeMap()

    var maxDamageValue: Float
        get() = attributeMap[Attribute.MAX_DAMAGE]
        set(value) {
            attributeMap.set(Attribute.MAX_DAMAGE, value)
        }

    var baseMaxDamageValue: Float
        get() = bases[Attribute.MAX_DAMAGE]
        protected set(value) {
            bases.set(Attribute.MAX_DAMAGE, value)
        }

    var attackDamage: Float
        get() = attributeMap[Attribute.ATTACK]
        set(value) {
            attributeMap.set(Attribute.ATTACK, value)
        }

    var baseAttackDamage: Float
        get() = bases[Attribute.ATTACK]
        protected set(value) {
            bases.set(Attribute.ATTACK, value)
        }

    var defenseValue: Float
        get() = attributeMap[Attribute.DEFENSE]
        set(value) {
            attributeMap.set(Attribute.DEFENSE, value)
        }

    var baseDefenseValue: Float
        get() = bases[Attribute.DEFENSE]
        protected set(value) {
            bases.set(Attribute.DEFENSE, value)
        }

    var speed: Float
        get() = attributeMap[Attribute.SPEED]
        set(value) {
            attributeMap.set(Attribute.SPEED, value)
        }

    var baseSpeed
        get() = bases[Attribute.SPEED]
        protected set(value) {
            bases.set(Attribute.SPEED, value)
        }

    var attributeMap = AttributeMap()
        protected set

    private val modifiers: List<Modifier> = ArrayList()

    @SubscribeEvent
    override fun onCollision(event: CollisionEvent) {
    }
    // Attack & heal.
    /**
     * <h1>Attack!!!</h1>
     *
     * @param value The attack value.
     */
    @Deprecated("Use {@link #damage(double, DamageSource)} instead.")
    fun damage(value: Double) {
        if (attributeMap.get(Attribute.DEFENSE) == 0f) {
            destroy()
        }
        damageValue -= value.toFloat()
        checkDamage()
    }

    /**
     * <h1>Attack!!!</h1>
     *
     * @param value  the attack value.
     * @param source the damage source.
     */
    open fun damage(value: Double, source: EntityDamageSource?) {
        if (attributeMap.get(Attribute.DEFENSE) == 0f) {
            PreGameLoader.LOGGER.warn("Defense is zero")
            destroy()
        }
        damageValue -= value.toFloat()
        checkDamage()
    }

    fun destroy() {
        damageValue = 0f
        checkDamage()
    }

    fun restoreDamage(value: Float) {
        damageValue += value
        damageValue = MathHelper.clamp(damageValue, 0f, attributeMap.get(Attribute.MAX_DAMAGE))
    }

    protected open fun checkDamage() {
        if (damageValue <= 0) {
            delete()
        }
    }

    override fun getState(): CompoundTag {
        val state = super.getState()
        val bases = bases.write(ListTag(CompoundTag::class.java))
        state.put("Bases", bases)
        var attributes: ListTag<CompoundTag?> =
            ListTag(CompoundTag::class.java)
        attributes = attributeMap.write(attributes)
        state.put("Attributes", attributes)
        val effects = ListTag(CompoundTag::class.java)
        for (effectInstance in activeEffects) {
            val effectDocument = effectInstance.write(CompoundTag())
            effects.add(effectDocument)
        }
        state.put("Effects", effects)
        val position = CompoundTag()
        position.putFloat("x", x)
        position.putFloat("y", y)
        state.put("position", position)
        state.putLong("id", entityId)
        state.putString("name", type.registryName.toString())
        state.putFloat("damageValue", damageValue)
        return state
    }

    override fun setState(state: CompoundTag) {
        val attributes = state.getListTag("Attributes", CompoundTag::class.java)
        attributeMap = AttributeMap().read(attributes)
        val bases = state.getListTag("Bases", CompoundTag::class.java)
        this.bases = AttributeMap().read(bases)
        val position = state.getCompoundTag("position")
        x = position.getFloat("x")
        y = position.getFloat("y")

        // TODO: Update to locate entity class, from ResourceLocation using EntityType.
//        val entityTypeKey = ResourceLocation.fromString(state.getString("Name"))
//        this.type = EntityTypeRegistry.INSTANCE.get(entityTypeKey);
//        type = Registry.getRegistry(EntityType::class.java)[entityTypeKey]!!
    }

    override fun toSimpleString(): String {
        return registryName.toString() + "@(" + x.roundToInt() + "," + y.roundToInt() + ")"
    }

    override fun toAdvancedString(): String {
        val nbt = getState()
        val data = nbt.toString()
        return "$registryName@$data"
    }
}