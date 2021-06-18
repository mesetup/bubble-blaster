package com.qtech.bubbles.common.ability

import com.qtech.bubbles.ability.triggers.AbilityKeyTrigger
import com.qtech.bubbles.ability.triggers.types.AbilityKeyTriggerType
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.interfaces.StateHolder
import net.querz.nbt.tag.CompoundTag

abstract class Ability<T : Ability<T>>(val type: AbilityType<T>) : StateHolder {
    var cooldown = 0
    var value = 0
    abstract val triggerKey: Int

    /**
     * Method for the trigger type of the ability.
     *
     * @return the trigger type.
     */
    abstract val triggerType: AbilityTriggerType?

    /**
     * Method for have a key trigger for the Ability.
     *
     * @return the key trigger type. Null for no key trigger.
     */
    open val keyTriggerType: AbilityKeyTriggerType?
        get() = null

    /**
     * Method for key trigger event.
     *
     * @param trigger the key trigger.
     */
    open fun onKeyTrigger(trigger: AbilityKeyTrigger?) {}
    override fun getState(): CompoundTag {
        val document = CompoundTag()
        document.putInt("cooldown", cooldown)
        document.putInt("value", value)
        return document
    }

    fun onEntityTick() {
        if (isRegeneratable) {
            value += regenerationSpeed
        }
    }

    override fun setState(nbt: CompoundTag) {
        cooldown = nbt.getInt("cooldown")
        value = nbt.getInt("value")
    }

    abstract fun trigger(trigger: AbilityTrigger?)
    abstract fun triggerEntity()
    abstract fun isTriggerable(entity: Entity?): Boolean
    abstract val isRegeneratable: Boolean
    private val regenerationSpeed: Int
        get() = 1

    fun subtractValue(amount: Int) {
        value -= amount
    }

    fun addValue(amount: Int) {
        value -= amount
    }
}