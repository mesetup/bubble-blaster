package qtech.bubbles.ability.triggers.types

import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.common.init.ObjectInit
import java.util.*

class AbilityKeyTriggerType : RegistryEntry(), ObjectInit<AbilityKeyTriggerType?> {
    private val hash: Long = System.nanoTime()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val that = other as AbilityKeyTriggerType
        return hash == that.hash
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), hash)
    }

    companion object {
        val HOLD = AbilityKeyTriggerType()
    }
}
