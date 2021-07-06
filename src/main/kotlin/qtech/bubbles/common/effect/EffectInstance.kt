package qtech.bubbles.common.effect

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.annotation.FieldsAreNonnullByDefault
import qtech.bubbles.annotation.MethodsReturnNonnullByDefault
import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.TagHolder
import qtech.bubbles.entity.Entity
import qtech.bubbles.registry.Registers
import qtech.bubbles.util.NbtUtils
import qtech.bubbles.util.helpers.MathHelper
import qtech.utilities.python.builtins.ValueError
import net.querz.nbt.tag.CompoundTag
import java.util.*
import javax.annotation.ParametersAreNonnullByDefault

@Suppress("UNUSED_PARAMETER", "unused")
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
class EffectInstance : TagHolder {
    val type: Effect
    var strength: Int
        private set
    var endTime: Long = 0
    var isActive = false
        private set
    override var tag = CompoundTag()
    var baseDuration: Long = 0
        private set

    /**
     * @throws ClassCastException when effect couldn't be recognized.
     */
    private constructor(nbt: CompoundTag) {
        tag = NbtUtils.getTagCompound(nbt)
        type = Registers.EFFECTS[ResourceLocation.fromString(nbt.getString("name"))]!!
        remainingTime = nbt.getLong("duration")
        baseDuration = nbt.getLong("baseDuration")
        strength = nbt.getInt("strength")
    }

    constructor(type: Effect, duration: Long, strength: Int) {
        if (strength < 1) {
            throw ValueError("Cannot create effect instance with strength < 1")
        }
        this.type = type
        this.strength = strength
        remainingTime = duration
    }

    fun allowMerge(): Boolean {
        return true
    }

    fun start(entity: Entity) {
        onStart(entity)
        BubbleBlaster.eventBus.register(this)
        isActive = true
    }

    fun stop(entity: Entity) {
        onStop(entity)
        try {
            BubbleBlaster.eventBus.unregister(this)
        } catch (ignored: IllegalArgumentException) {
        }
        isActive = false
    }

    fun tick(entity: Entity) {
        if (remainingTime <= 0.0) {
            isActive = false
            stop(entity)
        } else {
            type.tick(entity, this)
        }
    }

    fun onStart(entity: Entity) {
        type.onStart(this, entity)
    }

    fun onStop(entity: Entity) {
        type.onStop(entity)
    }

    private fun updateStrength(old: Int, _new: Int) {}
    fun addStrength() {
        val old = strength
        val output = (strength + 1).toByte()
        strength = MathHelper.clamp(output, 1, 255).toInt()
        updateStrength(old, strength)
    }

    fun addStrength(amount: Byte) {
        val old = strength
        val output = (strength + amount).toByte()
        strength = MathHelper.clamp(output, 1, 255).toInt()
        updateStrength(old, strength)
    }

    fun removeStrength() {
        val old = strength
        val output = (strength - 1).toByte()
        strength = MathHelper.clamp(output, 1, 255).toInt()
        updateStrength(old, strength)
    }

    fun removeStrength(amount: Byte) {
        val old = strength
        val output = (strength - amount).toByte()
        strength = MathHelper.clamp(output, 1, 255).toInt()
        updateStrength(old, strength)
    }

    @Throws(ValueError::class)
    fun setStrength(strength: Byte) {
        val old = strength.toInt()
        if (strength < 1) {
            throw ValueError("Tried to set strength less than 1.")
        }
        this.strength = strength.toInt()
        updateStrength(old, strength.toInt())
    }

    var remainingTime: Long
        get() = (endTime - System.currentTimeMillis()) / 1000
        set(time) {
            endTime = System.currentTimeMillis() + time * 1000
        }

    fun addTime(time: Long) {
        remainingTime += time
    }

    fun removeTime(time: Long) {
        remainingTime -= time
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        val effectInstance = other as EffectInstance
        return type == effectInstance.type
    }

    override fun hashCode(): Int {
        return Objects.hash(type)
    }

    fun write(nbt: CompoundTag): CompoundTag {
        NbtUtils.generateTagCompound(nbt, tag)
        nbt.putLong("baseDuration", baseDuration)
        nbt.putLong("duration", remainingTime)
        nbt.putInt("strength", strength)
        nbt.putString("name", type.registryName.toString())
        return nbt
    }

    val startTime: Long
        get() = endTime - baseDuration
}