package com.qtech.bubbles.registry

import com.qtech.bubbles.common.IRegistryEntry
import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.init.ObjectInit
import com.qtech.bubbles.common.maps.SequencedHashMap
import com.qtech.bubbles.event.Bus.qBubblesEventBus
import com.qtech.bubbles.event.SubscribeEvent
import java.util.*
import java.util.AbstractMap.SimpleEntry

open class Registry<T : IRegistryEntry?> protected constructor(clazz: Class<T>, registryName: ResourceLocation) {
    private val registry = SequencedHashMap<ResourceLocation, T>()
    val type: Class<T>
    val registryName: ResourceLocation

    /**
     * Returns the registered instance from the given [ResourceLocation]
     *
     * @param key the namespaced key.
     * @return an registered instance of the type [T].
     * @throws ClassCastException if the type is invalid.
     */
    operator fun get(key: ResourceLocation): T? {
        require(registry.containsKey(key)) { "Cannot find object for: " + key + " | type: " + type.simpleName }
        return registry[key]
    }

    operator fun contains(rl: ResourceLocation?): Boolean {
        return registry.containsKey(rl)
    }

    @SubscribeEvent
    fun onRegistryDump() {
        println("Registry dump: " + type.simpleName)
        //        System.out.println(hashCode());
        for ((rl, `object`) in entries()) {
            println("  ($rl) -> $`object`")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun register(clazz: Class<out ObjectInit<T>?>, addonId: String?) {
        // Get fields.
        val fields = clazz.declaredFields
        println("Registering object-initialization class: $clazz")

        // Loop fields.
        for (field in fields) {
            // Try.
            try {
                if (type.isAssignableFrom(field.type)) {
                    // Get register-object.
                    val `object` = field[null] as T

                    // Set key.
                    if (`object`!!.registryName == null) {
                        if (`object`.isTempRegistryName) {
                            `object`.updateRegistryName(addonId!!)
                        } else {
                            `object`.registryName = ResourceLocation(addonId, field.name.lowercase())
                        }
                    }

                    // Register if it isn't.
                    if (!values().contains(`object`)) {
                        register(`object`.registryName!!, `object`)
                    }
                }
            } catch (e: IllegalAccessException) {
                // Oops, some problem occurred.
                e.printStackTrace()
            }
        }
    }

    /**
     * Register an object.
     *
     * @param rl  the resource location.
     * @param val the register item value.
     */
    fun register(rl: ResourceLocation, `val`: T) {
        registry[rl] = `val`
    }

    fun values(): MutableCollection<T> {
        return Collections.unmodifiableCollection(registry.values)
    }

    fun keys(): MutableSet<ResourceLocation> {
        return Collections.unmodifiableSet(registry.keys)
    }

    fun entries(): Set<Map.Entry<ResourceLocation, T>> {
        // I do this because IDE won's accept dynamic values ans keys.
        val values = ArrayList(values())
        val keys = ArrayList(keys())
        check(keys.size == values.size) { "Keys and values have different lengths." }
        val entrySet: MutableSet<SimpleEntry<ResourceLocation, T>> = HashSet()
        for (i in keys.indices) {
            entrySet.add(SimpleEntry(keys[i], values[i]))
        }
        return Collections.unmodifiableSet(entrySet)
    }

    @Suppress("UNCHECKED_CAST")
    fun registrable(rl: ResourceLocation, `object`: RegistryEntry) {
        if (type.isAssignableFrom(`object`.javaClass)) {
            register(rl, `object` as T)
        }
    }

    companion object {
        private val metricsMap = HashMap<Class<*>, Registry<*>>()
        fun <T : RegistryEntry?> create(clazz: Class<T>, registryName: ResourceLocation): Registry<T> {
            return Registry(clazz, registryName)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : RegistryEntry?> getRegistry(objType: Class<T>): Registry<T> {
            return metricsMap[objType] as Registry<T>
        }
    }

    init {
        check(!metricsMap.containsKey(clazz))
        this.registryName = registryName
        type = clazz
        qBubblesEventBus.register(this)
        metricsMap[type] = this
    }
}