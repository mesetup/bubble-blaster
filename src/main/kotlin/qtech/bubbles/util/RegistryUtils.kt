package qtech.bubbles.util

import qtech.bubbles.registry.`object`.ObjectHolder

object RegistryUtils {
    fun getObjectHolder(clazz: Class<*>): ObjectHolder? {
        return clazz.getDeclaredAnnotation(ObjectHolder::class.java)
    }
}