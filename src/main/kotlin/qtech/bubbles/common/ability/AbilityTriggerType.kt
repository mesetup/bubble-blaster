package qtech.bubbles.common.ability

import qtech.bubbles.common.RegistryEntry
import qtech.bubbles.common.init.ObjectInit
import java.util.*

class AbilityTriggerType : RegistryEntry(), ObjectInit<AbilityTriggerType?> {
    private val hash: Long = System.currentTimeMillis()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val that = other as AbilityTriggerType
        return hash == that.hash
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), hash)
    }

    companion object {
        var KEY_TRIGGER = AbilityTriggerType()
    }
}