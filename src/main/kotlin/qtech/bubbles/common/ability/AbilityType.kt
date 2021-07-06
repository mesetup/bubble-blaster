package qtech.bubbles.common.ability

import qtech.bubbles.common.RegistryEntry
import java.util.*
import java.util.function.Supplier

/**
 * @param <T>
</T> */
class AbilityType<T : Ability<*>?>(// Types
    //    public static final AbilityType<TeleportAbility> TELEPORT_ABILITY = new AbilityType<>(TeleportAbility.class, "teleport_ability");
    // Fields
    private val ability: Supplier<T>
) : RegistryEntry() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AbilityType<*>
        return registryName == that.registryName
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }

    fun getAbility(): T {
        return ability.get()
    }
}