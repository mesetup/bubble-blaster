package com.qtech.bubbles.registry

/**
 * Base registry.
 *
 * @param <K> The type to use for values.. (KT = Key Type)
 * @param <V> The type for the registry. (VT = Value Type)
</V></K> */
abstract class AbstractRegistry<K, V> {
    protected open val registry = HashMap<K, V>()

    /**
     * Check if there's already an instance created of the registry.
     *
     * @param instance The registry instance to check.
     */
    fun checkInstance(instance: AbstractRegistry<K, V>?) {
        check(instance == null) { "Already created instance" }
    }

    abstract operator fun get(obj: K): V?
    abstract fun register(key: K, `val`: V)
    abstract fun values(): Collection<V>?
    abstract fun keys(): Set<K>
    @Throws(IllegalAccessException::class)
    abstract fun entries(): Set<Map.Entry<K, V>>

    companion object {
        var INSTANCE: AbstractRegistry<*, *>? = null
    }
}