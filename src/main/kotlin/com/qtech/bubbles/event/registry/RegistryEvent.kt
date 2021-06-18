package com.qtech.bubbles.event.registry

import com.qtech.bubbles.common.IRegistryEntry
import com.qtech.bubbles.event.Event
import com.qtech.bubbles.registry.Registry
import java.util.*

class RegistryEvent {
    class Register<T : IRegistryEntry>(val registry: Registry<T>) : Event() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val register = other as Register<*>
            return registry.type == register.registry.type
        }

        override fun hashCode(): Int {
            return Objects.hash(registry.type)
        }
    }

    class Dump : Event()
}